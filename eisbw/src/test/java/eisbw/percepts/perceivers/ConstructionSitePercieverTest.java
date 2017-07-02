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

import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;
import jnibwapi.bwapi.Game;
import jnibwapi.types.RaceType.RaceTypes;

public class ConstructionSitePercieverTest {

  private ConstructionSitePerceiver perciever;
  @Mock
  private bwapi.Game bwapi;
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
    neutrals.add(unit);
    when(unit.getType()).thenReturn(unitType);
    when(unit.isExists()).thenReturn(true);
    when(unit.getTilePosition()).thenReturn(mapsize);
    when(unitType.getName()).thenReturn("Resource Mineral Field");

    when(bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class)))
        .thenReturn(true);
    when(bwapi.getSelf()).thenReturn(player);
    when(player.getRace()).thenReturn(RaceTypes.Terran);

    when(bwapi.getMap()).thenReturn(map);
    when(map.getSize()).thenReturn(mapsize);
    when(mapsize.getBX()).thenReturn(10);
    when(mapsize.getBY()).thenReturn(10);
    when(bwapi.getNeutralUnits()).thenReturn(neutrals);
    perciever = new ConstructionSitePerceiver(bwapi);
  }

  @Test
  public void terran_test() {
    Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
    assertTrue(perciever.perceive(toReturn).size() > 0);
    when(bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class)))
        .thenReturn(false);
    toReturn = new HashMap<>();
    assertTrue(!perciever.perceive(toReturn).isEmpty());
  }

  @Test
  public void zerg_test() {
    Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
    when(player.getRace()).thenReturn(RaceTypes.Zerg);
    assertTrue(!perciever.perceive(toReturn).isEmpty());
    when(bwapi.hasCreep(any(Position.class))).thenReturn(true);
    assertTrue(perciever.perceive(toReturn).size() > 0);
    when(bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class)))
        .thenReturn(false);
    toReturn = new HashMap<>();
    assertTrue(!perciever.perceive(toReturn).isEmpty());
  }

  @Test
  public void protoss_test() {
    when(player.getRace()).thenReturn(RaceTypes.Protoss);
    Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
    assertTrue(perciever.perceive(toReturn).size() > 0);
    when(bwapi.canBuildHere(any(Position.class), eq(UnitType.UnitTypes.Protoss_Gateway),
        any(Boolean.class))).thenReturn(false);
    assertTrue(perciever.perceive(toReturn).size() > 0);
    when(bwapi.canBuildHere(any(Position.class), any(UnitType.class), any(Boolean.class)))
        .thenReturn(false);
    toReturn = new HashMap<>();
    assertTrue(!perciever.perceive(toReturn).isEmpty());
  }

  @Test
  public void noRace_test() {
    when(player.getRace()).thenReturn(RaceTypes.None);
    when(unit.isExists()).thenReturn(false);
    when(unitType.getName()).thenReturn("not illegal");
    Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
    assertTrue(!perciever.perceive(toReturn).isEmpty());
  }

}
