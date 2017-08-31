package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Wesley - The Map name percept.
 *
 */
public class MapNamePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The MapNamePercept constructor.
	 *
	 * @param width
	 *            The width of the map
	 * @param height
	 *            The height of the map
	 */
	public MapNamePercept(String name) {
		super(Percepts.MAPNAME, new Identifier(name));
	}
}
