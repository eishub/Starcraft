package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

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
	public Forfeit(JNIBWAPI api) {
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
	public void execute(Unit unit, Action action) {
		this.api.leaveGame();
	}

	@Override
	public String toString() {
		return "forfeit";
	}
}
