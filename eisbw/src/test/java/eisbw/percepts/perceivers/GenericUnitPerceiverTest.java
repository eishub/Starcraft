package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import eis.iilang.Percept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.RaceType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GenericUnitPerceiverTest {

  private GenericUnitPerceiver perciever;
  @Mock
  private Unit unit;
  @Mock
  private UnitType unitType;
  @Mock
  private Player self;
  @Mock
  private JNIBWAPI api;
  @Mock
  private RaceType race;

  private Set<Player> toReturn;

  /**
   * Initialize mocks.
   */
  @Before
  public void start() {
    MockitoAnnotations.initMocks(this);
    toReturn = new HashSet<>();
    toReturn.add(self);
    when(api.getEnemies()).thenReturn(toReturn);

    when(self.getRace()).thenReturn(RaceTypes.None);

    when(api.getSelf()).thenReturn(self);
    when(self.getMinerals()).thenReturn(50);
    when(self.getGas()).thenReturn(90);
    when(self.getSupplyUsed()).thenReturn(10);
    when(self.getSupplyTotal()).thenReturn(20);

    when(unit.getID()).thenReturn(1);
    when(unit.getType()).thenReturn(unitType);
    when(unitType.getName()).thenReturn("type");

    when(unit.getHitPoints()).thenReturn(25);
    when(unit.getShields()).thenReturn(30);
    when(unit.getPosition()).thenReturn(new Position(2, 1, PosType.BUILD));

    when(unit.getEnergy()).thenReturn(100);
    when(unitType.getMaxEnergy()).thenReturn(110);
    perciever = new GenericUnitPerceiver(api, unit);
  }

  @Test
  public void size_test() {
    Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
    when(race.getName()).thenReturn("race");
    when(self.getRace()).thenReturn(race);
    assertEquals(4, perciever.perceive(ret).size());
    toReturn = new HashSet<>();
    when(api.getEnemies()).thenReturn(toReturn);
    ret = new HashMap<>();
    assertEquals(4, perciever.perceive(ret).size());
    when(unitType.getMaxEnergy()).thenReturn(0);
    when(unit.isDefenseMatrixed()).thenReturn(true);
    ret = new HashMap<>();
    assertEquals(5, perciever.perceive(ret).size());
  }

}
