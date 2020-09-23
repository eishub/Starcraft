package eisbw;

import org.junit.Before;
import org.junit.Test;

import jnibwapi.Position;

public class BwapiEventsTest {
	private BwapiEvents events;

	@Before
	public void start() {
		this.events = new BwapiEvents();
	}

	@Test
	public void eventTests() {
		this.events.matchStart();
		this.events.connected();
		this.events.nukeDetect();
		this.events.keyPressed(0);
		this.events.matchFrame();
		this.events.nukeDetect(new Position(0, 0));
		this.events.playerDropped(0);
		this.events.playerLeft(0);
		this.events.receiveText("0");
		this.events.saveGame("0");
		this.events.sendText("0");
		this.events.unitComplete(0);
		this.events.unitCreate(0);
		this.events.unitDestroy(0);
		this.events.unitDiscover(0);
		this.events.unitEvade(0);
		this.events.unitHide(0);
		this.events.unitMorph(0);
		this.events.unitRenegade(0);
		this.events.unitShow(0);
		this.events.matchEnd(true);
	}
}
