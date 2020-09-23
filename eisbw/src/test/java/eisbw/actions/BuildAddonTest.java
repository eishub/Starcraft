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
import jnibwapi.types.UnitType;

public class BuildAddonTest {
	private BuildAddon action;
	private List<Parameter> params;
	private String unitType2;

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
		this.action = new BuildAddon(this.bwapi);

		this.unitType2 = "Terran SCV";

		this.params = new ArrayList<>(2);
		this.params.add(new Identifier("Terran SCV"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		final StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getUnitType(this.unitType2)).thenReturn(this.type);
		when(this.type.isAddon()).thenReturn(true);

		this.params.remove(1);
		assertTrue(spyAction.isValid(this.act));

		when(this.type.isAddon()).thenReturn(false);
		assertFalse(spyAction.isValid(this.act));

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
		when(this.unitType.isFlyingBuilding()).thenReturn(false);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.isFlyingBuilding()).thenReturn(true);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.action.execute(this.unit, this.act);
		verify(this.unit).buildAddon(null);
	}
}
