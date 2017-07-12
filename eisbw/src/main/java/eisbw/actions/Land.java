package eisbw.actions;

import java.util.List;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Lands the flying unit on the specified location.
 *
 */
public class Land extends StarcraftMovableAction {
	/**
	 * The Land constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Land(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.land(new TilePosition(xpos, ypos));
	}

	@Override
	public String toString() {
		return "land(X,Y)";
	}
}
