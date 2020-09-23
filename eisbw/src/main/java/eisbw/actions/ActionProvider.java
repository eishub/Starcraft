package eisbw.actions;

import java.util.HashMap;
import java.util.Map;

import eis.iilang.Action;
import eisbw.Game;
import jnibwapi.JNIBWAPI;

/**
 * @author Danny & Harm - The ActionProvider.
 */
public class ActionProvider {
	private final Map<String, StarcraftAction> actions;

	/**
	 * The ActionProvider constructor.
	 */
	public ActionProvider() {
		this.actions = new HashMap<>();
	}

	/**
	 * @param action An EIS action.
	 * @return The StarcraftAction for the given EIS action
	 */
	public StarcraftAction getAction(final Action action) {
		return this.actions.get(action.getName() + "/" + action.getParameters().size());
	}

	/**
	 * Load all actions to the EIS environment.
	 *
	 * @param api the API to pass into the actions.
	 */
	public void loadActions(final JNIBWAPI api, final Game game) {
		this.actions.put("attack/1", new Attack(api));
		this.actions.put("attack/2", new AttackMove(api));
		this.actions.put("build/3", new Build(api));
		this.actions.put("gather/1", new Gather(api));
		this.actions.put("move/2", new Move(api));
		this.actions.put("train/1", new Train(api));
		this.actions.put("stop/0", new Stop(api));
		this.actions.put("ability/1", new Use(api));
		this.actions.put("ability/2", new UseOnTarget(api));
		this.actions.put("ability/3", new UseOnPosition(api));
		this.actions.put("research/1", new Research(api));
		this.actions.put("lift/0", new Lift(api));
		this.actions.put("land/2", new Land(api));
		this.actions.put("buildAddon/1", new BuildAddon(api));
		this.actions.put("follow/1", new Follow(api));
		this.actions.put("hold/0/1", new Hold(api));
		this.actions.put("load/1", new Load(api));
		this.actions.put("unload/1", new UnloadUnit(api));
		this.actions.put("unloadAll/0", new UnloadAll(api));
		this.actions.put("morph/1", new Morph(api));
		this.actions.put("patrol/2", new Patrol(api));
		this.actions.put("cancel/0", new Cancel(api));
		this.actions.put("cancel/1", new CancelUnit(api));
		this.actions.put("repair/1", new Repair(api));
		this.actions.put("forfeit/0", new Forfeit(api));
		this.actions.put("debugdraw/1", new DebugDraw(api, game));
		this.actions.put("debugdraw/2", new DebugDrawUnit(api, game));
		this.actions.put("debugdraw/3", new DebugDrawMap(api, game));
		this.actions.put("startNewManager/0", new StartNewManager(api, game));
	}
}
