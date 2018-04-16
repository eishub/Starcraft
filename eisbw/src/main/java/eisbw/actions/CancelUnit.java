package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.Building;
import org.openbw.bwapi4j.unit.Cocoon;
import org.openbw.bwapi4j.unit.Egg;
import org.openbw.bwapi4j.unit.LurkerEgg;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Unit;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Cancels the action of a given unit.
 *
 */
public class CancelUnit extends StarcraftAction {
	/**
	 * The CancelUnit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public CancelUnit(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 1 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Numeral id = (Numeral) parameters.get(0);
		Unit target = this.api.getUnit(id.getValue().intValue());

		if (target instanceof Cocoon) {
			((Cocoon) target).cancelMorph();
		} else if (target instanceof Egg) {
			((Egg) target).cancelMorph();
		} else if (target instanceof LurkerEgg) {
			((LurkerEgg) target).cancelMorph();
		} else if (target instanceof Building) {
			((Building) target).cancelConstruction();
		}
	}

	@Override
	public String toString() {
		return "cancel(TargetID)";
	}
}
