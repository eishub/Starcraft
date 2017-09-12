package eisbw.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class LiftTest {
	private Lift action;
	private LinkedList<Parameter> params;

	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private Action act;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		this.action = new Lift(this.bwapi);

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Working"));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		assertFalse(this.action.isValid(this.act));
		this.params.remove(0);
		assertTrue(this.action.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		when(this.unitType.isBuilding()).thenReturn(false);
		when(this.unitType.getRaceID()).thenReturn(2);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.getRaceID()).thenReturn(1);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isFlyingBuilding()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.action.execute(this.unit, this.act);
		verify(this.unit).lift();
	}
}
