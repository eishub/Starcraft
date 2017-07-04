package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;

/**
 * @author Danny & Harm - Use a researched TechType.
 *
 */
public class Use extends StarcraftAction {
	/**
	 * The Use constructor.
	 *
	 * @param api
	 *            The BWAPI.
	 */
	public Use(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());
		return !techType.isTargetsPosition() && !techType.isTargetsUnits();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());

		unit.useTech(techType);
	}

	@Override
	public String toString() {
		return "ability(Type)";
	}
}
