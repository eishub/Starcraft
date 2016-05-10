package eisbw.actions;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;

import java.util.LinkedList;

public class Use extends StarcraftAction {

  public Use(JNIBWAPI api) {
    super(api);
  }

  @Override
  public boolean isValid(Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    if (parameters.size() == 1) { // type
      return parameters.get(0) instanceof Identifier
          && BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue()) != null;
    }

    return false;
  }

  @Override
  public boolean canExecute(Unit unit, Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());

    return !techType.isTargetsPosition() && !techType.isTargetsUnits();
  }

  @Override
  public void execute(Unit unit, Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    TechType techType = BwapiUtility.getTechType(((Identifier) parameters.get(0)).getValue());
    unit.useTech(techType);
  }

  @Override
  public String toString() {
    return "ability(Type)";
  }
}
