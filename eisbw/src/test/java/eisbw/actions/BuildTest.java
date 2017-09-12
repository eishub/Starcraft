package eisbw.actions;

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

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class BuildTest {
	private Build action;
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
		this.action = new Build(this.bwapi);

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Working"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getUnitType("Working")).thenReturn(this.unitType);
		when(this.unitType.isBuilding()).thenReturn(true);

		this.params.add(new Numeral(3));

		assertTrue(spyAction.isValid(this.act));
		this.params.set(0, new Numeral(9));
		assertFalse(spyAction.isValid(this.act));
		this.params.remove(0);
		assertFalse(spyAction.isValid(this.act));
		this.params.add(0, new Identifier("Working"));
		this.params.set(2, new Identifier("falser"));
		assertFalse(spyAction.isValid(this.act));
		this.params.set(1, new Identifier("false"));
		assertFalse(spyAction.isValid(this.act));
		this.params.set(1, new Numeral(2));
		assertFalse(spyAction.isValid(this.act));
		when(this.unitType.isBuilding()).thenReturn(false);
		assertFalse(spyAction.isValid(this.act));
		this.params.add(0, new Identifier("Working"));
		when(spyAction.getUnitType("Working")).thenReturn(null);
		this.unitType = null;
		assertFalse(spyAction.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		when(this.unitType.isWorker()).thenReturn(false);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isWorker()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.params.set(0, new Identifier("null"));
		this.params.set(1, new Numeral(1));
		this.params.add(new Numeral(2));
		this.action.execute(this.unit, this.act);
		verify(this.unit).build(new Position(1, 2, PosType.BUILD), null);
	}
}
