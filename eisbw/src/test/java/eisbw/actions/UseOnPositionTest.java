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
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;

public class UseOnPositionTest {
	private UseOnPosition action;
	private LinkedList<Parameter> params;
	private String techType;

	@Mock
	private JNIBWAPI bwapi;
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
		this.action = new UseOnPosition(this.bwapi);

		this.techType = "Stim Packs";

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Stim Packs"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getTechType(this.techType)).thenReturn(this.tech);

		this.params.add(new Numeral(2));
		assertTrue(spyAction.isValid(this.act));

		this.params.set(2, new Identifier("Bad"));
		assertFalse(spyAction.isValid(this.act));

		this.params.set(1, new Identifier("Worse"));
		assertFalse(spyAction.isValid(this.act));

		this.params.remove(0);
		assertFalse(spyAction.isValid(this.act));

		this.params.add(0, new Identifier("SomeThing"));
		assertFalse(spyAction.isValid(this.act));

		this.params.set(0, new Numeral(1));
		assertFalse(spyAction.isValid(this.act));
	}

	@Test
	public void canExecute_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getTechType(this.techType)).thenReturn(this.tech);

		this.params.add(new Numeral(2));
		assertFalse(spyAction.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.params.set(0, new Identifier("null"));
		this.params.set(1, new Numeral(1));
		this.params.add(new Numeral(2));
		this.action.execute(this.unit, this.act);
		verify(this.unit).useTech(null, new Position(1, 2, Position.PosType.BUILD));
	}
}
