package eisbw.percepts;

import java.util.List;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

/**
 * @author Danny & Harm - The Enemy percept which gives information about the
 *         opponent's units.
 */
public class EnemyPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor EnemyPercept.
	 *
	 * @param id          The ID of the unit
	 * @param type        The unit type
	 * @param health      The amount of health of the unit
	 * @param shields     The amount of shields of the unit
	 * @param energy      The amount of energy of the unit
	 * @param conditions  The current conditions of the unit
	 * @param orientation The orientation of the unit (degrees).
	 * @param x           The X coordinate of the location of the unit
	 * @param y           The Y coordinate of the location of the unit
	 * @param region      The region the unit is in
	 * @param lastUpdated The frame in which the enemy was last seen
	 */
	public EnemyPercept(final int id, final String type, final int health, final int shields, final int energy,
			final List<Parameter> conditions, final int orientation, final int x, final int y, final int region,
			final int lastUpdated) {
		super(Percepts.ENEMY, new Numeral(id), new Identifier(type), new Numeral(health), new Numeral(shields),
				new Numeral(energy), new ParameterList(conditions), new Numeral(orientation), new Numeral(x),
				new Numeral(y), new Numeral(region), new Numeral(lastUpdated));
	}
}
