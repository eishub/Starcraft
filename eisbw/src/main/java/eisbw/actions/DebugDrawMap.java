package eisbw.actions;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawMap;
import eisbw.debugger.draw.IDraw;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Enable or disable drawing text on a certain
 *         (tile)position on the map.
 *
 */
@SuppressWarnings("deprecation")
public class DebugDrawMap extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugDrawMap constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public DebugDrawMap(JNIBWAPI api, Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 3 && parameters.get(0) instanceof Numeral && parameters.get(1) instanceof Numeral);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Numeral x = (Numeral) parameters.get(0);
		Numeral y = (Numeral) parameters.get(1);
		Position pos = new Position((int) x.getValue(), (int) y.getValue(), PosType.BUILD);
		String text = StringEscapeUtils.unescapeJava(parameters.get(2).toProlog());

		IDraw draw = new CustomDrawMap(this.game, pos, text);
		this.game.addDraw(pos.toString(), draw);
		if (!text.isEmpty()) {
			this.game.toggleDraw(pos.toString());
		}
	}

	@Override
	public String toString() {
		return "debugdraw(X,Y,Text)";
	}
}
