package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Vespene Geyser percept.
 */
public class VespeneGeyserPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The VespeneGeyserPercept constructor.
	 *
	 * @param id        The id of the Vespene Geyser.
	 * @param resources The amount of resources left in the Vespene Geyser.
	 * @param xpos      The x-coordinate of the Vespene Geyser.
	 * @param ypos      The y-coordinate of the Vespene Geyser
	 * @param region    The region of the Vespene Geyser.
	 */
	public VespeneGeyserPercept(final int id, final int resources, final int xpos, final int ypos, final int region) {
		super(Percepts.VESPENEGEYSER, new Numeral(id), new Numeral(resources), new Numeral(xpos - 2),
				new Numeral(ypos - 1), new Numeral(region));
	}
}
