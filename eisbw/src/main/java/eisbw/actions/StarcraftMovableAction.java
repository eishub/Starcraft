package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Abstract class for some of the Movable actions.
 *
 */
public abstract class StarcraftMovableAction extends StarcraftAction {
	/**
	 * The Starcraft MovableAction constructor.
	 *
	 * @param api
	 *            The BWAPI.
	 */
	public StarcraftMovableAction(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 2) {
			return parameters.get(0) instanceof Numeral && parameters.get(1) instanceof Numeral;
		} else {
			return false;
		}
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return !unit.isBeingConstructed();
	}
}
