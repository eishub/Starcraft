package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Queue Size percept.
 *
 */
public class QueueSizePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The QueueSize constructor.
	 *
	 * @param queueSize
	 *            The size of the queue.
	 */
	public QueueSizePercept(int queueSize) {
		super(Percepts.QUEUESIZE, new Numeral(queueSize));
	}
}
