package eisbw.actions;

import java.util.List;

import bwapi.Unit;
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
	public Forfeit(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, Action action) {
		this.api.leaveGame();
	}

	@Override
	public String toString() {
		return "forfeit";
	}
}
