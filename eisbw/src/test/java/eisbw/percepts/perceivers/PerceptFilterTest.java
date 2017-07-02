package eisbw.percepts.perceivers;

import org.junit.Test;

import eis.eis2java.translation.Filter.Type;

public class PerceptFilterTest {
  private PerceptFilter filter;

  @Test
  public void test() {
    filter = new PerceptFilter("self", Type.ONCE);
    filter.equals(null);
    filter.equals(new Integer(10));
  }

}
