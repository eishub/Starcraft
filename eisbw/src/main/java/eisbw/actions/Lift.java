package eisbw.actions;

import java.util.List;

import bwapi.Race;
import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Lifts up the unit, making it a flying unit until it
 *         lands.
 *
 */
public class Lift extends StarcraftAction {
	/**
	 * The Lift constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Lift(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding() && unit.getType().getRace() == Race.Terran;
	}

	@Override
	public void execute(Unit unit, Action action) {
		unit.lift();
	}

	@Override
	public String toString() {
		return "lift";
	}
}
