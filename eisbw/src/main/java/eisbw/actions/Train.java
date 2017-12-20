package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Trains a specified unit from a production facility.
 *
 */
public class Train extends StarcraftAction {
	/**
	 * The Train constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Train(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier
				&& getUnitType(((Identifier) parameters.get(0)).getValue()) != null;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.canProduce() || type == UnitType.Terran_Nuclear_Silo;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String tobuild = ((Identifier) parameters.get(0)).getValue();
		UnitType unitType = getUnitType(tobuild);

		// TODO: *sigh* need to enumerate through all unit types here
		// to call specific functions on specific buildings...
		// OR: get access to the field for the protected Trainer class in Building
	}

	@Override
	public String toString() {
		return "train(Type)";
	}
}
