package eisbw.percepts.perceivers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;
import jnibwapi.bwapi.Game;
import jnibwapi.Position.PosType;
import jnibwapi.types.RaceType.RaceTypes;

public class WorkerPerceiverTest {

  private WorkerPerceiver perciever;
  private List<Unit> units;
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
  private UnitType unitType2;
  @Mock
  private UnitType unitType3;
  @Mock
  private bwapi.Game api;
  @Mock
  private Player self;
  private List<Unit> toreturn;

  /**
   * Initialize mocks.
   */
  @Before
  public void start() {
    MockitoAnnotations.initMocks(this);

    when(unit.getType()).thenReturn(unitType);
    when(unitType.getID()).thenReturn(RaceTypes.Terran.getID());
    when(api.getNeutralUnits()).thenReturn(new LinkedList<Unit>());
    toreturn = new LinkedList<>();
    toreturn.add(unit);
    when(api.getMyUnits()).thenReturn(toreturn);

    when(unit.getType()).thenReturn(unitType);
    when(unit.getResourceGroup()).thenReturn(1);
    when(unit.getResources()).thenReturn(2);
    when(unit.getPosition()).thenReturn(new Position(1, 2, PosType.BUILD));

    when(unit3.getResourceGroup()).thenReturn(1);
    when(unit3.getResources()).thenReturn(2);
    when(unit3.getPosition()).thenReturn(new Position(1, 2, PosType.BUILD));

    units = new LinkedList<>();
    units.add(unit);
    units.add(unit4);
    units.add(unit3);
    units.add(unit2);

    when(api.getMyUnits()).thenReturn(units);
    when(api.getNeutralUnits()).thenReturn(units);

    when(unit2.isVisible()).thenReturn(false);
    when(unit3.isVisible()).thenReturn(true);
    when(unit4.isVisible()).thenReturn(true);
    when(unit3.getType()).thenReturn(unitType2);
    when(unit2.getType()).thenReturn(unitType2);
    when(unit4.getType()).thenReturn(unitType3);
    when(unit.isVisible()).thenReturn(true);
    when(unitType.getName()).thenReturn("Resource Mineral Field");
    when(unitType2.getName()).thenReturn("Resource Vespene Geyser");
    when(unitType3.getName()).thenReturn("No Resource");

    perciever = new WorkerPerceiver(api, unit);
  }

  @Test
  public void gas_test() {
    when(unitType.isWorker()).thenReturn(true);
    when(unitType.isMechanical()).thenReturn(true);
    when(unit.isGatheringGas()).thenReturn(true);

    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void minerals_test() {
    when(unitType.isWorker()).thenReturn(true);
    when(unitType.isMechanical()).thenReturn(true);
    when(unit.isGatheringMinerals()).thenReturn(true);

    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void constructing_test() {
    when(unitType.isWorker()).thenReturn(true);
    when(unitType.isMechanical()).thenReturn(true);
    when(unit.isConstructing()).thenReturn(true);
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void idle_test() {
    when(unitType.isWorker()).thenReturn(true);
    when(unitType.isMechanical()).thenReturn(true);
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void noTerran_test() {
    when(unitType.getID()).thenReturn(RaceTypes.None.getID());
    when(unitType.isWorker()).thenReturn(true);
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
    when(unitType.isWorker()).thenReturn(false);
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void repair_test() {
    when(unitType.isMechanical()).thenReturn(true);
    when(unit.getHitPoints()).thenReturn(0);
    when(unitType.getMaxHitPoints()).thenReturn(1);
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
    when(unit.getHitPoints()).thenReturn(1);
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void geyser_test() {
    when(api.getNeutralUnits()).thenReturn(toreturn);
    when(unit.getType()).thenReturn(UnitType.UnitTypes.Resource_Vespene_Geyser);
    when(unit.getPosition()).thenReturn(new Position(1, 2));
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
    when(unit.getType()).thenReturn(unitType);
    assertFalse(perciever.perceive(ret).isEmpty());
  }

  @Test
  public void gathering_test() {
    when(unit.isGatheringMinerals()).thenReturn(true);
    when(unit.getOrderTarget()).thenReturn(unit);
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    assertFalse(perciever.perceive(ret).isEmpty());
  }

}
