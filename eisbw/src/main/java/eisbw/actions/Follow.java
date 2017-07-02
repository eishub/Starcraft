package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit follow an other specified unit.
 *
 */
public class Follow extends StarcraftAction {
	/**
	 * The Follow constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Follow(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		UnitType unitType = unit.getType();
		return unitType.canMove();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
		Unit target = this.api.getUnit(targetId);

		unit.follow(target, false);
	}

	@Override
	public String toString() {
		return "follow(targetID)";
	}
}