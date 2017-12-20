package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.Game;

public class StartNewManager extends StarcraftAction {
	private final Game game;

	/**
	 * The StartManager constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StartNewManager(BW api, Game game) {
		super(api);
		this.game = game;
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
		this.game.startNewManager();
	}

	@Override
	public String toString() {
		return "startNewManager";
	}
}
