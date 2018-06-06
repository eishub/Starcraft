package eisbw.actions;

import eis.iilang.Action;
import eisbw.BwapiAction;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UpgradeType;

/**
 * @author Danny & Harm - Abstract class for all the actions.
 *
 */
public abstract class StarcraftAction {
	protected final JNIBWAPI api;

	/**
	 * The StarcraftAction constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StarcraftAction(JNIBWAPI api) {
		this.api = api;
	}

	// Mocked in tests
	protected UnitType getUnitType(String name) {
		return BwapiUtility.getUnitType(name);
	}

	// Mocked in tests
	protected TechType getTechType(String name) {
		return BwapiUtility.getTechType(name);
	}

	// Mocked in tests
	protected UpgradeType getUpgradeType(String name) {
		return BwapiUtility.getUpgradeType(name);
	}

	/**
	 * @param action
	 *            The evaluated action.
	 * @return A boolean which indicates whether the parameters of the action are
	 *         valid.
	 */
	public abstract boolean isValid(Action action);

	/**
	 * @param type
	 *            The type of the unit performing the action.
	 * @param action
	 *            The evaluated action.
	 * @return A boolean which indicated wheter the specified unit can execute the
	 *         action.
	 */
	public abstract boolean canExecute(UnitType type, Action action);

	/**
	 * @param unit
	 *            The unit performing the action.
	 * @param action
	 *            The evaluated action.
	 */
	public abstract void execute(Unit unit, Action action);

	/**
	 * Executes this action with the specified BwapiAction. Can be
	 * overridden by subclasses if they need access to the raw
	 * BwapiAction object.
	 *
	 * @param action
	 *              The action to execute.
	 */
	public void execute(BwapiAction action) {
		this.execute(action.getUnit(), action.getAction());
	}

	@Override
	public abstract String toString();

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof StarcraftAction)) {
			return false;
		} else {
			return toString().equals(obj.toString());
		}
	}
}
