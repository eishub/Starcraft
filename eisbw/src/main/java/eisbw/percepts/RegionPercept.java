package eisbw.percepts;

import java.util.List;

import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

/**
 * @author Danny & Harm - The Chokepoint/4 percept.
 */
public class RegionPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id        The identifier of the region.
	 * @param xpos      The x coordinate of the center of the region.
	 * @param ypos      The y coordinate of the center of the region.
	 * @param height    The height of the region.
	 * @param connected The identifiers of all regions connected to this region.
	 */
	public RegionPercept(final int id, final int xpos, final int ypos, final int height,
			final List<Parameter> connected) {
		super(Percepts.REGION, new Numeral(id), new Numeral(xpos), new Numeral(ypos), new Numeral(height),
				new ParameterList(connected));
	}
}
