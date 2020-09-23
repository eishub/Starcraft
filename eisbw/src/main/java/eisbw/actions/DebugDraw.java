package eisbw.actions;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.BwapiAction;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 */
public class DebugDraw extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugText constructor.
	 *
	 * @param api The BWAPI
	 */
	public DebugDraw(final JNIBWAPI api, final Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return true;
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		// Empty, since we override execute(BwapiAction).
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(final BwapiAction action) {
		final List<Parameter> parameters = action.getAction().getParameters();
		final String text = StringEscapeUtils.unescapeJava(parameters.get(0).toProlog());
		final String name = action.getAgentName();

		final IDraw draw = new CustomDrawUnit(this.game, action.getUnit(), text);
		this.game.addDraw(name, draw);
		if (!text.isEmpty()) {
			this.game.toggleDraw(name);
		}
	}

	@Override
	public String toString() {
		return "debugdraw(Text)";
	}
}
