package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Self percept.
 *
 */
public class SelfPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 *            The (unique) ID of the unit.
	 * @param type
	 *            The type of the unit.
	 */
	public SelfPercept(int id, String type) {
		super(Percepts.SELF, new Numeral(id), new Identifier(type));
	}
}