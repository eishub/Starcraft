package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Self percept.
 */
public class SelfPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id   The (unique) ID of the unit.
	 * @param type The type of the unit.
	 */
	public SelfPercept(final int id, final String type) {
		super(Percepts.SELF, new Numeral(id), new Identifier(type));
	}
}