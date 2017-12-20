package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Ability which can be used on a specified location.
 *
 */
public class UseOnPosition extends StarcraftAction {
	/**
	 * The UseOnPosition constructor.
	 *
	 * @param api
	 *            The BWAPI.
	 */
	public UseOnPosition(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 3 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null
				&& parameters.get(1) instanceof Numeral && parameters.get(2) instanceof Numeral;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return techType.targetsPosition();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		int xpos = ((Numeral) parameters.get(1)).getValue().intValue();
		int ypos = ((Numeral) parameters.get(2)).getValue().intValue();

		// TODO: *sigh* need to enumerate through all techtypes here
		// to call specific functions on specific units...
	}

	@Override
	public String toString() {
		return "ability(Type,X,Y)";
	}
}
