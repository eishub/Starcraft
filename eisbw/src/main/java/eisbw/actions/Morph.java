package eisbw.actions;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

import java.util.LinkedList;

/**
 * @author Danny & Harm - Makes the unit morph into a specified unit.
 *
 */
public class Morph extends StarcraftAction {

  /**
   * The Morph constructor.
   * 
   * @param api
   *          The BWAPI
   */
  public Morph(JNIBWAPI api) {
    super(api);
  }

  @Override
  public boolean isValid(Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    if (parameters.size() == 1 && parameters.get(0) instanceof Identifier) {
      UnitType ut = BwapiUtility.getUnitType(((Identifier) parameters.get(0)).getValue());
      return ut != null;
    }
    return false;
  }

  @Override
  public boolean canExecute(Unit unit, Action action) {
    return api.getSelf().getRace().getID() == RaceTypes.Zerg.getID();
  }

  @Override
  public void execute(Unit unit, Action action) {
    LinkedList<Parameter> params = action.getParameters();
    String type = ((Identifier) params.get(0)).getValue();
    unit.morph(BwapiUtility.getUnitType(type));
  }

  @Override
  public String toString() {
    return "morph(Type)";
  }
}
