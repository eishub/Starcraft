package eisbw.percepts.perceivers;

import org.openbw.bwapi4j.BW;

import bwta.BWTA;

/**
 * @author Danny & Harm - The abstract perceiver.
 *
 */
public abstract class Perceiver implements IPerceiver {
	protected final BW bwapi;
	protected final BWTA bwta;

	/**
	 * @param api
	 *            The BWAPI.
	 */
	public Perceiver(BW bwapi, BWTA bwta) {
		this.bwapi = bwapi;
		this.bwta = bwta;
	}
}
