package eisbw.percepts.perceivers;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.unit.PlayerUnit;

import bwta.BWTA;

/**
 * @author Danny & Harm - Abstract class for Unit Perceivers.
 *
 */
public abstract class UnitPerceiver extends Perceiver {
	protected final PlayerUnit unit;

	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The perceiving unit.
	 */
	public UnitPerceiver(BW bwapi, BWTA bwta, PlayerUnit unit) {
		super(bwapi, bwta);
		this.unit = unit;
	}
}
