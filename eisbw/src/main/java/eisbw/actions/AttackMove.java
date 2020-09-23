package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - Makes the unit move to the specified location,
 *         attacking everything it encounters.
 */
public class AttackMove extends StarcraftMovableAction {

	/**
	 * The AttackMove constructor.
	 *
	 * @param api The BWAPI
	 */
	public AttackMove(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean canExecute(final UnitType type, final Action action) {
		return super.canExecute(type, action) && (type.isAttackCapable() || type == UnitTypes.Terran_Medic);
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		final List<Parameter> parameters = action.getParameters();
		final int xpos = ((Numeral) parameters.get(0)).getValue().intValue();
		final int ypos = ((Numeral) parameters.get(1)).getValue().intValue();

		unit.attack(new Position(xpos, ypos, Position.PosType.BUILD), false);
	}

	@Override
	public String toString() {
		return "attack(X,Y)";
	}
}
