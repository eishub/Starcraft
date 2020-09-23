package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Lifts up the unit, making it a flying unit until it
 *         lands.
 */
public class Lift extends StarcraftAction {
	/**
	 * The Lift constructor.
	 *
	 * @param api The BWAPI
	 */
	public Lift(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		unit.lift();
	}

	@Override
	public String toString() {
		return "lift";
	}
}
