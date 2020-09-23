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
import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class MorphTest {
	private Morph action;
	private List<Parameter> params;

	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private Action act;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private Player player;
	@Mock
	private UnitType type;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		this.action = new Morph(this.bwapi);

		this.params = new ArrayList<>(2);
		this.params.add(new Identifier("Zerg Hydralisk"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.bwapi.getSelf()).thenReturn(this.player);
		BwapiUtility.clearCache(0);
	}

	@Test
	public void isValid_test() {
		final StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getUnitType("Zerg Hydralisk")).thenReturn(this.unitType);

		this.params.remove(1);
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
		when(this.unitType.getRaceID()).thenReturn(RaceTypes.Terran.getID());
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.getRaceID()).thenReturn(RaceTypes.Zerg.getID());
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	@Test
	public void execute_test() {
		this.params.set(0, new Identifier("null"));
		this.action.execute(this.unit, this.act);
		verify(this.unit).morph(null);
	}
}
