package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Makes the (worker) unit build on the specified (not
 *         obstructed) location.
 */
public class Build extends StarcraftAction {
	/**
	 * The Build constructor.
	 *
	 * @param api The BWAPI
	 */
	public Build(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 3 && parameters.get(0) instanceof Identifier) {
			final UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null && ut.isBuilding() && parameters.get(1) instanceof Numeral
					&& parameters.get(2) instanceof Numeral;
		}
		return false;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isWorker();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final String type = ((Identifier) parameters.get(0)).getValue();
		final int tx = ((Numeral) parameters.get(1)).getValue().intValue();
		final int ty = ((Numeral) parameters.get(2)).getValue().intValue();

		unit.build(new Position(tx, ty, PosType.BUILD), getUnitType(type));
	}

	@Override
	public String toString() {
		return "build(Type,X,Y)";
	}
}
