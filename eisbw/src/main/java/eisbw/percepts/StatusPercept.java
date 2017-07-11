package eisbw.percepts;

import java.util.List;

import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.Percept;

/**
 * @author Danny & Harm - The Status percept.
 *
 */
public class StatusPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param chealth
	 *            The current health of the unit.
	 * @param cshields
	 *            The current shield of the unit.
	 * @param cenergy
	 *            The current energy of the unit.
	 * @param conditions
	 *            The current conditions of the unit.
	 * @param xpos
	 *            The current x-coordinate of the unit.
	 * @param ypos
	 *            The current y-coordinate of the unit.
	 * @param ypos
	 *            The current region of the unit.
	 */
	public StatusPercept(int chealth, int cshields, int cenergy, List<Parameter> conditions, int xpos, int ypos,
			int region) {
		super(Percepts.STATUS, new Numeral(chealth), new Numeral(cshields), new Numeral(cenergy),
				new ParameterList(conditions), new Numeral(xpos), new Numeral(ypos), new Numeral(region));
	}
}
