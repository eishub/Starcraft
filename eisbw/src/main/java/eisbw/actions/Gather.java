package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit gather from a specified resource.
 *
 */
public class Gather extends StarcraftAction {
	/**
	 * The Gather constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Gather(bwapi.Game api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isWorker();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.gather(target);
	}

	@Override
	public String toString() {
		return "gather(TargetID)";
	}
}
