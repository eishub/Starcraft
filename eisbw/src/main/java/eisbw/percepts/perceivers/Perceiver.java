package eisbw.percepts.perceivers;

/**
 * @author Danny & Harm - The abstract perceiver.
 *
 */
public abstract class Perceiver implements IPerceiver {
	protected final bwapi.Game api;

	/**
	 * @param api
	 *            The BWAPI.
	 */
	public Perceiver(bwapi.Game api) {
		this.api = api;
	}
}
