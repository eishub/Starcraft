package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

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
	public Stop(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return !unit.getType().isBuilding();
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
