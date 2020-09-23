package eisbw.debugger.draw;

import eisbw.Game;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;

/**
 * @author Harm & Danny.
 */
public class CustomDrawMap extends IDraw {
	private final Position pos;
	private final String text;

	/**
	 * The CustomDrawMap constructor.
	 *
	 * @param game The current game.
	 * @param pos  The position on the map to draw text on,
	 * @param text The text to draw.
	 */
	public CustomDrawMap(final Game game, final Position pos, final String text) {
		super(game);
		this.pos = pos;
		this.text = text;
	}

	@Override
	protected void doDraw(final JNIBWAPI api) {
		api.drawText(this.pos, this.text, false);
	}
}