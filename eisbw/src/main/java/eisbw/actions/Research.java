package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UpgradeType;

/**
 * @author Danny & Harm - Researches a specified Tech Type.
 *
 */
public class Research extends StarcraftAction {
	/**
	 * The Research constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Research(JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());
		UpgradeType upgradeType = BwapiUtility.getUpgradeType(((Identifier) parameters.get(0)).getValue());
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& (techType != null || upgradeType != null);
	}

	@Override
	public boolean canExecute(Unit unit, Action action) {
		return unit.getType().isBuilding();
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());
		UpgradeType upgradeType = BwapiUtility.getUpgradeType(((Identifier) parameters.get(0)).getValue());

		if (techType == null) {
			unit.upgrade(upgradeType);
		} else {
			unit.research(techType);
		}

	}

	@Override
	public String toString() {
		return "research(Type)";
	}
}
