package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Nuke percept.
 *
 */
public class NukePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The NukePercept constructor.
	 *
	 * @param xpos
	 *            The x-coordinate of the nuke drop point.
	 * @param ypos
	 *            The y-coordinate of the nuke drop point.
	 */
	public NukePercept(int xpos, int ypos) {
		super(Percepts.NUKE, new Numeral(xpos), new Numeral(ypos));
	}
}
