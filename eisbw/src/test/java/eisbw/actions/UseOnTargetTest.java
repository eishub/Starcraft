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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;

public class UseOnTargetTest {
	private UseOnTarget action;
	private List<Parameter> params;
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
		this.action = new UseOnTarget(this.bwapi);

		this.techType = "Stim Packs";

		this.params = new ArrayList<>(2);
		this.params.add(new Identifier("Stim Packs"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		final StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getTechType(this.techType)).thenReturn(this.tech);

		assertTrue(spyAction.isValid(this.act));
		this.params.set(1, new Identifier("Bad"));
		assertFalse(spyAction.isValid(this.act));

		this.params.add(new Identifier("string"));
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
		final StarcraftAction spyAction = Mockito.spy(this.action);

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
		verify(this.unit).useTech(null, (Unit) null);
	}
}
