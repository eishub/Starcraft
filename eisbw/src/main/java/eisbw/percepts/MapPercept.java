package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Map percept.
 */
public class MapPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The MapPercept constructor.
	 *
	 * @param name   The name of the map
	 * @param width  The width of the map
	 * @param height The height of the map
	 */
	public MapPercept(final String name, final int width, final int height) {
		super(Percepts.MAP, new Identifier(name), new Numeral(width), new Numeral(height));
	}
}
