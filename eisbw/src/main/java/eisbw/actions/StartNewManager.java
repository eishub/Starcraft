package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.Game;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class StartNewManager extends StarcraftAction {
	private final Game game;

	/**
	 * The StartManager constructor.
	 *
	 * @param api The BWAPI
	 */
	public StartNewManager(final JNIBWAPI api, final Game game) {
		super(api);
		this.game = game;
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
		this.game.startNewManager();
	}

	@Override
	public String toString() {
		return "startNewManager";
	}
}
