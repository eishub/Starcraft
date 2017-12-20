package eisbw.debugger.draw;

import org.openbw.bwapi4j.MapDrawer;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.unit.PlayerUnit;

import eisbw.BwapiUtility;
import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class CustomDrawUnit extends IDraw {
	private final PlayerUnit unit;
	private final String text;

	/**
	 * The CustomDrawUnit constructor.
	 *
	 * @param game
	 *            The current game.
	 * @param unit
	 *            The unit to draw text above. If null (i.e., the mapagent) the text
	 *            will be drawn on the top left of the screen instead.
	 * @param text
	 *            The text to draw.
	 */
	public CustomDrawUnit(Game game, PlayerUnit unit, String text) {
		super(game);
		this.unit = unit;
		this.text = text;
	}

	@Override
	protected void doDraw(MapDrawer api) {
		if (BwapiUtility.isValid(this.unit)) {
			Position aboveUnit = new Position(this.unit.getX(), this.unit.getY() - 30);
			api.drawTextMap(aboveUnit, this.text);
		} else if (this.unit == null) { // mapagent
			api.drawTextScreen(10, 25, this.text);
		}
	}
}