package eisbw.debugger.draw;

import bwapi.Position;
import bwapi.Unit;
import eisbw.BwapiUtility;
import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class CustomDrawUnit extends IDraw {
	private final Unit unit;
	private final String text;

	/**
	 * The CustomDrawUnit constructor.
	 *
	 * @param game
	 *            The current game.
	 * @param text
	 *            The unit to draw text above. If null (i.e., the mapagent) the text
	 *            will be drawn on the top left of the screen instead.
	 * @param text
	 *            The text to draw.
	 */
	public CustomDrawUnit(Game game, Unit unit, String text) {
		super(game);
		this.unit = unit;
		this.text = text;
	}

	@Override
	protected void doDraw(bwapi.Game api) {
		if (BwapiUtility.isValid(this.unit)) {
			Position pos = this.unit.getPosition();
			api.drawTextMap(pos.getX(), pos.getY() - 30, this.text);
		} else if (this.unit == null) { // mapagent
			api.drawTextScreen(10, 20, this.text);
		}
	}
}