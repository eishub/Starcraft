package eisbw.actions;

import java.util.HashMap;
import java.util.Map;

import jnibwapi.JNIBWAPI;

/**
 * @author Danny & Harm - The ActionProvider.
 *
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
	 * @param actionName
	 *            The name of the action.
	 * @return The action object with the specified action name.
	 */
	public StarcraftAction getAction(String actionName) {
		return this.actions.get(actionName);
	}

	/**
	 * Load all actions to the EIS environment.
	 *
	 * @param api
	 *            the API to pass into the actions.
	 */
	public void loadActions(JNIBWAPI api) {
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
		this.actions.put("setRallyPoint/1", new SetRallyUnit(api));
		this.actions.put("setRallyPoint/2", new SetRallyPoint(api));
		this.actions.put("lift/0", new Lift(api));
		this.actions.put("land/2", new Land(api));
		this.actions.put("buildAddon/1", new BuildAddon(api));
		this.actions.put("follow/1", new Follow(api));
		this.actions.put("load/1", new Load(api));
		this.actions.put("unload/1", new UnloadUnit(api));
		this.actions.put("unloadAll/0", new UnloadAll(api));
		this.actions.put("morph/1", new Morph(api));
		this.actions.put("patrol/2", new Patrol(api));
		this.actions.put("cancel/0", new Cancel(api));
		this.actions.put("cancel/1", new Cancel(api));
		this.actions.put("repair/1", new Repair(api));
		this.actions.put("forfeit/0", new Forfeit(api));
	}
}
