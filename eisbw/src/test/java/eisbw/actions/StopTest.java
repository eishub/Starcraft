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

import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class StopTest {
	private Stop action;
	private LinkedList<Parameter> params;

	@Mock
	private bwapi.Game bwapi;
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
		this.action = new Stop(this.bwapi);

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Working"));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
	}

	@Test
	public void isValid_test() {
		assertFalse(this.action.isValid(this.act));
		this.params.remove(0);
		assertTrue(this.action.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		when(this.unitType.canMove()).thenReturn(false);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.canMove()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.action.execute(this.unit, this.act);
		verify(this.unit).stop();
	}
}
