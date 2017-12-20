package eisbw;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.InteractionHandler;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Unit;

import bwta.BWTA;
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

/**
 * @author Danny & Harm - The Listener of the BWAPI Events.
 *
 */
public class BwapiListener extends BwapiEvents {
	protected final Logger logger = Logger.getLogger("StarCraft Logger");
	protected BW bwapi; // overridden in test
	protected BWTA bwta;
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
		// File dll = (scDir.isEmpty()) ? new File("bwapi-data" + File.separator + "AI")
		// : new File(scDir);
		// File bwta = (scDir.isEmpty()) ? new File(dll + File.separator + "mapData")
		// : new File(scDir + File.separator + "bwapi-data" + File.separator + "BWTA");
		this.bwapi = new BW(this);
		this.bwta = new BWTA();
		this.game = game;
		this.actionProvider = new ActionProvider();
		this.actionProvider.loadActions(this.bwapi, this.game);
		this.pendingActions = new ConcurrentLinkedQueue<>();
		this.factory = new StarcraftUnitFactory(this.bwapi, this.bwta);
		this.debug = debug;
		this.invulnerable = invulnerable;
		this.speed = speed;

		IDraw mapInfo = new DrawMapInfo(game, this.bwapi, this.bwta);
		game.addDraw(Draw.MAP.name(), mapInfo);
		if (drawMapInfo) {
			mapInfo.toggle();
		}
		IDraw unitInfo = new DrawUnitInfo(game, this.bwapi);
		game.addDraw(Draw.UNITS.name(), unitInfo);
		if (drawUnitInfo) {
			unitInfo.toggle();
		}

		this.bwapi.startGame();
	}

	@Override
	public void onStart() {
		final InteractionHandler handler = this.bwapi.getInteractionHandler();
		this.bwta.analyze();
		// SET INIT SPEED (DEFAULT IS 50 FPS, WHICH IS 20 SPEED)
		// this.bwapi.setCommandOptimizationLevel(1); // 2 seems bugged for buildings
		// (FIXME: not implemented?)
		if (this.speed > 0) {
			handler.setLocalSpeed(1000 / this.speed);
		} else if (this.speed == 0) {
			handler.setLocalSpeed(this.speed);
		}

		// SET INIT INVULNERABLE PARAMETER
		if (this.invulnerable) {
			handler.sendText("power overwhelming");
		}

		// START THE DEBUG TOOLS
		if (this.debug) {
			handler.enableUserInput();
			this.debugwindow = new DebugWindow(this.game);
		}

		// UPDATE MAP INFO
		this.game.updateMap(this.bwapi, this.bwta);

		// KnowledgeExport.export();
	}

	@Override
	public void onFrame() {
		// GENERATE PERCEPTS
		int frame = this.bwapi.getInteractionHandler().getFrameCount();
		if ((frame % 50) == 0) {
			this.game.updateConstructionSites(this.bwapi, this.bwta);
		}
		if (this.nuke >= 0 && ++this.nuke == 50) {
			this.game.updateNukePerceiver(null);
			this.nuke = -1;
		}
		do {
			this.game.update(this.bwapi, this.bwta);
			if (frame == 0) {
				this.game.startManagers();
			}
			try { // always sleep 5ms to better facilitate running at speed 0
				Thread.sleep(5);
			} catch (InterruptedException ie) {
				break;
			} // wait until all the initial workers get an action request
		} while (frame == 0 && isRunning() && this.pendingActions.size() < 4);

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
			this.debugwindow.debug(this.bwapi.getInteractionHandler());
		}
		for (IDraw draw : this.game.getDraws()) {
			draw.draw(this.bwapi.getMapDrawer());
		}
	}

	@Override
	public void onUnitComplete(Unit unit) {
		if (unit instanceof PlayerUnit) {
			PlayerUnit pu = (PlayerUnit) unit;
			if (pu.getPlayer() == this.bwapi.getInteractionHandler().self() && this.game.getUnitName(pu) == null) {
				this.game.addUnit(pu, this.factory);
			}
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit instanceof PlayerUnit) {
			PlayerUnit pu = (PlayerUnit) unit;
			this.game.deleteUnit(pu);
			this.game.removeDraw(BwapiUtility.getName(pu));
			BwapiUtility.clearCache(pu);
		}
	}

	@Override
	public void onUnitMorph(Unit unit) {
		if (unit instanceof PlayerUnit) {
			PlayerUnit pu = (PlayerUnit) unit;
			UnitType type = BwapiUtility.getType(pu);
			if (type != null && type.getRace() != Race.Terran) { // siege tank hack
				onUnitDestroy(unit);
				onUnitComplete(unit);
			}
		}
	}

	@Override
	public void onUnitRenegade(Unit unit) {
		onUnitDestroy(unit);
	}

	@Override
	public void onNukeDetect(Position pos) {
		this.game.updateNukePerceiver(pos.toTilePosition());
		this.nuke = 0;
	}

	@Override
	public void onEnd(boolean winner) {
		this.game.updateEndGamePerceiver(winner);
		this.game.update(this.bwapi, this.bwta);

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
		this.bwapi.exit();
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
		PlayerUnit unit = this.game.getUnit(name); // can be null for the mapagent
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
		PlayerUnit unit = this.game.getUnit(name);
		return isSupportedByEnvironment(action)
				&& act.canExecute((unit == null) ? null : BwapiUtility.getType(unit), action);
	}

	private boolean isRunning() {
		return this.game != null && this.game.getEnvironment() != null
				&& this.game.getEnvironment().getState() != EnvironmentState.KILLED;
	}
}
