package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Unit Loaded percept.
 *
 */
public class UnitLoadedPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * The UnitLoadedPercept constructor,
	 *
	 * @param id
	 *            The id of the (loaded) unit.
	 */
	public UnitLoadedPercept(int id) {
		super(Percepts.UNITLOADED, new Numeral(id));
	}
}
