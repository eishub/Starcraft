package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;
import eis.iilang.TruthValue;

/**
 * @author Danny & Harm - The Base percept.
 *
 */
public class BasePercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param isStart
	 *            Indicates whether the base location is a starting location or not.
	 * @param minerals
	 *            The amount of minerals available near the base (at the start).
	 * @param gas
	 *            The amount of gas available near the base (at the start).
	 * @param xpos
	 *            The x coordinate of the base location.
	 * @param ypos
	 *            The y coordinate of the base location.
	 * @param region
	 *            The region of the base location.
	 */
	public BasePercept(boolean isStart, int minerals, int gas, int xpos, int ypos, int region) {
		super(Percepts.BASE, new TruthValue(isStart), new Numeral(minerals), new Numeral(gas), new Numeral(xpos),
				new Numeral(ypos), new Numeral(region));
	}
}
