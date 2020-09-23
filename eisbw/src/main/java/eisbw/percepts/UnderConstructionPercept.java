package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Unit percept which gives information about the
 *         other units.
 */
public class UnderConstructionPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor NewUnitPercept.
	 *
	 *
	 * @param id        The ID of the unit
	 * @param builderId The ID of the unit that is constructing/training/whatever
	 *                  this unit (or -1 if not applicable)
	 * @param vitality  The combined health and shield of the unit
	 * @param x         The (initial) X coordinate of the location of the unit
	 * @param y         The (initial) Y coordinate of the location of the unit
	 * @param region    The region of the location of the unit
	 */
	public UnderConstructionPercept(final int id, final int builderId, final int vitality, final int x, final int y,
			final int region) {
		super(Percepts.UNDERCONSTRUCTION, new Numeral(id), new Numeral(builderId), new Numeral(vitality),
				new Numeral(x), new Numeral(y), new Numeral(region));
	}
}
