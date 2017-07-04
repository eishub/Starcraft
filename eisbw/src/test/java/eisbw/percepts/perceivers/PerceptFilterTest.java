package eisbw.percepts.perceivers;

import org.junit.Test;

import eis.eis2java.translation.Filter.Type;

public class PerceptFilterTest {
	private PerceptFilter filter;

	@Test
	public void test() {
		this.filter = new PerceptFilter("self", Type.ONCE);
		this.filter.equals(null);
		this.filter.equals(new Integer(10));
	}

}
