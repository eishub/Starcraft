package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.Assimilator;
import org.openbw.bwapi4j.unit.Drone;
import org.openbw.bwapi4j.unit.Extractor;
import org.openbw.bwapi4j.unit.MineralPatch;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Probe;
import org.openbw.bwapi4j.unit.Refinery;
import org.openbw.bwapi4j.unit.SCV;
import org.openbw.bwapi4j.unit.Unit;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit gather from a specified resource.
 *
 */
public class Gather extends StarcraftAction {
	/**
	 * The Gather constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Gather(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isWorker();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		if (target instanceof Extractor) {
			((Drone) unit).gather((Extractor) target);
		} else if (target instanceof Refinery) {
			((SCV) unit).gather((Refinery) target);
		} else if (target instanceof Assimilator) {
			// ((Probe) unit).gather((Assimilator) target); FIXME: mistake in lib
		} else if (target instanceof MineralPatch) {
			if (unit instanceof Drone) {
				((Drone) unit).gather((MineralPatch) target);
			} else if (unit instanceof SCV) {
				((SCV) unit).gather((MineralPatch) target);
			} else if (unit instanceof Probe) {
				((Probe) unit).gather((MineralPatch) target);
			}
		}
	}

	@Override
	public String toString() {
		return "gather(TargetID)";
	}
}
