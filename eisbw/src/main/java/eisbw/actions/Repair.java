package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class Repair extends StarcraftAction {
	public Repair(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isWorker() && type.getRaceID() == RaceTypes.Terran.getID();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
		final Unit target = this.api.getUnit(targetId);

		if (target != null && target.isCompleted()) {
			unit.repair(target, false);
		} else {
			unit.rightClick(target, false);
		}
	}

	@Override
	public String toString() {
		return "repair(TargetID)";
	}
}
