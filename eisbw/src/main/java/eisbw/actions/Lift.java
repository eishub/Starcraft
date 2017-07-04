package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;

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
	public Lift(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding() && unit.getType().getRaceID() == RaceTypes.Terran.getID();
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
