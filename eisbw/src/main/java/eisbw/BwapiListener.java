package eisbw;

import java.io.File;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eisbw.actions.ActionProvider;
import eisbw.actions.StarcraftAction;
import eisbw.debugger.DebugWindow;
import eisbw.debugger.Draw;
import eisbw.debugger.draw.DrawMapInfo;
import eisbw.debugger.draw.DrawUnitInfo;
import eisbw.debugger.draw.IDraw;
import eisbw.units.StarcraftUnitFactory;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - The Listener of the BWAPI Events.
 *
 */
public class BwapiListener extends BwapiEvents {
	protected final Logger logger = Logger.getLogger("StarCraft Logger");
	protected JNIBWAPI bwapi; // overridden in test
	protected final Game game;
	protected final ActionProvider actionProvider;
	protected final Queue<BwapiAction> pendingActions;
	protected final StarcraftUnitFactory factory;
	protected final boolean debug;
	protected final boolean invulnerable;
	protected final int speed;
	protected DebugWindow debugwindow;
	protected int nuke = -1;

	/**
	 * Event listener for BWAPI.
	 *
	 * @param game
	 *            - the game data class
	 * @param debugmode
	 *            - true iff debugger should be attached
	 */
	public BwapiListener(Game game, String scDir, boolean debug, boolean drawMapInfo, boolean drawUnitInfo,
			boolean invulnerable, int speed) {
		File dll = (scDir.isEmpty()) ? new File("bwapi-data" + File.separator + "AI") : new File(scDir);
		File bwta = (scDir.isEmpty()) ? new File(dll + File.separator + "mapData")
				: new File(scDir + File.separator + "bwapi-data" + File.separator + "BWTA");
		this.bwapi = new JNIBWAPI(this, dll, bwta);
		this.game = game;
		this.actionProvider = new ActionProvider();
		this.actionProvider.loadActions(this.bwapi, this.game);
		this.pendingActions = new ConcurrentLinkedQueue<>();
		this.factory = new StarcraftUnitFactory(this.bwapi);
		this.debug = debug;
		this.invulnerable = invulnerable;
		this.speed = speed;

		IDraw mapInfo = new DrawMapInfo(game);
		game.addDraw(Draw.MAP.name(), mapInfo);
		if (drawMapInfo) {
			mapInfo.toggle();
		}
		IDraw unitInfo = new DrawUnitInfo(game);
		game.addDraw(Draw.UNITS.name(), unitInfo);
		if (drawUnitInfo) {
			unitInfo.toggle();
		}

		new Thread() {
			@Override
			public void run() {
				Thread.currentThread().setPriority(MAX_PRIORITY);
				Thread.currentThread().setName("BWAPI thread");
				BwapiListener.this.bwapi.start();
			}
		}.start();
	}

	@Override
	public void matchStart() {
		// SET INIT SPEED (DEFAULT IS 50 FPS, WHICH IS 20 SPEED)
		this.bwapi.setCommandOptimizationLevel(1); // 2 seems bugged for buildings
		if (this.speed > 0) {
			this.bwapi.setGameSpeed(1000 / this.speed);
		} else if (this.speed == 0) {
			this.bwapi.setGameSpeed(this.speed);
		}

		// SET INIT INVULNERABLE PARAMETER
		if (this.invulnerable) {
			this.bwapi.sendText("power overwhelming");
		}

		// START THE DEBUG TOOLS
		if (this.debug) {
			this.bwapi.enableUserInput();
			this.debugwindow = new DebugWindow(this.game);
		}

		// UPDATE MAP INFO
		this.game.updateMap(this.bwapi);

		// KnowledgeExport.export();
	}

