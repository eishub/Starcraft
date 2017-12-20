package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

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
	public Land(BW api) {
		super(api);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		// ((FlyingBuilding) unit).land(new Position(xpos, ypos, PosType.BUILD)); F
		// FIXME: interface not public in lib atm.
	}

	@Override
	public String toString() {
		return "land(X,Y)";
	}
}
