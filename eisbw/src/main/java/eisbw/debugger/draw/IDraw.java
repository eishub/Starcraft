package eisbw.debugger.draw;

import java.util.logging.Level;
import java.util.logging.Logger;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eisbw.Game;

/**
 * @author Danny & Harm - The abstract class for the drawing classes.
 *
 */
public abstract class IDraw {
	protected Logger logger = Logger.getLogger("StarCraft Logger"); // overriden
																	// in test

	protected final Game game;
	private boolean toggle = false;

	/**
	 * The IDraw constructor.
	 *
	 * @param game
	 *            the current game.
	 */
	public IDraw(Game game) {
		this.game = game;
	}

	protected abstract void drawOnMap(bwapi.Game api) throws TranslationException;

	/**
	 * Draw on the map.
	 *
	 * @param api
	 *            - the StarCraft API.
	 */
	public void draw(bwapi.Game api) {
		if (this.toggle) {
			try {
				drawOnMap(api);
			} catch (NoTranslatorException exception) {
				this.logger.log(Level.WARNING, "No translator in draw function", exception);
			} catch (TranslationException exception) {
				this.logger.log(Level.WARNING, "Cannot translate in draw function", exception);
			}
		}
	}

	/**
	 * Handles the toggling of the drawing.
	 */
	public void toggle() {
		this.toggle = !this.toggle;
	}
}
