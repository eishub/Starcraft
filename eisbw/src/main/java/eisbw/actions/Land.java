package eisbw.actions;

import eis.exceptions.ActException;
import eis.iilang.*;

import java.util.LinkedList;

import jnibwapi.*;
import jnibwapi.Position.PosType;

public class Land extends StarcraftAction {

    public Land(JNIBWAPI api) {
        super(api);
    }

    @Override
    public boolean isValid(Action action) {
        LinkedList<Parameter> parameters = action.getParameters();
        return (parameters.size() == 2) && parameters.get(0) instanceof Numeral
                && parameters.get(1) instanceof Numeral;
    }

    @Override
    public boolean canExecute(Unit unit, Action action) {
        return isValid(action) && unit.isLifted();
    }

    @Override
    public void execute(Unit unit, Action action) throws ActException {
    	LinkedList<Parameter> parameters = action.getParameters();
    	int x = ((Numeral) parameters.get(0)).getValue().intValue();
        int y = ((Numeral) parameters.get(1)).getValue().intValue();
        unit.land(new Position(x, y, PosType.BUILD));
    }

    @Override
    public String toString() {
        return "land(x,y)";
    }
}
