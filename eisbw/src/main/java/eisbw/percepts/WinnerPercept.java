package eisbw.percepts;

import eis.iilang.TruthValue;

/**
 * @author Danny & Harm - The Winner percept.
 */
public class WinnerPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The WinnerPercept constructor.
	 *
	 * @param boolean true if won, false if lost.
	 */
	public WinnerPercept(final boolean winner) {
		super(Percepts.WINNER, new TruthValue(winner));
	}
}
