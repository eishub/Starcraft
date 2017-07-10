package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Makes the unit follow an other specified unit.
 *
 */
public class Follow extends StarcraftMovableAction {
	/**
	 * The Follow constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Follow(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
		Unit target = this.api.getUnit(targetId);

		unit.follow(target, false);
	}

	@Override
	public String toString() {
		return "follow(TargetID)";
	}
}