package eisbw.percepts;

import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Resources percept.
 */
public class ResourcesPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The ResourcesPercept constructor.
	 *
	 * @param minerals The amount of collected minerals.
	 * @param gas      The amount of collected gas.
	 * @param csupply  The current amount of supply.
	 * @param tsupply  The total amount of supply.
	 */
	public ResourcesPercept(final int minerals, final int gas, final int csupply, final int tsupply) {
		super(Percepts.RESOURCES, new Numeral(minerals), new Numeral(gas), new Numeral(csupply), new Numeral(tsupply));
	}
}
