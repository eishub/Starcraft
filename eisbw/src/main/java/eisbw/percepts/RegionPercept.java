package eisbw.percepts;

import java.util.List;

import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.Percept;
import eis.iilang.TruthValue;

/**
 * @author Danny & Harm - The Chokepoint/4 percept.
 *
 */
public class RegionPercept extends Percept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 *            The identifier of the region.
	 * @param xpos
	 *            The x coordinate of the center of the region.
	 * @param ypos
	 *            The y coordinate of the center of the region.
	 * @param heighground
	 *            True if the region is on a high ground.
	 * @param connected
	 *            The identifiers of all regions connected to this region.
	 */
	public RegionPercept(int id, int xpos, int ypos, boolean highground, List<Parameter> connected) {
		super(Percepts.REGION, new Numeral(id), new Numeral(xpos), new Numeral(ypos), new TruthValue(highground),
				new ParameterList(connected));
	}
}
