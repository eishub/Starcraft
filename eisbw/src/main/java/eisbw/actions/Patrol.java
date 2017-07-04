package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Makes the unit patrol between his current location and
 *         the specified location.
 *
 */
public class Patrol extends StarcraftMovableAction {
	/**
	 * The Patrol constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Patrol(JNIBWAPI api) {
		super(api);
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		Position pos = new Position(xpos, ypos, Position.PosType.BUILD);
		unit.patrol(pos, false);
	}

	@Override
	public String toString() {
		return "patrol(X,Y)";
	}
}
