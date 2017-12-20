package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Stops a unit from what it was doing.
 *
 */
public class Stop extends StarcraftMovableAction {
	/**
	 * The Stop constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Stop(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return super.canExecute(type, action) || type == UnitType.Zerg_Larva;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		((MobileUnit) unit).stop(false);
	}

	@Override
	public String toString() {
		return "stop";
	}
}
