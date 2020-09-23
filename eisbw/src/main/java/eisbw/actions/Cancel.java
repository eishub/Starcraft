package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType.UpgradeTypes;

/**
 * @author Danny & Harm - Cancels the action of the current unit.
 */
public class Cancel extends StarcraftAction {
	/**
	 * The Cancel constructor.
	 *
	 * @param api The BWAPI
	 */
	public Cancel(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isBuilding() || type.isProduceCapable() || type == UnitTypes.Terran_Nuclear_Silo;
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		if (unit.isTraining()) {
			unit.cancelTrain();
		} else if (unit.getTech() != null && unit.getTech() != TechTypes.None) {
			unit.cancelResearch();
		} else if (unit.getUpgrade() != null && unit.getUpgrade() != UpgradeTypes.None) {
			unit.cancelUpgrade();
		} else if (BwapiUtility.getType(unit).getRaceID() == RaceTypes.Terran.getID()) {
			unit.cancelAddon();
		}
	}

	@Override
	public String toString() {
		return "cancel";
	}
}
