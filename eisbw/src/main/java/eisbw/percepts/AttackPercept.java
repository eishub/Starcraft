package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import eis.iilang.TruthValue;

/**
 * @author Danny & Harm - The Attacking Percept.
 *
 */
public class AttackPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param type
	 *            The type of attack
	 * @param targetUnit
	 *            The id of the source of the attack
	 * @param targetUnit
	 *            The id of the attack target (-1 if none)
	 * @param targetX
	 *            The X coordinate of the attack target (-1 if none)
	 * @param targetY
	 *            The Y coordinate of the attack target (-1 if none)
	 * @param traveling
	 *            True iff the unit has not arrived at the target yet
	 */
	public AttackPercept(String type, int sourceUnit, int targetUnit, int targetX, int targetY, boolean traveling) {
		super(Percepts.ATTACKING, new Identifier(type), new Numeral(sourceUnit), new Numeral(targetUnit),
				new Numeral(targetX), new Numeral(targetY), new TruthValue(traveling));
	}
}