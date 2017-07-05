package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 *
 */
public class DebugDraw extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugText constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public DebugDraw(bwapi.Game api, Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String text = parameters.get(0).toProlog();
		String id = (unit == null) ? "0" : Integer.toString(unit.getID());

		IDraw draw = new CustomDrawUnit(this.game, unit, text);
		this.game.addDraw(id, draw);
		if (!text.isEmpty()) {
			this.game.toggleDraw(id);
		}
	}

	@Override
	public String toString() {
		return "debugdraw(Text)";
	}
}
