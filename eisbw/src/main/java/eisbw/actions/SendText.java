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

    /**
     * Checks if the invoked sendText action has a syntactically correct parameter.
	 * @param action The action that is invoked.
     * @return If the action contains an Identifier as a parameter.
	 */ 
    @Override
    public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1 && parameters.get(0) instanceof Identifier;
    }

    /**
     * Checks if this unit type can execute this action. Since SendText is not type specific, this should always
     * return true.
     * @param type
     *            The type of the unit performing the action.
     * @param action
     *            The evaluated action.
     * @return true, since sendText is not type specific.
     */
    @Override
    public boolean canExecute(UnitType type, Action action) {
        return true;
    }

    /**
     * Execute the sendText command
     * @param unit
     *            The unit performing the action.
     * @param action The action that is being performed.
     */
    @Override
    public void execute(Unit unit, Action action) {
        List<Parameter> parameters = action.getParameters();
        String text = ((Identifier) parameters.get(0)).getValue();

        this.api.sendText(text);
    }

    /**
     * @return The String notation of this method
     */
    @Override
    public String toString() {
        return "sendText";
    }
}
