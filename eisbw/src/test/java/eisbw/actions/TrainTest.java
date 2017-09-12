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
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class TrainTest {
	private Train action;
	private LinkedList<Parameter> params;

	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private Action act;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private UnitType type;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		this.action = new Train(this.bwapi);

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Terran SCV"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getUnitType("Terran SCV")).thenReturn(this.unitType);

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
		when(this.unitType.isProduceCapable()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.action.execute(this.unit, this.act);
		verify(this.unit).train(null);
	}
}
