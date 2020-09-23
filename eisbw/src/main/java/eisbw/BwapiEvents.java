package eisbw;

import jnibwapi.BWAPIEventListener;
import jnibwapi.Position;

/**
 * @author Danny & Harm - Stub class for all overriden event methods.
 */
public class BwapiEvents implements BWAPIEventListener {
	@Override
	public void connected() {
		// Method stub
	}

	@Override
	public void matchStart() {
		// Method stub
	}

	@Override
	public void matchFrame() {
		// Method stub
	}

	@Override
	public void matchEnd(final boolean winner) {
		// Method stub
	}

	@Override
	public void keyPressed(final int keyCode) {
		// Method stub
	}

	@Override
	public void sendText(final String text) {
		// Method stub
	}

	@Override
	public void receiveText(final String text) {
		// Method stub
	}

	@Override
	public void playerLeft(final int playerId) {
		// Method stub
	}

	@Override
	public void nukeDetect(final Position pos) {
		// Method stub
	}

	@Override
	public void nukeDetect() {
		// Method stub
	}

	@Override
	public void unitDiscover(final int unitId) {
		// Method stub
	}

	@Override
	public void unitEvade(final int unitId) {
		// Method stub
	}

	@Override
	public void unitShow(final int unitId) {
		// Method stub
	}

	@Override
	public void unitHide(final int unitId) {
		// Method stub
	}

	@Override
	public void unitCreate(final int unitId) {
		// Method stub
	}

	@Override
	public void unitDestroy(final int unitId) {
		// Method stub
	}

	@Override
	public void unitMorph(final int unitId) {
		// Method stub
	}

	@Override
	public void unitRenegade(final int unitId) {
		// Method stub
	}

	@Override
	public void saveGame(final String gameName) {
		// Method stub
	}

	@Override
	public void unitComplete(final int unitId) {
		// Method stub
	}

	@Override
	public void playerDropped(final int playerId) {
		// Method stub
	}
}
