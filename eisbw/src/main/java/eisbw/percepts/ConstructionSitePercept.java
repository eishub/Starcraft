package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.TruthValue;

/**
 * @author Danny & Harm - The ConstructionSite percept.
 */
public class ConstructionSitePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param xpos   The x coordinate of the construction site.
	 * @param ypos   The y coordinate of the construction site.
	 * @param region The region of the construction site.
	 */
	public ConstructionSitePercept(final int xpos, final int ypos, final int region) {
		super(Percepts.CONSTRUCTIONSITE, new Numeral(xpos), new Numeral(ypos), new Numeral(region));
	}

	/**
	 * @param xpos      The x coordinate of the construction site.
	 * @param ypos      The y coordinate of the construction site.
	 * @param region    The region of the construction site.
	 * @param isInRange Indicates whether the construction site is in range of a
	 *                  pylon.
	 */
	public ConstructionSitePercept(final int xpos, final int ypos, final int region, final boolean isInRange) {
		super(Percepts.CONSTRUCTIONSITE, new Numeral(xpos), new Numeral(ypos), new Numeral(region),
				new TruthValue(isInRange));
	}
}
