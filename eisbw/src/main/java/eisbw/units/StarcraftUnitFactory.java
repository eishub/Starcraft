package eisbw.units;

import java.util.ArrayList;
import java.util.List;

import eisbw.BwapiUtility;
import eisbw.percepts.perceivers.GenericUnitPerceiver;
import eisbw.percepts.perceivers.IPerceiver;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - The Starcraft Unit Factory which creates the units.
 */
public class StarcraftUnitFactory {
	private final JNIBWAPI api;

	/**
	 * The StarcraftUnitFactory constructor.
	 *
	 * @param api The BWAPI
	 */
	public StarcraftUnitFactory(final JNIBWAPI api) {
		this.api = api;
	}

	/**
	 * Creates a unit.
	 *
	 * @param unit - the unit in the game.
	 * @return - a StarCraft unit with perceivers.
	 */
	public StarcraftUnit create(final Unit unit) {
		final List<IPerceiver> perceptGenerators = new ArrayList<>(1);
		perceptGenerators.add(new GenericUnitPerceiver(this.api, unit));
		return new StarcraftUnit(perceptGenerators, BwapiUtility.getType(unit));
	}
}
