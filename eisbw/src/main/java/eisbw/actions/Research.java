package eisbw.actions;

import java.util.List;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UpgradeType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Researches a specified Tech Type.
 *
 */
public class Research extends StarcraftAction {
	/**
	 * The Research constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Research(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		boolean valid = parameters.size() == 1 && parameters.get(0) instanceof Identifier;
		if (valid) {
			TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
			UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());
			return techType != null || upgradeType != null;
		} else {
			return false;
		}
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());

		if (techType == null) {
			unit.upgrade(upgradeType);
		} else {
			unit.research(techType);
		}
	}

	@Override
	public String toString() {
		return "research(Type)";
	}
}
