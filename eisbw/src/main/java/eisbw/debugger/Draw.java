package eisbw.debugger;

/**
 * @author Danny & Harm.
 */
public enum Draw {
	MAP("map"), UNITS("units");

	private final String parameter;

	private Draw(String name) {
		this.parameter = name;
	}

	public String getName() {
		return this.parameter;
	}
}
