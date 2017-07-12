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

import bwapi.Player;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class MorphTest {
	private Morph action;
	private LinkedList<Parameter> params;

	@Mock
	private bwapi.Game bwapi;
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

		this.params = new LinkedList<>();
		this.params.add(new Identifier("Zerg Hydralisk"));
		this.params.add(new Numeral(2));

		when(this.act.getParameters()).thenReturn(this.params);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.bwapi.self()).thenReturn(this.player);
	}

	// @Test FIXME (native call)
	public void isValid_test() {
		StarcraftAction spyAction = Mockito.spy(this.action);

		when(spyAction.getUnitType("Zerg Hydralisk")).thenReturn(this.unitType);

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
		when(this.unitType.getRace()).thenReturn(Race.Terran);
		assertFalse(this.action.canExecute(this.unitType, this.act));
		when(this.unitType.getRace()).thenReturn(Race.Zerg);
		assertTrue(this.action.canExecute(this.unitType, this.act));
	}

	// @Test FIXME (native call)
	public void execute_test() {
		this.params.set(0, new Identifier("null"));
		this.action.execute(this.unit, this.act);
		verify(this.unit).morph(null);
	}
}
