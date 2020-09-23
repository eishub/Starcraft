package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UpgradeType;

/**
 * @author Danny & Harm - Researches a specified Tech Type.
 */
public class Research extends StarcraftAction {
	/**
	 * The Research constructor.
	 *
	 * @param api The BWAPI
	 */
	public Research(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final boolean valid = parameters.size() == 1 && parameters.get(0) instanceof Identifier;
		if (valid) {
			final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
			final UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());
			return techType != null || upgradeType != null;
		} else {
			return false;
		}
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return type.isBuilding();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		final UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());

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
