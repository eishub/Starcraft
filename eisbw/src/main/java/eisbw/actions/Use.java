package eisbw.actions;

import java.util.List;

import bwapi.TechType;
import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

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
	public Use(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return !techType.targetsPosition() && !techType.targetsUnit();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());

		unit.useTech(techType);
	}

	@Override
	public String toString() {
		return "ability(Type)";
	}
}
