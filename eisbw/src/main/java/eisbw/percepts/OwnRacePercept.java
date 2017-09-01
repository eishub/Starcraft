package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Danny & Harm - The Own Race percept.
 *
 */
public class OwnRacePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 *            The name of the player's own race.
	 */
	public OwnRacePercept(String name) {
		super(Percepts.OWNRACE, new Identifier(name));
	}
}
