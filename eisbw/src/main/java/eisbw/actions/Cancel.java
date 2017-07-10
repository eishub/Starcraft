package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UpgradeType.UpgradeTypes;

/**
 * @author Danny & Harm - Cancels the action of the current unit.
 *
 */
public class Cancel extends StarcraftAction {
	/**
	 * The Cancel constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Cancel(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		if (unit.isTraining()) {
			unit.cancelTrain();
		} else if (unit.getTech() != null && unit.getTech().getID() != TechTypes.None.getID()) {
			unit.cancelResearch();
		} else if (unit.getUpgrade() != null && unit.getUpgrade().getID() != UpgradeTypes.None.getID()) {
			unit.cancelUpgrade();
		} else if (unit.getType().getRaceID() == RaceTypes.Terran.getID()) {
			unit.cancelAddon();
		}
	}

	@Override
	public String toString() {
		return "cancel";
	}
}
