package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Unit percept which gives information about the
 *         other units.
 *
 */
public class UnderConstructionPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor NewUnitPercept.
	 *
	 *
	 * @param id
	 *            The ID of the unit
	 * @param vitality
	 *            The combined health and sheald of the unit
	 * @param x
	 *            The (initial) X coordinate of the location of the unit
	 * @param y
	 *            The (initial) Y coordinate of the location of the unit
	 * @param region
	 *            The region of the location of the unit
	 */
	public UnderConstructionPercept(int id, int vitality, int x, int y, int region) {
		super(Percepts.UNDERCONSTRUCTION, new Numeral(id), new Numeral(vitality), new Numeral(x), new Numeral(y),
				new Numeral(region));
	}
}
