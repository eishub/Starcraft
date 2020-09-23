package eisbw.units;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eis.iilang.Percept;
import eisbw.percepts.perceivers.IPerceiver;
import eisbw.percepts.perceivers.PerceptFilter;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - The class which gathers all the percepts of a
 *         starcraft unit.
 */
public class StarcraftUnit {
	protected final List<IPerceiver> perceivers;
	private final boolean worker;

	/**
	 * A starcraft unit with perceivers.
	 *
	 * @param perceivers - list with perceivers to percept from.
	 * @param worker     - true iff the unit is a worker
	 */
	public StarcraftUnit(final List<IPerceiver> perceivers, final UnitType type) {
		this.perceivers = perceivers;
		this.worker = (type != null && type.isWorker());
	}

	/**
	 * Perceive this units' percepts.
	 *
	 * @return - a list of percepts.
	 */
	public Map<PerceptFilter, List<Percept>> perceive() {
		final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		for (final IPerceiver perceiver : this.perceivers) {
			perceiver.perceive(toReturn);
		}
		return toReturn;
	}

	public boolean isWorker() {
		return this.worker;
	}
}
