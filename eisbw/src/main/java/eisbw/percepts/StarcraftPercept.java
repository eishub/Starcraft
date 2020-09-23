package eisbw.percepts;

import eis.iilang.Parameter;
import eis.iilang.Percept;

public class StarcraftPercept extends Percept {
	private static final long serialVersionUID = 1L;
	private final int hash;

	public StarcraftPercept(final Percepts percept, final Parameter... parameters) {
		super(percept.toString(), parameters);
		this.hash = super.hashCode();
	}

	@Override
	public int hashCode() {
		return this.hash;
	}
}
