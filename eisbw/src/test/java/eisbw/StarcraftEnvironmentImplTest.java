package eisbw;

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

public class StarcraftEnvironmentImplTest {
	private StarcraftEnvironmentImpl env;

	@Mock
	private BwapiListener bwapiListener;

	/**
	 * init environment and mocks.
	 *
	 * @throws ManagementException
	 *             - from environment
	 */
	@Before
	public void start() throws ManagementException {
		MockitoAnnotations.initMocks(this);

		this.env = new StarcraftEnvironmentImpl();
		Map<String, Parameter> parameters = new HashMap<>();
		parameters.put("debug", new Identifier("true"));
		parameters.put("own_race", new Identifier("test"));
		parameters.put("map", new Identifier("map"));
		parameters.put("starcraft_location", new Identifier("scdir"));
		this.env.init(parameters);
	}

	@Test
	public void test()
			throws PerceiveException, NoEnvironmentException, AgentException, RelationException, ActException {
		this.env.addToEnvironment("none", "type");
		this.env.registerAgent("none");
		this.env.associateEntity("none", "none");
		this.env.getAllPercepts("none", "none");
		this.env.listener = this.bwapiListener;
		when(this.bwapiListener.isSupportedByEnvironment(new Action("lift"))).thenReturn(true);
		doNothing().when(this.bwapiListener).performEntityAction(any(String.class), any(Action.class));
		assertTrue(this.env.performEntityAction("entity", new Action("lift")) == null);
		this.env.deleteFromEnvironment("none");
		when(this.bwapiListener.isSupportedByEntity(any(Action.class), any(String.class))).thenReturn(true);
		assertTrue(this.env.isSupportedByEntity(new Action("action"), "action"));
	}

}
