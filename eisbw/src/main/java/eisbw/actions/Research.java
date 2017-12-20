package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

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
	public Research(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		boolean valid = parameters.size() == 1 && parameters.get(0) instanceof Identifier;
		if (valid) {
			TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
			UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());
			return techType != null || upgradeType != null;
		} else {
			return false;
		}
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isBuilding();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		UpgradeType upgradeType = getUpgradeType(((Identifier) parameters.get(0)).getValue());

		// TODO: *sigh* need to enumerate through all tech and upgrade types here
		// to call specific functions on specific buildings...
		// OR: get access to the field for the protected Trainer class in Building
	}

	@Override
	public String toString() {
		return "research(Type)";
	}
}
