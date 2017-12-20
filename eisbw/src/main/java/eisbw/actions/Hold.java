package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit patrol between his current location and
 *         the specified location.
 *
 */
public class Hold extends StarcraftMovableAction {
	/**
	 * The Patrol constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Hold(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		((MobileUnit) unit).holdPosition();
	}

	@Override
	public String toString() {
		return "hold";
	}
}
