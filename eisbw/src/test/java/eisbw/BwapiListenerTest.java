package eisbw;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eisbw.debugger.DebugWindow;
import eisbw.units.StarcraftUnitFactory;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class BwapiListenerTest {

	private BwapiListener listener;
	private List<Unit> list;

	@Mock
	private Game game;
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

		when(this.unit.isExists()).thenReturn(true);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.getName()).thenReturn("Terran Siege Tank Tank Mode");
		when(this.unitType.isCanMove()).thenReturn(true);
		BwapiUtility.clearCache(this.unit);

		when(this.game.getUnitName(0)).thenReturn("unit");
		when(this.game.getUnit("unit")).thenReturn(this.unit);
		this.list = new LinkedList<>();
		this.list.add(this.unit);
		when(this.bwapi.getMyUnits()).thenReturn(this.list);
		when(this.bwapi.getUnit(0)).thenReturn(this.unit);

		this.listener = new BwapiListener(this.game, "", false, false, false, false, 200);
		this.listener.bwapi = this.bwapi;
	}

	@Test
	public void unitComplete_test() {
		when(this.game.getUnitName(0)).thenReturn(null);
		this.listener.unitComplete(0);
		verify(this.game, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.game.getUnitName(0)).thenReturn("unit");
		this.listener.unitComplete(0);
		verify(this.game, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.bwapi.getMyUnits()).thenReturn(new LinkedList<Unit>());
		this.listener.unitComplete(0);
		verify(this.game, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitMorph_test() {
		when(this.self.getRace()).thenReturn(RaceTypes.Zerg);
		when(this.bwapi.getSelf()).thenReturn(this.self);
		when(this.unit.getPlayer()).thenReturn(this.self);
		when(this.game.getUnitName(0)).thenReturn(null);
		this.listener.unitMorph(0);
		when(this.game.getUnitName(0)).thenReturn("unit");
		this.listener.unitMorph(0);
		// verify(units, times(1)).getUnits();
		verify(this.game, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
		when(this.bwapi.getMyUnits()).thenReturn(new LinkedList<Unit>());
		this.listener.unitMorph(0);
		verify(this.game, times(1)).addUnit(eq(this.unit), any(StarcraftUnitFactory.class));
	}

	@Test
	public void unitDestroy_test() {
		this.listener.unitDestroy(0);
		verify(this.game, times(1)).deleteUnit(any(Unit.class));
	}

	@Test
	public void matchStart_test() {
		this.listener.matchStart();
		verify(this.bwapi, times(0)).setGameSpeed(30);
		verify(this.bwapi, times(1)).setGameSpeed(5);
		this.listener.matchEnd(true);
		verify(this.game, times(1)).clean();
		this.listener.matchStart();
		this.listener.debugwindow = this.debugwindow;
		this.listener.matchEnd(true);
		verify(this.game, times(2)).clean();
	}

	@Test
	public void matchFrame_test() throws ActException {
		this.listener.pendingActions.add(new BwapiAction(new Unit(1, null), new Action("stub")));
		this.listener.pendingActions.add(new BwapiAction(new Unit(2, null), new Action("stub")));
		this.listener.pendingActions.add(new BwapiAction(new Unit(3, null), new Action("stub")));
		this.listener.pendingActions.add(new BwapiAction(new Unit(4, null), new Action("stub")));
		this.listener.matchFrame();
		verify(this.game, times(1)).updateConstructionSites(this.bwapi);
		when(this.bwapi.getFrameCount()).thenReturn(50);
		this.listener.matchFrame();
		verify(this.game, times(2)).updateConstructionSites(this.bwapi);
		this.listener.performEntityAction("unit", new Action("stop"));
		eis.iilang.Parameter[] list = new eis.iilang.Parameter[1];
		list[0] = new Identifier("fail");
		this.listener.performEntityAction("unit", new Action("setRallyPoint", list));
		when(this.unit.isBeingConstructed()).thenReturn(true);
		this.listener.performEntityAction("unit", new Action("stop"));
		assertTrue(this.listener.pendingActions.size() == 1);
		this.listener.debugwindow = this.debugwindow;
		this.listener.matchFrame();
		assertTrue(this.listener.pendingActions.size() == 0);
		verify(this.debugwindow, times(1)).debug(this.bwapi);
	}

}
