package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.FlyingBuilding;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Lifts up the unit, making it a flying unit until it
 *         lands.
 *
 */
public class Lift extends StarcraftAction {
	/**
	 * The Lift constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Lift(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		((FlyingBuilding) unit).lift();
	}

	@Override
	public String toString() {
		return "lift";
	}
}
