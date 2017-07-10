package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

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
	public Hold(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public void execute(Unit unit, Action action) {
		unit.holdPosition(false);
	}

	@Override
	public String toString() {
		return "hold";
	}
}
