package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Use a researched TechType.
 */
public class Use extends StarcraftAction {
	/**
	 * The Use constructor.
	 *
	 * @param api The BWAPI.
	 */
	public Use(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return !techType.isTargetsPosition() && !techType.isTargetsUnits();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());

		unit.useTech(techType);
	}

	@Override
	public String toString() {
		return "ability(Type)";
	}
}
