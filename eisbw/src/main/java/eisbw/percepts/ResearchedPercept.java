package eisbw.percepts;

import java.util.List;

import eis.iilang.Parameter;
import eis.iilang.ParameterList;

/**
 * @author Danny & Harm - The Researched percept.
 *
 */
public class ResearchedPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param names
	 *            The list of available tech or upgrade types.
	 */
	public ResearchedPercept(List<Parameter> names) {
		super(Percepts.RESEARCHED, new ParameterList(names));
	}
}
