package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Cancels the action of a given unit.
 */
public class CancelUnit extends StarcraftAction {
	/**
	 * The CancelUnit constructor.
	 *
	 * @param api The BWAPI
	 */
	public CancelUnit(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 1 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final Numeral id = (Numeral) parameters.get(0);
		unit = this.api.getUnit(id.getValue().intValue());

		if (unit != null) {
			if (unit.isMorphing()) {
				unit.cancelMorph();
			} else {
				unit.cancelConstruction();
			}
		}
	}

	@Override
	public String toString() {
		return "cancel(TargetID)";
	}
}
