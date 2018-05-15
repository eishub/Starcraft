package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Identifier;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

/**
 * @author Thijs Molendijk & Thijs Raymakers - Send a chat message to all players in the game.
 */
public class SendText extends StarcraftAction {
    /**
     * The SendText constructor.
     *
     * @param api
     *            The BWAPI
     */
    public SendText(JNIBWAPI api) {
        super(api);
    }

    @Override
    public boolean isValid(Action action) {
		return true;
       // List<Parameter> parameters = action.getParameters();
     //   return parameters.size() == 1 && parameters.get(0) instanceof Identifier
    //            && getUnitType(((Identifier) parameters.get(0)).getValue()) != null;
    }

    @Override
    public boolean canExecute(UnitType type, Action action) {
        return true;
    }

    @Override
    public void execute(Unit unit, Action action) {
        List<Parameter> parameters = action.getParameters();
        String text = ((Identifier) parameters.get(0)).getValue();

        this.api.sendText(text);
    }

    @Override
    public String toString() {
        return "sendText";
    }
}
