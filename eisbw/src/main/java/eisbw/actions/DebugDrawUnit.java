package eisbw.actions;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 */
public class DebugDrawUnit extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugDrawUnit constructor.
	 *
	 * @param api The BWAPI
	 */
	public DebugDrawUnit(final JNIBWAPI api, final Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 2 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final Numeral id = (Numeral) parameters.get(0);
		unit = this.api.getUnit(id.getValue().intValue());
		if (BwapiUtility.isValid(unit)) {
			final String name = BwapiUtility.getName(unit);
			final String text = StringEscapeUtils.unescapeJava(parameters.get(1).toProlog());
			final IDraw draw = new CustomDrawUnit(this.game, unit, text);
			this.game.addDraw(name, draw);
			if (!text.isEmpty()) {
				this.game.toggleDraw(name);
			}
		}
	}

	@Override
	public String toString() {
		return "debugdraw(TargetId,Text)";
	}
}
