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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class UseTest {
	private Use action;
	private LinkedList<Parameter> params;
	private String techType;

	@Mock
	private bwapi.Game bwapi;
	@Mock
	private Action act;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private TechType tech;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		this.action = new Use(this.bwapi);

		this.techType = "Stim Packs";

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Stim Packs"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
	}

	@Test
	public void isValid_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getTechType(this.techType)).thenReturn(this.tech);
		this.params.removeLast();
		assertTrue(spyAction.isValid(this.act));
		this.params.add(new Numeral(2));

		assertFalse(this.action.isValid(this.act));
		this.params.remove(1);
		assertFalse(this.action.isValid(this.act));
		this.params.set(0, new Numeral(1));
		assertFalse(this.action.isValid(this.act));
		this.params.set(0, new Identifier("Hero Mojo"));
		assertFalse(this.action.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getTechType(this.techType)).thenReturn(this.tech);

		when(this.unit.isLoaded()).thenReturn(false);
		when(this.tech.targetsPosition()).thenReturn(false);
		when(this.tech.targetsUnit()).thenReturn(false);

		assertEquals(spyAction.canExecute(this.unit, this.act), true);

		when(this.tech.targetsPosition()).thenReturn(true);
		when(this.tech.targetsUnit()).thenReturn(false);

		assertEquals(spyAction.canExecute(this.unit, this.act), false);

		when(this.tech.targetsPosition()).thenReturn(true);
		when(this.tech.targetsUnit()).thenReturn(true);

		assertEquals(spyAction.canExecute(this.unit, this.act), false);

		when(this.tech.targetsPosition()).thenReturn(false);
		when(this.tech.targetsUnit()).thenReturn(true);

		assertEquals(spyAction.canExecute(this.unit, this.act), false);

		when(this.unit.isLoaded()).thenReturn(true);
		assertEquals(spyAction.canExecute(this.unit, this.act), false);
	}

	@Test
	public void execute_test() {
		this.action.execute(this.unit, this.act);
		verify(this.unit).useTech(null);
	}

	@Test
	public void toString_test() {
		assertEquals("ability(Type)", this.action.toString());
	}
}
