package eisbw.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;

public class BuildAddonTest {

  private BuildAddon action;
  private LinkedList<Parameter> params;

  @Mock
  private JNIBWAPI bwapi;
  @Mock
  private Action act;
  @Mock
  private Unit unit;
  @Mock
  private UnitType unitType;

  /**
   * Initialize mocks.
   */
  @Before
  public void start() {
    MockitoAnnotations.initMocks(this);
    action = new BuildAddon(bwapi);
    
    params = new LinkedList<>();
    params.add(new Identifier("Working"));
    params.add(new Numeral(2));
    
    when(act.getParameters()).thenReturn(params);
    when(unit.getType()).thenReturn(unitType);
  }

  @Test
  public void isValid_test() {
    assertFalse(action.isValid(act));
    params.remove(1);
    assertFalse(action.isValid(act));
    params.set(0, new Numeral(1));
    assertFalse(action.isValid(act));
    params.set(0, new Identifier("Hero Mojo"));
    assertFalse(action.isValid(act));
  }
  
  @Test
  public void canExecute_test() {
    when(unitType.isBuilding()).thenReturn(false);
    when(unit.getAddon()).thenReturn(unit);
    assertFalse(action.canExecute(unit, act));
    when(unitType.isBuilding()).thenReturn(true);
    assertFalse(action.canExecute(unit, act));
    when(unit.getAddon()).thenReturn(null);
    assertTrue(action.canExecute(unit, act));
    when(unitType.isBuilding()).thenReturn(false);
    assertFalse(action.canExecute(unit, act));
  }
  
  @Test
  public void execute_test() {
    action.execute(unit, act);
    verify(unit).buildAddon(null);
  }
  
  @Test
  public void toString_test() {
    assertEquals("buildAddon(Type)", action.toString());
  }

}
