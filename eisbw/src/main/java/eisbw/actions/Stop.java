package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - Stops a unit from what it was doing.
 *
 */
public class Stop extends StarcraftMovableAction {
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
	public boolean canExecute(UnitType type, Action action) {
		return super.canExecute(type, action) || type == UnitTypes.Zerg_Larva;
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
