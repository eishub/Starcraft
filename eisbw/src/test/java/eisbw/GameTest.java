package eisbw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import bwapi.Unit;
import bwapi.UnitType;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.percepts.ConstructionSitePercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.perceivers.PerceptFilter;
import eisbw.units.StarcraftUnit;
import eisbw.units.Units;

public class GameTest {
	private Game game;
	private Map<PerceptFilter, List<Percept>> percepts;

	@Mock
	private StarcraftEnvironmentImpl env;
	@Mock
	private bwapi.Game bwapi;
	@Mock
	private Player self;
	@Mock
	private StarcraftUnit scUnit;
	@Mock
	private Units units;
	@Mock
	private Unit unit;
	@Mock
	private UnitType type;

	/**
	 * Init mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		this.percepts = new HashMap<>(1);
		List<Percept> perc = new ArrayList<>(1);
		perc.add(new ConstructionSitePercept(1, 2, 3));
		this.percepts.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), perc);

		when(this.type.isWorker()).thenReturn(true);
		when(this.unit.getID()).thenReturn(0);
		when(this.unit.getType()).thenReturn(this.type);
		when(this.units.getUnitName(0)).thenReturn("unit");
		when(this.units.getUnit("unit")).thenReturn(this.unit);
		when(this.units.getStarcraftUnit(this.unit)).thenReturn(this.scUnit);
		when(this.scUnit.perceive()).thenReturn(this.percepts);

		when(this.env.getAgents()).thenReturn(new LinkedList<String>());

		this.game = new Game(this.env);
		this.game.units = this.units;

		List<Unit> units = new ArrayList<>(1);
		units.add(this.unit);
		when(this.self.getUnits()).thenReturn(units);
		when(this.bwapi.self()).thenReturn(this.self);
	}

	@Test
	public void update_test() {
		this.game.update(this.bwapi);
		assertTrue(this.game.getUnits() == this.units);
		assertTrue(this.game.getPercepts("null").isEmpty());
		assertEquals(1, this.game.getPercepts("unit").size());
	}

	@Test
	public void constructionsites_test() {
		when(this.bwapi.mapWidth()).thenReturn(0);
		when(this.bwapi.mapHeight()).thenReturn(0);
		this.game.updateConstructionSites(this.bwapi);
		assertTrue(this.game.getConstructionSites().isEmpty());
		this.game.clean();
		assertEquals(0, this.game.getAgentCount());
	}
}
