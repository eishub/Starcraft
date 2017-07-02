package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Abstract class for Load actions.
 *
 */
public abstract class StarcraftLoadingAction extends StarcraftAction {
	/**
	 * The StarcraftLoadingAction constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StarcraftLoadingAction(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().spaceProvided() > 0;
	}
}
