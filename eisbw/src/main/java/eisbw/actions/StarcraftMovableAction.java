package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

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
	public StarcraftMovableAction(BW api) {
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
	public boolean canExecute(UnitType type, Action action) {
		return type.canMove() || type.isFlyingBuilding();
	}
}
