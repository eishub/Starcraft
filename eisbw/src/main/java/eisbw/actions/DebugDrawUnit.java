package eisbw.actions;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 *
 */
@SuppressWarnings("deprecation")
public class DebugDrawUnit extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugDrawUnit constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public DebugDrawUnit(BW api, Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return (parameters.size() == 2 && parameters.get(0) instanceof Numeral);
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Numeral id = (Numeral) parameters.get(0);
		unit = (PlayerUnit) this.api.getUnit(id.getValue().intValue());
		if (BwapiUtility.isValid(unit)) {
			String name = BwapiUtility.getName(unit);
			String text = StringEscapeUtils.unescapeJava(parameters.get(1).toProlog());
			IDraw draw = new CustomDrawUnit(this.game, unit, text);
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
