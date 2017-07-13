package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Cancels the action of a given unit.
 *
 */
public class CancelUnit extends StarcraftAction {
	/**
	 * The CancelUnit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public CancelUnit(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 1 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Numeral id = (Numeral) parameters.get(0);
		unit = this.api.getUnit(id.getValue().intValue());

		if (unit != null && unit.isMorphing()) {
			unit.cancelMorph();
		} else {
			unit.cancelConstruction();
		}
	}

	@Override
	public String toString() {
		return "cancel(TargetID)";
	}
}
