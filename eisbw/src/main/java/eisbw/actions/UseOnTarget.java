package eisbw.actions;

import eis.exceptions.ActException;
import eis.iilang.*;
import java.util.LinkedList;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;

public class UseOnTarget extends StarcraftAction {

    public UseOnTarget(JNIBWAPI api) {
        super(api);
    }

    @Override
    public boolean isValid(Action action) {
        LinkedList<Parameter> parameters = action.getParameters();

        if (parameters.size() == 2) { // type, targetId
            boolean isTechType = parameters.get(0) instanceof Identifier && utility.getTechType(((Identifier) parameters.get(0)).getValue()) != null;
            return isTechType && parameters.get(1) instanceof Numeral;
        }
        return false;
    }

    @Override
    public boolean canExecute(Unit unit, Action action) {
        LinkedList<Parameter> parameters = action.getParameters();
        TechType techType = utility.getTechType(((Identifier) parameters.get(0)).getValue());
        return techType.isTargetsUnits(); //check if unit has Tech?
    }

    @Override
    public void execute(Unit unit, Action action) throws ActException {
        LinkedList<Parameter> parameters = action.getParameters();
        TechType techType = utility.getTechType(((Identifier) parameters.get(0)).getValue());
        api.useTech(unit.getTypeID(), techType.getID(), ((Numeral) parameters.get(1)).getValue().intValue());
    }

    @Override
    public String toString() {
        return "useOnTarget(techType, targetId)";
    }
}
