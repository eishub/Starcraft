package eisbw.percepts.perceivers;

import java.util.List;
import java.util.Map;

import eis.iilang.Percept;

/**
 * @author Danny & Harm - The interface of all perceivers.
 */
@FunctionalInterface
public interface IPerceiver {
	/**
	 * @param toReturn The percepts and reference of which kind of percepts they
	 *                 are.
	 */
	void perceive(Map<PerceptFilter, List<Percept>> toReturn);
}
