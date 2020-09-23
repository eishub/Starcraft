package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Nuke percept.
 */
public class NukePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The NukePercept constructor.
	 *
	 * @param xpos   The x-coordinate of the nuke drop point.
	 * @param ypos   The y-coordinate of the nuke drop point.
	 * @param region The corresponding region.
	 */
	public NukePercept(final int xpos, final int ypos, final int region) {
		super(Percepts.NUKE, new Numeral(xpos), new Numeral(ypos), new Numeral(region));
	}
}
