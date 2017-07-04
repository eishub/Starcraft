package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Trains a specified unit from a production facility.
 *
 */
public class Train extends StarcraftAction {
	/**
	 * The Train constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Train(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& BwapiUtility.getUnitType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return true; // edge cases such as reavers or nuclear silos
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String tobuild = ((Identifier) parameters.get(0)).getValue();
		UnitType unitType = BwapiUtility.getUnitType(tobuild);

		unit.train(unitType);
	}

	@Override
	public String toString() {
		return "train(Type)";
	}

}
