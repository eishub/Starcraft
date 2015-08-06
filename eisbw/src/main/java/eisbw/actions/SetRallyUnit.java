package eisbw.actions;

import eis.exceptions.ActException;
import eis.iilang.*;

import java.util.LinkedList;

import jnibwapi.*;
import jnibwapi.types.TechType;

public class SetRallyUnit extends StarcraftAction {

    public SetRallyUnit(JNIBWAPI api) {
        super(api);
    }

    @Override
    public boolean isValid(Action action) {
        LinkedList<Parameter> parameters = action.getParameters();
        if (parameters.size() == 2) { // type
            return parameters.get(0) instanceof Numeral;
        }

        return false;
    }

    @Override
    public boolean canExecute(Unit unit, Action action) {
        return isValid(action) && unit.getType().isBuilding();
    }

    @Override
    public void execute(Unit unit, Action action) throws ActException {
        LinkedList<Parameter> parameters = action.getParameters();
        int unitID = ((Numeral) parameters.get(0)).getValue().intValue();
        unit.setRallyPoint(api.getUnit(unitID));
    }

    @Override
    public String toString() {
        return "setRallyUnit(Unit)";
    }
}
