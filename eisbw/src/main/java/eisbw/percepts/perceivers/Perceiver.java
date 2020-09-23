package eisbw.percepts.perceivers;

import jnibwapi.JNIBWAPI;

/**
 * @author Danny & Harm - The abstract perceiver.
 */
public abstract class Perceiver implements IPerceiver {
	protected final JNIBWAPI api;

	/**
	 * @param api The BWAPI.
	 */
	public Perceiver(final JNIBWAPI api) {
		this.api = api;
	}
}
