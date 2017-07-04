package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Makes the (worker) unit build on the specified (not
 *         obstructed) location.
 *
 */
public class Build extends StarcraftAction {
	/**
	 * The Build constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Build(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 3 && parameters.get(0) instanceof Identifier) {
			UnitType ut = BwapiUtility.getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null && ut.isBuilding() && parameters.get(1) instanceof Numeral
					&& parameters.get(2) instanceof Numeral;
		}
		return false;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isWorker();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String type = ((Identifier) parameters.get(0)).getValue();
		int tx = ((Numeral) parameters.get(1)).getValue().intValue();
		int ty = ((Numeral) parameters.get(2)).getValue().intValue();

		unit.build(new Position(tx, ty, PosType.BUILD), BwapiUtility.getUnitType(type));
	}

	@Override
	public String toString() {
		return "build(Type,X,Y)";
	}
}
