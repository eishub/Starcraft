package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Forfeits the match (mapAgent only).
 *
 */
public class Forfeit extends StarcraftAction {
	/**
	 * The Forfeit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Forfeit(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		this.api.exit();
	}

	@Override
	public String toString() {
		return "forfeit";
	}
}
