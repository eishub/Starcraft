package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Loads a unit into a specified other unit.
 */
public class Load extends StarcraftLoadingAction {
	/**
	 * The Load constructor.
	 *
	 * @param api The BWAPI
	 */
	public Load(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.load(target, false);
	}

	@Override
	public String toString() {
		return "load(TargetID)";
	}
}
