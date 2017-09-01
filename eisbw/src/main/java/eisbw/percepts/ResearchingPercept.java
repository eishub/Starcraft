package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Danny & Harm - The Researching percept.
 *
 */
public class ResearchingPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 *            The name of the tech or upgrade type.
	 */
	public ResearchingPercept(String name) {
		super(Percepts.RESEARCHING, new Identifier(name));
	}
}
