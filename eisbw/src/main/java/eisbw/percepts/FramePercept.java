package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Frame percept.
 */
public class FramePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The FramePercept constructor.
	 *
	 * @param frame The frame number
	 */
	public FramePercept(final int frame) {
		super(Percepts.FRAME, new Numeral(frame));
	}
}
