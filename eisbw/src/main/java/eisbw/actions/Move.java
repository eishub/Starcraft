package eisbw.actions;

import java.util.List;

import bwapi.TilePosition;
import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit move to the specified location.
 *
 */
public class Move extends StarcraftMovableAction {
	/**
	 * The Move constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Move(bwapi.Game api) {
		super(api);
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.move(new TilePosition(xpos, ypos).toPosition());
	}

	@Override
	public String toString() {
		return "move(X,Y)";
	}
}
