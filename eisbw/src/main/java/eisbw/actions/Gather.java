package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Makes the unit gather from a specified resource.
 */
public class Gather extends StarcraftAction {
	/**
	 * The Gather constructor.
	 *
	 * @param api The BWAPI
	 */
	public Gather(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isWorker();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.gather(target, false);
	}

	@Override
	public String toString() {
		return "gather(TargetID)";
	}
}
