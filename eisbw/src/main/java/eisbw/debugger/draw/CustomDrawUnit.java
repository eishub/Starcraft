package eisbw.debugger.draw;

import eisbw.BwapiUtility;
import eisbw.Game;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;

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
	 * @param unit
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
	protected void doDraw(JNIBWAPI api) {
		if (BwapiUtility.isValid(this.unit)) {
			Position aboveUnit = new Position(this.unit.getX(), this.unit.getY() - 30);
			api.drawText(aboveUnit, this.text, false);
		} else if (this.unit == null) { // mapagent
			api.drawText(new Position(10, 25, PosType.PIXEL), this.text, true);
		}
	}
}