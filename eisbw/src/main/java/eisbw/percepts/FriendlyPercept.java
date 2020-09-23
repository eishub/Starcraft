package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Friendly percept which gives information about
 *         your own units.
 */
public class FriendlyPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor FriendlyPercept.
	 *
	 * @param id   The ID of the unit
	 * @param type The unit type
	 */
	public FriendlyPercept(final int id, final String type) {
		super(Percepts.FRIENDLY, new Numeral(id), new Identifier(type));
	}
}
