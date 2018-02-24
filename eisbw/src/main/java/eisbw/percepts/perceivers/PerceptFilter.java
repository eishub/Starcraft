package eisbw.percepts.perceivers;

import eis.eis2java.translation.Filter.Type;
import eisbw.percepts.Percepts;

/**
 * @author Danny & Harm - The class which classifies all the percepts.
 *
 */
public class PerceptFilter {
	private final String name;
	private final Type type;

	/**
	 * The PerceptFilter constructor.
	 *
	 * @param percept
	 *            The name of the percept (from the Percepts enum).
	 * @param type
	 *            The type of the percept.
	 */
	public PerceptFilter(Percepts percept, Type type) {
		this.name = percept.name();
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "PerceptFilter[name=" + this.name + ",type=" + this.type + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof PerceptFilter)) {
			return false;
		}
		PerceptFilter that = (PerceptFilter) other;
		return this.name.equals(that.getName());
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
