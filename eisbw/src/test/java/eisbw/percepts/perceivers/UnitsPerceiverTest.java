//package eisbw.percepts.perceivers;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.mockito.Mockito.when;
//
//import eis.iilang.Percept;
//import eisbw.units.ConditionHandler;
//import jnibwapi.bwapi.Game;
//import jnibwapi.Position;
//import jnibwapi.Position.PosType;
//import jnibwapi.Unit;
//import jnibwapi.types.UnitType;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Set;
//
//public class UnitsPerceiverTest {
//
//  private final Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
//
//  private UnitsPerceiver perceiver;
//  private LinkedList<Unit> toreturn;
//  @Mock
//  private Unit unit;
//  @Mock
//  private bwapi.Game bwapi;
//  @Mock
//  private UnitType unitType;
//  @Mock
//  private UnitType unitType2;
//  @Mock
//  private Unit unit2;
//  @Mock
//  private ConditionHandler cond;
//
//  /**
//   * Initialize mocks.
//   */
//  @Before
//  public void start() {
//    MockitoAnnotations.initMocks(this);
//    toreturn = new LinkedList<>();
//    toreturn.add(unit);
//    perceiver = new UnitsPerceiver(bwapi);
//
//    when(unit.getType()).thenReturn(unitType);
//    when(unit2.getType()).thenReturn(unitType2);
//    when(unitType.getName()).thenReturn("unitType");
//    when(unitType.getName()).thenReturn("unitType");
//    when(unit2.getID()).thenReturn(1);
//    when(unit2.getHitPoints()).thenReturn(20);
//    when(unit2.getShields()).thenReturn(10);
//    when(cond.getConditions()).thenReturn(new LinkedList<>());
//
//    when(bwapi.getEnemyUnits()).thenReturn(toreturn);
//    when(bwapi.getMyUnits()).thenReturn(toreturn);
//    when(unit.getPosition()).thenReturn(new Position(10, 12, PosType.BUILD));
//    when(unit2.getPosition()).thenReturn(new Position(10, 12, PosType.BUILD));
//  }
//
//  @Test
//  public void test() {
//    assertFalse(perceiver.perceive(ret).isEmpty());
//  }
//
//  @Test
//  public void checkConditionsTrue_test() {
//    when(unitType.isFlyer()).thenReturn(true);
//    when(unit.isMorphing()).thenReturn(true);
//    when(unit.isCloaked()).thenReturn(true);
//    when(unit.isBeingConstructed()).thenReturn(true);
//    assertEquals(3, perceiver.perceive(ret).size());
//  }
//
//  @Test
//  public void checkConditionsFalse_test() {
//    when(unitType.isFlyer()).thenReturn(false);
//    when(unit.isMorphing()).thenReturn(false);
//    when(unit.isCloaked()).thenReturn(false);
//    when(unit.isBeingConstructed()).thenReturn(false);
//    assertEquals(3, perceiver.perceive(ret).size());
//  }
//
//  @Test
//  public void attackcapable_test() {
//    when(unitType.isAttackCapable()).thenReturn(true);
//    when(unit.getOrderTarget()).thenReturn(unit2);
//    when(unitType2.isAttackCapable()).thenReturn(true);
//
//    assertEquals(3, perceiver.perceive(ret).size());
//    when(unitType.isAttackCapable()).thenReturn(false);
//
//    assertEquals(3, perceiver.perceive(ret).size());
//    when(unitType.isAttackCapable()).thenReturn(true);
//    when(unitType2.isAttackCapable()).thenReturn(false);
//
//    assertEquals(3, perceiver.perceive(ret).size());
//    when(unit.getOrderTarget()).thenReturn(null);
//
//    assertEquals(3, perceiver.perceive(ret).size());
//  }
//
//}