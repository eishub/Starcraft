package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Mineral Field percept.
 */
public class MineralFieldPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id        The id of the mineralfield.
	 * @param resources The amount of resources left.
	 * @param xpos      The x-coordinate of the position.
	 * @param ypos      The y-coordinate of the position.
	 * @param region    The region.
	 */
	public MineralFieldPercept(final int id, final int resources, final int xpos, final int ypos, final int region) {
		super(Percepts.MINERALFIELD, new Numeral(id), new Numeral(resources), new Numeral(xpos), new Numeral(ypos),
				new Numeral(region));
	}
}
