package eisbw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	private Map<PerceptFilter, Set<Percept>> percepts;
	private Map<String, StarcraftUnit> unitList;

	@Mock
	private StarcraftEnvironmentImpl env;
	@Mock
	private bwapi.Game bwapi;
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

		this.unitList = new HashMap<>();
		this.unitList.put("unit", this.scUnit);

		this.percepts = new HashMap<>();
		Set<Percept> perc = new HashSet<>();
		perc.add(new ConstructionSitePercept(1, 2, 3));
		this.percepts.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), perc);

		when(this.scUnit.perceive()).thenReturn(this.percepts);

		when(this.units.getStarcraftUnits()).thenReturn(this.unitList);

		Map<String, Unit> unitMap = new HashMap<>();
		unitMap.put("unit", this.unit);
		when(this.unit.getType()).thenReturn(this.type);
		when(this.type.isWorker()).thenReturn(true);
		when(this.units.getUnits()).thenReturn(unitMap);

		when(this.env.getAgents()).thenReturn(new LinkedList<String>());

		this.game = new Game(this.env);
		this.game.units = this.units;
	}

	@Test
	public void update_test() {
		this.game.update(this.bwapi);
		assertTrue(this.game.getUnits() == this.units);
		assertTrue(this.game.getPercepts("null").isEmpty());
		assertEquals(2, this.game.getPercepts("unit").size());
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
