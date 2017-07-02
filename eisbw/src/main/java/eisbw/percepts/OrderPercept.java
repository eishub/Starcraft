package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Order Percept.
 *
 */
public class OrderPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param primary
	 *            The name of the primary order.
	 * @param target
	 *            The id of the primary order target (-1 if none)
	 * @param secondary
	 *            The name of the secondary order.
	 */
	public OrderPercept(String primary, int target, String secondary) {
		super(Percepts.ORDER, new Identifier(primary), new Numeral(target), new Identifier(secondary));
	}
}