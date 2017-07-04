package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiListener;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 *
 */
public class DebugDraw extends StarcraftAction {
	private final BwapiListener listener;

	/**
	 * The DebugText constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public DebugDraw(JNIBWAPI api, BwapiListener listener) {
		super(api);
		this.listener = listener;
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String text = ((Identifier) parameters.get(0)).getValue();
		String id = (unit == null) ? "0" : Integer.toString(unit.getID());

		IDraw draw = new CustomDrawUnit(this.listener.getGame(), unit, text);
		this.listener.addDraw(id, draw);
		if (!text.isEmpty()) {
			this.listener.toggleDraw(id);
		}
	}

	@Override
	public String toString() {
		return "debugdraw(Text)";
	}

}
