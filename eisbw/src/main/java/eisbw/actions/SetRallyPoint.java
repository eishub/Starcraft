package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Sets a rally point on a specified location.
 *
 */
public class SetRallyPoint extends StarcraftAction {
	/**
	 * The SetRallyPoint constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public SetRallyPoint(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 2 && parameters.get(0) instanceof Numeral && parameters.get(1) instanceof Numeral;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.setRallyPoint(new Position(xpos, ypos, PosType.BUILD));
	}

	@Override
	public String toString() {
		return "setRallyPoint(X,Y)";
	}
}
