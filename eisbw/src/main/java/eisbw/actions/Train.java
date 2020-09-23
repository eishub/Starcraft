package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - Trains a specified unit from a production facility.
 */
public class Train extends StarcraftAction {
	/**
	 * The Train constructor.
	 *
	 * @param api The BWAPI
	 */
	public Train(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& getUnitType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isProduceCapable() || type == UnitTypes.Terran_Nuclear_Silo;
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final String tobuild = ((Identifier) parameters.get(0)).getValue();
		final UnitType unitType = getUnitType(tobuild);

		unit.train(unitType);
	}

	@Override
	public String toString() {
		return "train(Type)";
	}
}
