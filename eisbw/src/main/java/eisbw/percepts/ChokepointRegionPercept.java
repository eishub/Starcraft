package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Chokepoint percept.
 */
public class ChokepointRegionPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param xpos1   The x coordinate of the first side of the chokepoint.
	 * @param ypos1   The y coordinate of the first side of the chokepoint.
	 * @param xpos2   The x coordinate of the second side of the chokepoint.
	 * @param ypos2   The y coordinate of the second side of the chokepoint.
	 * @param region1 The identifier of the region at one side of the chokepoint.
	 * @param region2 The identifier of the region at the other side of the
	 *                chokepoint.
	 */
	public ChokepointRegionPercept(final int xpos1, final int ypos1, final int xpos2, final int ypos2,
			final int region1, final int region2) {
		super(Percepts.CHOKEPOINT, new Numeral(xpos1), new Numeral(ypos1), new Numeral(xpos2), new Numeral(ypos2),
				new Numeral(region1), new Numeral(region2));
	}
}
