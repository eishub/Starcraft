package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Forfeits the match (mapAgent only).
 */
public class Forfeit extends StarcraftAction {
	/**
	 * The Forfeit constructor.
	 *
	 * @param api The BWAPI
	 */
	public Forfeit(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return true;
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		this.api.leaveGame();
	}

	@Override
	public String toString() {
		return "forfeit";
	}
}
