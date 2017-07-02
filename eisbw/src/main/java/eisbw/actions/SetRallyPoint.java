package eisbw.actions;

import java.util.List;

import bwapi.TilePosition;
import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

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
	public SetRallyPoint(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 2 && parameters.get(0) instanceof Numeral && parameters.get(1) instanceof Numeral;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.setRallyPoint(new TilePosition(xpos, ypos).toPosition());
	}

	@Override
	public String toString() {
		return "setRallyPoint(x,y)";
	}
}
