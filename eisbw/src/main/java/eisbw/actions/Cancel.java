package eisbw.actions;

import java.util.List;

import bwapi.Race;
import bwapi.TechType;
import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
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
		return parameters.isEmpty() || (parameters.size() == 1 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.isEmpty()) {
			return unit.getType().isBuilding() || unit.getType().getRace() == Race.Zerg;
		} else {
			return true;
		}
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		if (!parameters.isEmpty()) {
			Numeral id = (Numeral) parameters.get(0);
			unit = this.api.getUnit(id.getValue().intValue());
		}

		if (unit == null) {
			return;
		} else if (unit.isMorphing()) {
			unit.cancelMorph();
		} else if (unit.isBeingConstructed()) {
			unit.cancelConstruction();
		} else if (unit.isUpgrading()) {
			unit.cancelUpgrade();
		} else if (unit.isTraining()) {
			unit.cancelTrain();
		} else if (unit.getTech() != null && unit.getTech() != TechType.None) {
			unit.cancelResearch();
		}
	}

	@Override
	public String toString() {
		return "cancel";
	}
}
