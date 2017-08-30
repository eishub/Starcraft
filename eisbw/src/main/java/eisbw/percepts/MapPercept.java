package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Map percept.
 *
 */
public class MapPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The MapPercept constructor.
	 *
	 * @param width
	 *            The width of the map
	 * @param height
	 *            The height of the map
	 */
	public MapPercept(int width, int height) {
		super(Percepts.MAP, new Numeral(width), new Numeral(height));
	}
}
