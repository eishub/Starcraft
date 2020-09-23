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
 * @author Danny & Harm - Lands the flying unit on the specified location.
 */
public class Land extends StarcraftMovableAction {
	/**
	 * The Land constructor.
	 *
	 * @param api The BWAPI
	 */
	public Land(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		final int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.land(new Position(xpos, ypos, PosType.BUILD));
	}

	@Override
	public String toString() {
		return "land(X,Y)";
	}
}
