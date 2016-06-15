package eisbw.actions;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.LinkedList;

/**
 * @author Danny & Harm - Makes the (worker) unit build on the specified (not
 *         obstructed) location.
 *
 */
public class Build extends StarcraftAction {

  /**
   * The Build constructor.
   * 
   * @param api
   *          The BWAPI
   */
  public Build(JNIBWAPI api) {
    super(api);
  }

  @Override
  public boolean isValid(Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    if (parameters.size() == 3 && parameters.get(0) instanceof Identifier) {
      UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
      return ut != null 
          && ut.isBuilding() 
          && parameters.get(1) instanceof Numeral
          && parameters.get(2) instanceof Numeral;
    }
    return false;
  }

  @Override
  public boolean canExecute(Unit unit, Action action) {
    return unit.getType().isWorker();
  }

  @Override
  public void execute(Unit unit, Action action) {
    LinkedList<Parameter> params = action.getParameters();
    String type = ((Identifier) params.get(0)).getValue();
    int tx = ((Numeral) params.get(1)).getValue().intValue();
    int ty = ((Numeral) params.get(2)).getValue().intValue();
    unit.build(new Position(tx, ty, PosType.BUILD), getUnitType(type));
  }

  @Override
  public String toString() {
    return "build(Type, X, Y)";
  }
}
