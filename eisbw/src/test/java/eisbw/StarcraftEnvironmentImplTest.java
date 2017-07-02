package eisbw;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.actions.Lift;

public class StarcraftEnvironmentImplTest {

  private StarcraftEnvironmentImpl env;
  @Mock
  private BwapiListener bwapiListener;

  /**
   * init environment and mocks.
   * @throws ManagementException - from environment
   */
  @Before
  public void start() throws ManagementException {
    MockitoAnnotations.initMocks(this);

    env = new StarcraftEnvironmentImpl();
    Map<String, Parameter> parameters = new HashMap<>();
    parameters.put("debug", new Identifier("true"));
    parameters.put("own_race", new Identifier("test"));
    parameters.put("map", new Identifier("map"));
    parameters.put("starcraft_location", new Identifier("scdir"));
    env.init(parameters);
  }

  @Test
  public void test() throws PerceiveException, NoEnvironmentException, AgentException,
      RelationException, ActException {
    env.addToEnvironment("none", "type");
    env.registerAgent("none");
    env.associateEntity("none", "none");
    env.getAllPercepts("none", "none");
    env.listener = bwapiListener;
    when(bwapiListener.getAction(any(Action.class))).thenReturn(new Lift(null));
    assertTrue(env.isSupportedByType(new Action("lift"), null));
    when(bwapiListener.getAction(any(Action.class))).thenReturn(null);
    assertFalse(env.isSupportedByType(new Action("fake"), null));
    doNothing().when(bwapiListener).performEntityAction(any(String.class), any(Action.class));
    assertTrue(env.performEntityAction("entity", new Action("lift")) == null);
    env.deleteFromEnvironment("none");
    when(bwapiListener.isSupportedByEntity(any(Action.class), any(String.class))).thenReturn(true);
    assertTrue(env.isSupportedByEntity(new Action("action"), "action"));
  }

}
