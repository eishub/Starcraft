package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Makes the unit morph into a specified unit.
 *
 */
public class Morph extends StarcraftAction {
	/**
	 * The Morph constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Morph(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 1 && parameters.get(0) instanceof Identifier) {
			UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null;
		}
		return false;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.getRaceID() == RaceTypes.Zerg.getID();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String type = ((Identifier) parameters.get(0)).getValue();

		unit.morph(getUnitType(type));
	}

	@Override
	public String toString() {
		return "morph(Type)";
	}
}
