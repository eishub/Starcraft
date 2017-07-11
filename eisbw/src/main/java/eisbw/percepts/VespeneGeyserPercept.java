package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Vespene Geyser percept.
 *
 */
public class VespeneGeyserPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * The VespeneGeyserPercept constructor.
	 *
	 * @param id
	 *            The id of the Vespene Geyser.
	 * @param resources
	 *            The amount of resources left in the Vespene Geyser.
	 * @param xpos
	 *            The x-coordinate of the Vespene Geyser.
	 * @param ypos
	 *            The y-coordinate of the Vespene Geyser
	 * @param region
	 *            The region of the Vespene Geyser.
	 */
	public VespeneGeyserPercept(int id, int resources, int xpos, int ypos, int region) {
		super(Percepts.VESPENEGEYSER, new Numeral(id), new Numeral(resources), new Numeral(xpos - 2),
				new Numeral(ypos - 1), new Numeral(region));
	}
}
