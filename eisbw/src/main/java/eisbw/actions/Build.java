package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.Drone;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Probe;
import org.openbw.bwapi4j.unit.SCV;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

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
	public Build(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 3 && parameters.get(0) instanceof Identifier) {
			UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null && ut.isBuilding() && parameters.get(1) instanceof Numeral
					&& parameters.get(2) instanceof Numeral;
		}
		return false;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isWorker();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String type = ((Identifier) parameters.get(0)).getValue();
		int tx = ((Numeral) parameters.get(1)).getValue().intValue();
		int ty = ((Numeral) parameters.get(2)).getValue().intValue();

		if (unit instanceof SCV) {
			((SCV) unit).build(new TilePosition(tx, ty), getUnitType(type));
		} else if (unit instanceof Probe) {
			((Probe) unit).build(new TilePosition(tx, ty).toPosition(), getUnitType(type));
		} else if (unit instanceof Drone) {
			// TODO: build not supported for drones in lib atm.
		}
	}

	@Override
	public String toString() {
		return "build(Type,X,Y)";
	}
}
