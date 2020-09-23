package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Abstract class for Load actions.
 */
public abstract class StarcraftLoadingAction extends StarcraftAction {
	/**
	 * The StarcraftLoadingAction constructor.
	 *
	 * @param api The BWAPI
	 */
	public StarcraftLoadingAction(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.getSpaceProvided() > 0;
	}
}
