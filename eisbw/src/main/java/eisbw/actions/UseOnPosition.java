package eisbw.actions;

import java.util.List;

import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Ability which can be used on a specified location.
 *
 */
public class UseOnPosition extends StarcraftAction {
	/**
	 * The UseOnPosition constructor.
	 *
	 * @param api
	 *            The BWAPI.
	 */
	public UseOnPosition(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 3 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null
				&& parameters.get(1) instanceof Numeral && parameters.get(2) instanceof Numeral;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return techType.targetsPosition();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		int xpos = ((Numeral) parameters.get(1)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(2)).getValue().intValue();

		unit.useTech(techType, new TilePosition(xpos, ypos).toPosition());
	}

	@Override
	public String toString() {
		return "ability(Type,X,Y)";
	}
}
