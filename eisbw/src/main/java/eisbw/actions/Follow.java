package eisbw.actions;

import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.LinkedList;

public class Follow extends StarcraftAction {

  public Follow(JNIBWAPI api) {
    super(api);
  }

  @Override
  public boolean isValid(Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    return parameters.size() == 1 && parameters.get(0) instanceof Numeral;
  }

  @Override
  public boolean canExecute(Unit unit, Action action) {
    UnitType unitType = unit.getType();
    return unitType.isCanMove();
  }

  @Override
  public void execute(Unit unit, Action action) {
    LinkedList<Parameter> parameters = action.getParameters();
    int targetId = ((Numeral) parameters.get(0)).getValue().intValue();
    Unit target = api.getUnit(targetId);

    unit.follow(target, false);
  }

  @Override
  public String toString() {
    return "follow(target)";
  }
}