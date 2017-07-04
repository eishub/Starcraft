package eisbw.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActionProviderTest {

  private ActionProvider actionProvider;
  
  @Test
  public void test() {
    actionProvider = new ActionProvider();
    actionProvider.loadActions(null,null);
    assertEquals(new Attack(null).toString(), actionProvider.getAction("attack/1").toString());
  }

}
