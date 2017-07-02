package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
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
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;
import jnibwapi.bwapi.Game;
import jnibwapi.Position.Positions;
import jnibwapi.types.UpgradeType.UpgradeTypes;

public class BuildingPerceiverTest {

  private BuildingPerceiver perciever;
  @Mock
  private Unit unit;
  @Mock
  private UnitType unitType;
  @Mock
  private bwapi.Game api;
  @Mock
  private Player self;

  /**
   * Initialize mocks.
   */
  @Before
  public void start() {
    MockitoAnnotations.initMocks(this);

    when(unit.getRallyPosition()).thenReturn(new Position(1, 1));
    when(unit.getRallyUnit()).thenReturn(unit);
    when(unit.getID()).thenReturn(1);
    when(unit.getTrainingQueueSize()).thenReturn(1);
    when(unit.isUpgrading()).thenReturn(true);
    when(unit.getUpgrade()).thenReturn(UpgradeTypes.Adrenal_Glands);
    when(api.getSelf()).thenReturn(self);
    when(self.isResearched(any(TechType.class))).thenReturn(false);
    when(unit.getType()).thenReturn(unitType);
    when(unitType.getName()).thenReturn("name");
    when(unitType.getSpaceProvided()).thenReturn(1);
    when(unit.getLoadedUnits()).thenReturn(new LinkedList<Unit>());

    perciever = new BuildingPerceiver(api, unit);
  }

  @Test
  public void size_test() {
    Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
    assertEquals(4, perciever.perceive(toReturn).size());
    when(unit.getRallyPosition()).thenReturn(Positions.None);
    toReturn = new HashMap<>();
    assertEquals(3, perciever.perceive(toReturn).size());
    when(unit.getRallyUnit()).thenReturn(null);
    toReturn = new HashMap<>();
    assertEquals(2, perciever.perceive(toReturn).size());
    toReturn = new HashMap<>();
    when(unit.isUpgrading()).thenReturn(false);
    assertEquals(1, perciever.perceive(toReturn).size());
    toReturn = new HashMap<>();
    List<Unit> loadedunits = new LinkedList<>();
    loadedunits.add(unit);
    loadedunits.add(null);
    when(unit.getLoadedUnits()).thenReturn(loadedunits);
    assertEquals(1, perciever.perceive(toReturn).size());
    toReturn = new HashMap<>();
    when(unitType.getSpaceProvided()).thenReturn(0);
    assertEquals(1, perciever.perceive(toReturn).size());
    toReturn = new HashMap<>();
    when(self.isResearched(any(TechType.class))).thenReturn(true);
    assertEquals(1, perciever.perceive(toReturn).size());
  }
  
}
