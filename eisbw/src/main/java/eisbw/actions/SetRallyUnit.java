package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Sets a rally point on a specified unit.
 *
 */
public class SetRallyUnit extends StarcraftAction {
	/**
	 * The SetRallyUnit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public SetRallyUnit(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.setRallyPoint(target);
	}

	@Override
	public String toString() {
		return "setRallyUnit(TargetId)";
	}
}
