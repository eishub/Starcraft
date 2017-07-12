package eisbw.actions;

import java.util.List;

import bwapi.Race;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Cancels the action of the current unit.
 *
 */
public class Cancel extends StarcraftAction {
	/**
	 * The Cancel constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Cancel(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isBuilding() || type.canProduce() || type == UnitType.Terran_Nuclear_Silo;
	}

	@Override
	public void execute(Unit unit, Action action) {
		if (unit.isTraining()) {
			unit.cancelTrain();
		} else if (unit.getTech() != null && unit.getTech() != TechType.None) {
			unit.cancelResearch();
		} else if (unit.getUpgrade() != null && unit.getUpgrade() != UpgradeType.None) {
			unit.cancelUpgrade();
		} else if (unit.getType().getRace() == Race.Terran) {
			unit.cancelAddon();
		}
	}

	@Override
	public String toString() {
		return "cancel";
	}
}
