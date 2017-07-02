package eisbw;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eisbw.actions.ActionProvider;
import eisbw.actions.StarcraftAction;
import eisbw.debugger.DebugWindow;
import eisbw.debugger.draw.DrawMapInfo;
import eisbw.debugger.draw.DrawUnitInfo;
import eisbw.debugger.draw.IDraw;
import eisbw.units.StarcraftUnitFactory;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;

/**
 * @author Danny & Harm - The Listener of the BWAPI Events.
 *
 */
public class BwapiListener extends BwapiEvents {
	protected final Logger logger = Logger.getLogger("StarCraft Logger");
	protected JNIBWAPI bwapi; // overridden in test
	protected final Game game;
	protected final ActionProvider actionProvider;
	protected final Map<Unit, Action> pendingActions;
	protected final StarcraftUnitFactory factory;
	protected final boolean debug;
	protected final IDraw drawMapInfo;
	protected final IDraw drawUnitInfo;
	protected final boolean invulnerable;
	protected final int speed;
	protected int count = 0;
	protected int nuke = -1;
	protected DebugWindow debugwindow;

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
		File bwta = new File(scDir + File.separator + "bwapi-data" + File.separator + "BWTA");
		if (!bwta.isDirectory()) {
			bwta = new File("mapData");
		}
		this.bwapi = new JNIBWAPI(this, bwta);
		this.game = game;
		this.actionProvider = new ActionProvider();
		this.actionProvider.loadActions(this.bwapi);
		this.pendingActions = new ConcurrentHashMap<>();
		this.factory = new StarcraftUnitFactory(this.bwapi);
		this.debug = debug;
		this.drawMapInfo = new DrawMapInfo(game);
		if (drawMapInfo) {
			this.drawMapInfo.toggle();
		}
		this.drawUnitInfo = new DrawUnitInfo(game);
		if (drawUnitInfo) {
			this.drawUnitInfo.toggle();
		}
		this.invulnerable = invulnerable;
		this.speed = speed;

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
			this.debugwindow = new DebugWindow(this.game);
			this.bwapi.enableUserInput();
		}

		// DO INITIAL UPDATES
		this.game.mapAgent();
		this.game.updateMap(this.bwapi);
		this.game.updateConstructionSites(this.bwapi);
		this.game.updateFrameCount(this.count);

		// KnowledgeExport.export();
	}

	@Override
	public void matchFrame() {
		// GENERATE PERCEPTS
		if ((++this.count % 50) == 0) {
			this.game.updateConstructionSites(this.bwapi);
			this.game.updateFrameCount(this.count);
		}
		if (this.nuke >= 0 && ++this.nuke == 50) {
			this.game.updateNukePerceiver(this.bwapi, null);
			this.nuke = -1;
		}
		do {
			this.game.update(this.bwapi);
			try { // always sleep 1ms to better facilitate running at speed 0
				Thread.sleep(1);
			} catch (InterruptedException ignore) {
			} // wait until all the initial workers get an action request
		} while (this.count == 1 && isRunning() && this.pendingActions.size() < 4);

		// PERFORM ACTIONS
		for (final Unit unit : this.pendingActions.keySet()) {
			Action act = this.pendingActions.remove(unit);
			StarcraftAction action = getAction(act);
			if (action != null) {
				action.execute(unit, act);
			}
		}
		if (this.debugwindow != null) {
			this.debugwindow.debug(this.bwapi);
		}
		this.drawMapInfo.draw(this.bwapi);
		this.drawUnitInfo.draw(this.bwapi);
	}

	@Override
	public void unitComplete(int id) {
		Unit unit = this.bwapi.getUnit(id);
		if (this.bwapi.getMyUnits().contains(unit) && !this.game.getUnits().getUnitNames().containsKey(id)) {
			this.game.getUnits().addUnit(unit, this.factory);
		}
	}

	@Override
	public void unitDestroy(int id) {
		String unitName = this.game.getUnits().getUnitNames().get(id);
		if (unitName != null) {
			Unit deleted = this.game.getUnits().deleteUnit(unitName, id);
			this.pendingActions.remove(deleted);
		}
	}

	@Override
	public void unitMorph(int id) {
		Unit unit = this.bwapi.getUnit(id);
		if (unit.getType().getRaceID() != RaceTypes.Terran.getID()) {
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
		this.game.updateEndGamePerceiver(this.bwapi, winner);
		this.game.update(this.bwapi);

		// have the winner percept perceived for 1 second before all agents
		// are removed
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException ignore) {
		}

		this.pendingActions.clear();
		if (this.debugwindow != null) {
			this.debugwindow.dispose();
		}
		this.bwapi.leaveGame();
		this.game.clean();
	}

	protected boolean isSupportedByEntity(Action act, String name) {
		Unit unit = this.game.getUnits().getUnits().get(name);
		StarcraftAction action = getAction(act);
		return action != null && action.isValid(act) && action.canExecute(unit, act);
	}

	/**
	 * @param action
	 *            The inserted requested action.
	 * @return The requested Starcraft Action.
	 */
	public StarcraftAction getAction(Action action) {
		return this.actionProvider.getAction(action.getName() + "/" + action.getParameters().size());
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
	 * Adds an action to the action queue, the action is then executed on the
	 * next frame.
	 *
	 * @param name
	 *            - the name of the unit.
	 * @param act
	 *            - the action.
	 * @throws ActException
	 *             - mandatory from EIS
	 */
	public void performEntityAction(String name, Action act) throws ActException {
		Unit unit = this.game.getUnits().getUnits().get(name);
		if (isSupportedByEntity(act, name)) {
			this.pendingActions.put(unit, act);
		} else {
			this.logger.log(Level.WARNING,
					"The Entity: " + name + " is not able to perform the action: " + act.getName());
		}
	}

	private boolean isRunning() {
		return this.game != null && this.game.getEnvironment() != null
				&& this.game.getEnvironment().getState() != EnvironmentState.KILLED;
	}
}
