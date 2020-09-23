package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - Ability which can be used on a specified location.
 */
public class UseOnPosition extends StarcraftAction {
	/**
	 * The UseOnPosition constructor.
	 *
	 * @param api The BWAPI.
	 */
	public UseOnPosition(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.size() == 3 && parameters.get(0) instanceof Identifier
				&& getTechType(((Identifier) parameters.get(0)).getValue()) != null
				&& parameters.get(1) instanceof Numeral && parameters.get(2) instanceof Numeral;
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		return techType.isTargetsPosition();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final TechType techType = getTechType(((Identifier) parameters.get(0)).getValue());
		final int xpos = ((Numeral) parameters.get(1)).getValue().intValue();
		final int ypos = ((Numeral) parameters.get(2)).getValue().intValue();

		unit.useTech(techType, new Position(xpos, ypos, Position.PosType.BUILD));
	}

	@Override
	public String toString() {
		return "ability(Type,X,Y)";
	}
}
