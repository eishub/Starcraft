package eisbw.percepts.perceivers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import eisbw.BwapiUtility;

public class ConstructionSitePercieverTest {
	private ConstructionSitePerceiver perciever;

	@Mock
	private bwapi.Game bwapi;
	@Mock
	private TilePosition mapsize;
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

		List<Unit> neutrals = new ArrayList<>();
		neutrals.add(this.unit);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unit.exists()).thenReturn(true);
		when(this.unit.getTilePosition()).thenReturn(this.mapsize);
		when(this.unitType.toString()).thenReturn("Resource Mineral Field");
		BwapiUtility.clearCache(this.unit);

		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(true);
		when(this.bwapi.self()).thenReturn(this.player);
		when(this.player.getRace()).thenReturn(Race.Terran);

		when(this.bwapi.mapWidth()).thenReturn(0);
		when(this.bwapi.mapHeight()).thenReturn(0);
		when(this.mapsize.getX()).thenReturn(10);
		when(this.mapsize.getY()).thenReturn(10);
		when(this.bwapi.getNeutralUnits()).thenReturn(neutrals);
		this.perciever = new ConstructionSitePerceiver(this.bwapi);
	}

	@Test
	public void terran_test() {
		Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());

		toReturn.clear();
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());
	}

	@Test
	public void zerg_test() {
		Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		when(this.player.getRace()).thenReturn(Race.Zerg);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());

		toReturn.clear();
		when(this.bwapi.hasCreep(any(TilePosition.class))).thenReturn(true);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());

		toReturn.clear();
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());
	}

	@Test
	public void protoss_test() {
		Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		when(this.player.getRace()).thenReturn(Race.Protoss);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());

		toReturn.clear();
		when(this.bwapi.canBuildHere(any(TilePosition.class), eq(UnitType.Protoss_Gateway), any(Unit.class),
				any(Boolean.class))).thenReturn(false);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());

		toReturn.clear();
		when(this.bwapi.canBuildHere(any(TilePosition.class), any(UnitType.class), any(Unit.class), any(Boolean.class)))
				.thenReturn(false);
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());
	}

	@Test
	public void noRace_test() {
		when(this.player.getRace()).thenReturn(Race.None);
		when(this.unit.exists()).thenReturn(false);
		when(this.unitType.toString()).thenReturn("not illegal");
		Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		this.perciever.perceive(toReturn);
		assertTrue(!toReturn.isEmpty());
	}
}
