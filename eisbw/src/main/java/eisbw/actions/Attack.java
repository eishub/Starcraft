package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Makes the unit attack the specified unit.
 *
 */
public class Attack extends StarcraftAction {
	/**
	 * The Attack constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Attack(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isAttackCapable();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
		Unit target = this.api.getUnit(targetId);

		unit.attack(target, false);
	}

	@Override
	public String toString() {
		return "attack(TargetId)";
	}
}
