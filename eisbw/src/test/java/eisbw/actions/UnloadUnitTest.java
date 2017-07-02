package eisbw.actions;

import static org.junit.Assert.assertEquals;
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
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class UnloadUnitTest {
	private UnloadUnit action;
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
		this.action = new UnloadUnit(this.bwapi);

		this.params = new LinkedList<>();
		this.params.add(new Numeral(1));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
	}

	@Test
	public void isValid_test() {
		assertTrue(this.action.isValid(this.act));
		this.params.set(0, new Identifier("Not Working"));
		assertFalse(this.action.isValid(this.act));
		this.params.set(0, new Numeral(1));
		this.params.add(new Numeral(10));
		assertFalse(this.action.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		when(this.unitType.spaceProvided()).thenReturn(0);
		assertFalse(this.action.canExecute(this.unit, this.act));
		when(this.unitType.spaceProvided()).thenReturn(1);
		assertTrue(this.action.canExecute(this.unit, this.act));
	}

	@Test
	public void execute_test() {
		when(this.bwapi.getUnit(1)).thenReturn(this.unit);
		this.action.execute(this.unit, this.act);
		verify(this.unit).unload(this.unit);
	}

	@Test
	public void toString_test() {
		assertEquals("unload(targetId)", this.action.toString());
	}
}
