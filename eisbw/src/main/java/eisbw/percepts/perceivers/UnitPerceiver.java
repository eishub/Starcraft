package eisbw.percepts.perceivers;

import bwapi.Unit;

/**
 * @author Danny & Harm - Abstract class for Unit Perceivers.
 *
 */
public abstract class UnitPerceiver extends Perceiver {
	protected final Unit unit;

	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The perceiving unit.
	 */
	public UnitPerceiver(bwapi.Game api, Unit unit) {
		super(api);
		this.unit = unit;
	}
}
