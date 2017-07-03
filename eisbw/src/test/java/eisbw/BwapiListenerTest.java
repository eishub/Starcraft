package eisbw;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eisbw.debugger.DebugWindow;
import eisbw.units.StarcraftUnitFactory;
import eisbw.units.Units;

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
	private bwapi.Game bwapi;
	@Mock
	private Unit unit;
	@Mock
	private Unit unit2;
	@Mock
	private Unit unit3;
	@Mock
	private Unit unit4;
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
		when(this.game.getUnits()).thenReturn(this.units);
		this.unitMap = new HashMap<>();
		this.unitMap.put("unit", this.unit);
		this.unitNames = new HashMap<>();
		this.unitNames.put(0, "unit");
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("Terran Siege Tank Tank Mode");
		when(this.units.getUnits()).thenReturn(this.unitMap);
		this.list = new LinkedList<>();
		this.list.add(this.unit);
		when(this.bwapi.self()).thenReturn(this.self);
		when(this.self.getUnits()).thenReturn(this.list);
		when(this.bwapi.getUnit(0)).thenReturn(this.unit);
		this.listener = new BwapiListener(this.game, false, false, false, false, 200);
		this.listener.api = this.bwapi;
	}

	@Test
	public void getAction_test() {
		assertNotNull("getAction(Action) returned null", this.listener.getAction(new Action("lift")));
	}

	@Test
	public void isSupportedByEntity_test() {
		assertTrue(this.listener.isSupportedByEntity(new Action("stop"), "unit"));
		eis.iilang.Parameter[] list = new eis.iilang.Parameter[1];
		list[0] = new Identifier("fail");
		assertFalse(this.listener.isSupportedByEntity(new Action("stop", list), "unit"));
		assertFalse(this.listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
		list[0] = new Numeral(1);
		assertFalse(this.listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
		when(this.unitType.isBuilding()).thenReturn(true);
		assertTrue(this.listener.isSupportedByEntity(new Action("setRallyPoint", list), "unit"));
	}

	@Test
	public void unitComplete_test() {
		when(this.units.getUnitNames()).thenReturn(new HashMap<Integer, String>());
		this.listener.onUnitComplete(this.unit);
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.units.getUnitNames()).thenReturn(this.unitNames);
		this.listener.onUnitComplete(this.unit);
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.self.getUnits()).thenReturn(new ArrayList<Unit>(0));
		this.listener.onUnitComplete(this.unit);
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.units.getUnitNames()).thenReturn(new HashMap<Integer, String>());
		this.listener.onUnitComplete(this.unit);
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitMorph_test() {
		when(this.self.getRace()).thenReturn(Race.Zerg);

		this.listener.onUnitMorph(this.unit);
		verify(this.units, times(0)).getUnits();
		when(this.units.getUnitNames()).thenReturn(this.unitNames);
		when(this.units.deleteUnit("unit", 0)).thenReturn(this.unit);
		this.listener.onUnitMorph(this.unit);
		// verify(units, times(1)).getUnits();
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.self.getUnits()).thenReturn(new ArrayList<Unit>(0));
		this.listener.onUnitMorph(this.unit);
		verify(this.units, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitDestroy_test() {
		this.listener.onUnitDestroy(this.unit);
		verify(this.units, times(0)).deleteUnit(any(String.class), any(Integer.class));
		when(this.units.getUnitNames()).thenReturn(this.unitNames);
		when(this.units.deleteUnit("unit", 0)).thenReturn(this.unit);
		this.listener.onUnitDestroy(this.unit);
		verify(this.units, times(1)).deleteUnit(any(String.class), any(Integer.class));
	}

	@Test
	public void matchStart_test() {
		this.listener.onStart();
		verify(this.bwapi, times(0)).setLocalSpeed(30);
		verify(this.bwapi, times(1)).setLocalSpeed(5);
		this.listener.onEnd(true);
		verify(this.game, times(1)).clean();
		this.listener.onStart();
		this.listener.debugwindow = this.debugwindow;
		this.listener.onEnd(true);
		verify(this.game, times(2)).clean();
	}

	@Test
	public void matchFrame_test() throws Exception {
		this.listener.pendingActions.put(this.unit, new Action("stub"));
		this.listener.pendingActions.put(this.unit2, new Action("stub"));
		this.listener.pendingActions.put(this.unit3, new Action("stub"));
		this.listener.pendingActions.put(this.unit4, new Action("stub"));
		this.listener.onFrame();
		verify(this.game, times(0)).updateConstructionSites(this.bwapi);
		this.listener.count = 49;
		this.listener.onFrame();
		assertTrue(this.listener.count == 50);
		verify(this.game, times(1)).updateConstructionSites(this.bwapi);
		this.listener.performEntityAction("unit", new Action("stop"));
		eis.iilang.Parameter[] list = new eis.iilang.Parameter[1];
		list[0] = new Identifier("fail");
		this.listener.performEntityAction("unit", new Action("setRallyPoint", list));
		when(this.unit.isBeingConstructed()).thenReturn(true);
		this.listener.performEntityAction("unit", new Action("stop"));
		assertTrue(this.listener.pendingActions.size() == 1);
		this.listener.debugwindow = this.debugwindow;
		this.listener.onFrame();
		assertTrue(this.listener.pendingActions.size() == 0);
		verify(this.debugwindow, times(1)).debug(this.bwapi);
	}
}
