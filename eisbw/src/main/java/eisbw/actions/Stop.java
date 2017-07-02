package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Stops a unit from what it was doing.
 *
 */
public class Stop extends StarcraftAction {
	/**
	 * The Stop constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Stop(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		UnitType unitType = unit.getType();
		return !unitType.isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		unit.stop(false);
	}

	@Override
	public String toString() {
		return "stop";
	}
}