	@Override
	public void matchFrame() {
		// GENERATE PERCEPTS
		int frame = this.bwapi.getFrameCount();
		if ((frame % 50) == 0) {
			this.game.updateConstructionSites(this.bwapi);
		}
		if (this.nuke >= 0 && ++this.nuke == 50) {
			this.game.updateNukePerceiver(this.bwapi, null);
			this.nuke = -1;
		}
		do {
			this.game.update(this.bwapi);
			if (frame == 0) {
				this.game.startManagers();
			}
			try { // always sleep 5ms to better facilitate running at speed 0
				Thread.sleep(5);
			} catch (InterruptedException ie) {
				break;
			} // wait until all the initial workers get an action request
		} while (frame == 1 && isRunning() && this.pendingActions.size() < 4);

		// PERFORM ACTIONS
		Iterator<BwapiAction> actions = this.pendingActions.iterator();
		while (actions.hasNext()) {
			BwapiAction act = actions.next();
			StarcraftAction action = this.actionProvider.getAction(act.getAction());
			if (action != null) {
				action.execute(act.getUnit(), act.getAction());
			}
			actions.remove();
		}
		if (this.debugwindow != null) {
			this.debugwindow.debug(this.bwapi);
		}
		for (IDraw draw : this.game.getDraws()) {
			draw.draw(this.bwapi);
		}
	}

	@Override
	public void unitComplete(int id) {
		Unit unit = this.bwapi.getUnit(id);
		if (unit != null && unit.getPlayer() == this.bwapi.getSelf() && this.game.getUnitName(id) == null) {
			this.game.addUnit(unit, this.factory);
		}
	}

	@Override
	public void unitDestroy(int id) {
		String name = this.game.getUnitName(id);
		if (name != null) {
			this.game.removeDraw(name);
		}
		this.game.deleteUnit(id);
		BwapiUtility.clearCache(id);
	}

	@Override
	public void unitMorph(int id) {
		Unit unit = this.bwapi.getUnit(id);
		UnitType type = BwapiUtility.getType(unit);
		boolean isTerran = (type != null && type.getRaceID() == RaceTypes.Terran.getID());
		if (unit != null && !isTerran) { // siege tank hack
			unitDestroy(id);
			unitComplete(id);
		}
	}

	@Override
	public void unitRenegade(int id) {
		unitDestroy(id);
	}

	@Override
	public void nukeDetect(Position pos) {
		this.game.updateNukePerceiver(this.bwapi, pos);
		this.nuke = 0;
	}

	@Override
	public void matchEnd(boolean winner) {
		this.game.updateEndGamePerceiver(winner);
		this.game.update(this.bwapi);

		// have the winner percept perceived for 1 second
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignore) {
		}

		this.pendingActions.clear();
		if (this.debugwindow != null) {
			this.debugwindow.dispose();
		}

		// this.bwapi.leaveGame();
		this.game.clean();
	}

	/**
	 * Returns the current FPS.
	 *
	 * @return the current FPS.
	 */
	public int getFPS() {
		return (this.debugwindow == null) ? this.speed : this.debugwindow.getFPS();
	}

	/**
	 * Adds an action to the action queue, the action is then executed on the next
	 * frame.
	 *
	 * @param name
	 *            - the name of the unit.
	 * @param act
	 *            - the action.
	 * @throws ActException
	 *             - mandatory from EIS
	 */
	public void performEntityAction(String name, Action action) throws ActException {
		Unit unit = this.game.getUnit(name); // can be null for the mapagent
		if (isSupportedByEntity(action, name)) {
			BwapiAction apiAction = new BwapiAction(unit, action);
			if (!this.pendingActions.contains(apiAction)) {
				this.pendingActions.add(apiAction);
			}
		} else {
			this.logger.log(Level.WARNING,
					"The entity " + name + " is not able to perform the action " + action.getName());
		}
	}

	public boolean isSupportedByEnvironment(Action action) {
		StarcraftAction act = this.actionProvider.getAction(action);
		return act != null && act.isValid(action);
	}

	public boolean isSupportedByEntity(Action action, String name) {
		StarcraftAction act = this.actionProvider.getAction(action);
		Unit unit = this.game.getUnit(name);
		return isSupportedByEnvironment(action)
				&& act.canExecute((unit == null) ? null : BwapiUtility.getType(unit), action);
	}

	private boolean isRunning() {
		return this.game != null && this.game.getEnvironment() != null
				&& this.game.getEnvironment().getState() != EnvironmentState.KILLED;
	}
}
