package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit patrol between his current location and
 *         the specified location.
 *
 */
public class Hold extends StarcraftMovableAction {
	/**
	 * The Patrol constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Hold(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public void execute(Unit unit, Action action) {
		unit.holdPosition();
	}

	@Override
	public String toString() {
		return "hold";
	}
}
