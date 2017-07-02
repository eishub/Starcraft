package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Attacking Percept.
 *
 */
public class AttackingPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param unit
	 *            The unit which is attacking.
	 * @param targetUnit
	 *            The unit which is being attacked.
	 */
	public AttackingPercept(int unit, int targetUnit) {
		super(Percepts.ATTACKING, new Numeral(unit), new Numeral(targetUnit));
	}
}