package eisbw.percepts.perceivers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class ConstructionSitePercieverTest {

	private ConstructionSitePerceiver perciever;
	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private jnibwapi.Map map;
	@Mock
	private Position mapsize;
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
		ArrayList<Unit> neutrals = new ArrayList<>();
		neutrals.add(this.unit);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unit.isExists()).thenReturn(true);
		when(this.unit.getTilePosition()).thenReturn(this.mapsize);
		when(this.unitType.getName()).thenReturn("Resource Mineral Field");

		when(this.bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class))).thenReturn(true);
		when(this.bwapi.getSelf()).thenReturn(this.player);
		when(this.player.getRace()).thenReturn(RaceTypes.Terran);

		when(this.bwapi.getMap()).thenReturn(this.map);
		when(this.map.getSize()).thenReturn(this.mapsize);
		when(this.mapsize.getBX()).thenReturn(10);
		when(this.mapsize.getBY()).thenReturn(10);
		when(this.bwapi.getNeutralUnits()).thenReturn(neutrals);
		this.perciever = new ConstructionSitePerceiver(this.bwapi);
	}

	@Test
	public void terran_test() {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class))).thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void zerg_test() {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		when(this.player.getRace()).thenReturn(RaceTypes.Zerg);
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
		when(this.bwapi.hasCreep(any(Position.class))).thenReturn(true);
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class))).thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void protoss_test() {
		when(this.player.getRace()).thenReturn(RaceTypes.Protoss);
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(Position.class), eq(UnitType.UnitTypes.Protoss_Gateway), any(Boolean.class)))
				.thenReturn(false);
		assertTrue(this.perciever.perceive(toReturn).size() > 0);
		when(this.bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class))).thenReturn(false);
		toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

	@Test
	public void noRace_test() {
		when(this.player.getRace()).thenReturn(RaceTypes.None);
		when(this.unit.isExists()).thenReturn(false);
		when(this.unitType.getName()).thenReturn("not illegal");
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertTrue(!this.perciever.perceive(toReturn).isEmpty());
	}

}
