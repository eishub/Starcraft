package eisbw;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eisbw.debugger.DebugWindow;
import eisbw.units.StarcraftUnitFactory;
import eisbw.units.Units;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BwapiListenerTest {

	private BwapiListener listener;

	private Map<String, Unit> unitMap;
	private Map<Integer, String> unitNames;
	private List<Unit> list;

	@Mock
	private Game game;
	@Mock
	private Units units;
	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private DebugWindow debugwindow;
	@Mock
	private Player self;

	/**
	 * Init mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		when(game.getUnits()).thenReturn(units);
		unitMap = new HashMap<>();
		unitMap.put("unit", unit);
		unitNames = new HashMap<>();
		unitNames.put(0, "unit");
		when(unit.getType()).thenReturn(unitType);
		when(unitType.getName()).thenReturn("Terran Siege Tank Tank Mode");
		when(units.getUnits()).thenReturn(unitMap);
		list = new LinkedList<>();
		list.add(unit);
		when(bwapi.getMyUnits()).thenReturn(list);
		when(bwapi.getUnit(0)).thenReturn(unit);
		listener = new BwapiListener(game, "", false, false, false, false, 200);
		listener.bwapi = bwapi;
	}

	@Test
	public void getAction_test() {
		assertNotNull("getAction(Action) returned null", listener.getAction(new Action("lift")));
	}

	@Test
	public void isSupportedByEntity_test() {
		assertTrue(listener.isSupportedByEntity(new Action("stop"), "unit"));
		eis.iilang.Parameter[] list = new eis.iilang.Parameter[1];
		list[0] = new Identifier("fail");
		assertFalse(listener.isSupportedByEntity(new Action("stop", list), "unit"));
		assertFalse(listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
		list[0] = new Numeral(1);
		assertFalse(listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
		when(unitType.isBuilding()).thenReturn(true);
		assertTrue(listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
	}

	@Test
	public void unitComplete_test() {
		when(units.getUnitNames()).thenReturn(new HashMap<Integer, String>());
		listener.unitComplete(0);
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
		when(units.getUnitNames()).thenReturn(unitNames);
		listener.unitComplete(0);
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
		when(bwapi.getMyUnits()).thenReturn(new LinkedList<Unit>());
		listener.unitComplete(0);
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
		when(units.getUnitNames()).thenReturn(new HashMap<Integer, String>());
		listener.unitComplete(0);
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitMorph_test() {
		when(bwapi.getSelf()).thenReturn(self);
		when(self.getRace()).thenReturn(RaceTypes.Zerg);

		listener.unitMorph(0);
		verify(units, times(0)).getUnits();
		when(units.getUnitNames()).thenReturn(unitNames);
		when(units.deleteUnit("unit",0)).thenReturn(unit);
		listener.unitMorph(0);
		// verify(units, times(1)).getUnits();
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
		when(bwapi.getMyUnits()).thenReturn(new LinkedList<Unit>());
		listener.unitMorph(0);
		verify(units, times(1)).addUnit(eq(unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitDestroy_test() {
		listener.unitDestroy(0);
		verify(units, times(0)).deleteUnit(any(String.class), any(Integer.class));
		when(units.getUnitNames()).thenReturn(unitNames);
		when(units.deleteUnit("unit",0)).thenReturn(unit);
		listener.unitDestroy(0);
		verify(units, times(1)).deleteUnit(any(String.class), any(Integer.class));
	}

	@Test
	public void matchStart_test() {
		listener.matchStart();
		verify(bwapi, times(0)).setGameSpeed(30);
		verify(bwapi, times(1)).setGameSpeed(5);
		listener.matchEnd(true);
		verify(game, times(1)).clean();
		listener.matchStart();
		listener.debugwindow = debugwindow;
		listener.matchEnd(true);
		verify(game, times(2)).clean();
	}

	@Test
	public void matchFrame_test() throws ActException {
		listener.pendingActions.put(new Unit(1, null), new Action("stub"));
		listener.pendingActions.put(new Unit(2, null), new Action("stub"));
		listener.pendingActions.put(new Unit(3, null), new Action("stub"));
		listener.pendingActions.put(new Unit(4, null), new Action("stub"));
		listener.matchFrame();
		verify(game, times(0)).updateConstructionSites(bwapi);
		listener.count = 49;
		listener.matchFrame();
		assertTrue(listener.count == 50);
		verify(game, times(1)).updateConstructionSites(bwapi);
		listener.performEntityAction("unit", new Action("stop"));
		eis.iilang.Parameter[] list = new eis.iilang.Parameter[1];
		list[0] = new Identifier("fail");
		listener.performEntityAction("unit", new Action("setRallyPoint", list));
		when(unit.isBeingConstructed()).thenReturn(true);
		listener.performEntityAction("unit", new Action("stop"));
		assertTrue(listener.pendingActions.size() == 1);
		listener.debugwindow = debugwindow;
		listener.matchFrame();
		assertTrue(listener.pendingActions.size() == 0);
		verify(debugwindow, times(1)).debug(bwapi);
	}

}
