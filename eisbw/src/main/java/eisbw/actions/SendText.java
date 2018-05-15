package eisbw.actions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Identifier;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

/**
 * @author Thijs Molendijk & Thijs Raymakers - Send a chat message to all players in the game.
 */
public class SendText extends StarcraftAction {
	private static List<String> CHEATS = Arrays.asList(
			"show me the money",
			"whats mine is mine",
			"breathe deep",
			"something for nothing",
			"operation cwal",
			"modify the phase variance",
			"medieval man",
			"the gathering",
			"noglues",
			"power overwhelming",
			"food for thought",
			"there is no cow level",
			"ophelia",
			"staying alive",
			"man over game",
			"game over man",
			"war aint waht it used to be",
			"black sheep wall",
			"radio free zerg"
	);

	// Use a timed semaphore to limit the amount of messages sent per second.
	private TimedSemaphore rateLimit = new TimedSemaphore(1, TimeUnit.SECONDS, 1);

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
		String text = ((Identifier) parameters.get(0)).getValue().trim();

		// Deny chats that are too long.
		if (text.length() > 140) return;

		// Deny mission selects.
		if (text.toLowerCase().startsWith("terran")
			|| text.toLowerCase().startsWith("zerg")
			|| text.toLowerCase().startsWith("protoss")) return;

		// Deny any slash commands.
		if (text.toLowerCase().startsWith("/")) return;

		// Deny any cheats.
		if (CHEATS.contains(text.toLowerCase())) return;

		// Only allow up to 1 message a second.
		if (!this.rateLimit.tryAcquire()) return;

		// Everything is okay, send the message.
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
