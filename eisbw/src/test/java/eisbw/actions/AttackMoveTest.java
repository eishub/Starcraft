package eisbw.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class AttackMoveTest {
	private AttackMove action;
	private List<Parameter> params;

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
		this.action = new AttackMove(this.bwapi);

		this.params = new ArrayList<>(2);
		this.params.add(new Numeral(1));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		assertTrue(this.action.isValid(this.act));
		this.params.set(0, new Identifier("Not Working"));
		assertFalse(this.action.isValid(this.act));
		this.params.set(0, new Numeral(1));
		this.params.set(1, new Identifier("Not Working"));
		assertFalse(this.action.isValid(this.act));
		this.params.set(1, new Numeral(2));
		this.params.add(new Numeral(10));
		assertFalse(this.action.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		when(this.unitType.isAttackCapable()).thenReturn(false);
		when(this.unitType.isCanMove()).thenReturn(false);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isCanMove()).thenReturn(true);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isAttackCapable()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isCanMove()).thenReturn(false);
		assertFalse(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		when(this.bwapi.getUnit(1)).thenReturn(this.unit);
		when(this.unitType.isAttackCapable()).thenReturn(true);
		this.action.execute(this.unit, this.act);
		verify(this.unit).attack(new Position(1, 2, Position.PosType.BUILD), false);
	}
}
