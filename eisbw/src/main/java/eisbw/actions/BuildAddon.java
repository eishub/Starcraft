package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Builds an addon for the (terran) building.
 *
 */
public class BuildAddon extends StarcraftAction {
	/**
	 * The BuildAddon constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public BuildAddon(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 1 && parameters.get(0) instanceof Identifier) {
			UnitType ut = BwapiUtility.getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null && ut.isAddon();
		}
		return false;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding() && unit.getAddon() == null;
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String type = ((Identifier) parameters.get(0)).getValue();

		unit.buildAddon(BwapiUtility.getUnitType(type));
	}

	@Override
	public String toString() {
		return "buildAddon(Type)";
	}
}
