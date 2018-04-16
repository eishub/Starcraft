package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.unit.Bunker;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Transporter;
import org.openbw.bwapi4j.unit.Unit;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Unloads a specified unit.
 *
 */
public class UnloadUnit extends StarcraftLoadingAction {
	/**
	 * The UnloadUnit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public UnloadUnit(BW api) {
		super(api);
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		if (unit instanceof Transporter) {
			((Transporter) unit).unload((MobileUnit) target);
		} else if (unit instanceof Bunker) {
			((Bunker) unit).unload((MobileUnit) target);
		}
	}

	@Override
	public String toString() {
		return "unload(TargetId)";
	}
}
