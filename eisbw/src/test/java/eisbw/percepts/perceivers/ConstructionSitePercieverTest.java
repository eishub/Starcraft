package eisbw.percepts.perceivers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;

public class ConstructionSitePercieverTest {
	private ConstructionSitePerceiver perciever;

	@Mock
	private bwapi.Game bwapi;
	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private Player player;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		List<Unit> neutrals = new ArrayList<>(1);
		neutrals.add(this.unit);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unit.exists()).thenReturn(true);
		when(this.unit.getTilePosition()).thenReturn(new TilePosition(1, 1));
		when(this.unitType.toString()).thenReturn("Resource Mineral Field");

		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(true);
		when(this.bwapi.self()).thenReturn(this.player);
		when(this.player.getRace()).thenReturn(Race.Terran);

		when(this.bwapi.mapWidth()).thenReturn(10);
		when(this.bwapi.mapHeight()).thenReturn(10);
		when(this.bwapi.getNeutralUnits()).thenReturn(neutrals);
		this.perciever = new ConstructionSitePerceiver(this.bwapi);
	}

	@Test
	public void terran_test() {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void zerg_test() {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		when(this.player.getRace()).thenReturn(Race.Zerg);
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
		when(this.bwapi.hasCreep(any(TilePosition.class))).thenReturn(true);
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void protoss_test() {
		when(this.player.getRace()).thenReturn(Race.Protoss);
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(TilePosition.class), eq(UnitType.Protoss_Gateway), any(Unit.class),
				any(Boolean.class))).thenReturn(false);
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void noRace_test() {
		when(this.player.getRace()).thenReturn(Race.None);
		when(this.unit.exists()).thenReturn(false);
		when(this.unitType.toString()).thenReturn("not illegal");
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}
}
