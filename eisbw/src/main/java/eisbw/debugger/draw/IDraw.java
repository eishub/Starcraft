package eisbw.debugger.draw;

import java.util.logging.Level;
import java.util.logging.Logger;

import eis.eis2java.exception.TranslationException;
import eisbw.Game;
import jnibwapi.JNIBWAPI;

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

	protected abstract void doDraw(JNIBWAPI api) throws TranslationException;

	/**
	 * Draw on the map.
	 *
	 * @param api
	 *            - the StarCraft API.
	 */
	public void draw(JNIBWAPI api) {
		if (this.toggle) {
			try {
				doDraw(api);
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
