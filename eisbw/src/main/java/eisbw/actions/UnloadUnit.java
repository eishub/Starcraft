package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Unloads a specified unit.
 */
public class UnloadUnit extends StarcraftLoadingAction {
	/**
	 * The UnloadUnit constructor.
	 *
	 * @param api The BWAPI
	 */
	public UnloadUnit(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.unload(target);
	}

	@Override
	public String toString() {
		return "unload(TargetId)";
	}
}
