package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Ability which can be used on a specified unit.
 */
public class UseOnTarget extends StarcraftAction {
	/**
	 * The UseOnTarget constructor.
	 *
	 * @param api The BWAPI.
	 */
	public UseOnTarget(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 2 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null
				&& parameters.get(1) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return techType.isTargetsUnits();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		final Unit target = this.api.getUnit(((Numeral) parameters.get(1)).getValue().intValue());

		unit.useTech(techType, target);
	}

	@Override
	public String toString() {
		return "ability(Type,TargetId)";
	}
}
