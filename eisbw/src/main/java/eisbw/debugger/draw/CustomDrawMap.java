package eisbw.debugger.draw;

import org.openbw.bwapi4j.MapDrawer;
import org.openbw.bwapi4j.TilePosition;

import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class CustomDrawMap extends IDraw {
	private final TilePosition pos;
	private final String text;

	/**
	 * The CustomDrawMap constructor.
	 *
	 * @param game
	 *            The current game.
	 * @param pos
	 *            The position on the map to draw text on,
	 * @param text
	 *            The text to draw.
	 */
	public CustomDrawMap(Game game, TilePosition pos, String text) {
		super(game);
		this.pos = pos;
		this.text = text;
	}

	@Override
	protected void doDraw(MapDrawer api) {
		api.drawTextMap(this.pos.toPosition(), this.text);
	}
}