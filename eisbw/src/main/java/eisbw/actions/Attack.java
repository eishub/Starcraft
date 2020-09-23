package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - Makes the unit attack the specified unit.
 */
public class Attack extends StarcraftAction {
	/**
	 * The Attack constructor.
	 *
	 * @param api The BWAPI
	 */
	public Attack(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return (type.isAttackCapable() || type == UnitTypes.Terran_Medic);
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
		final Unit target = this.api.getUnit(targetId);

		unit.attack(target, false);
	}

	@Override
	public String toString() {
		return "attack(TargetId)";
	}
}
