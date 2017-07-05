package eisbw;

import org.junit.Before;
import org.junit.Test;

import bwapi.Position;

public class BwapiEventsTest {

	BwapiEvents events;

	@Before
	public void start() {
		this.events = new BwapiEvents();
	}

	@Test
	public void eventTests() {
		this.events.onStart();
		this.events.onFrame();
		this.events.onNukeDetect(new Position(0, 0));
		this.events.onPlayerDropped(null);
		this.events.onPlayerLeft(null);
		this.events.onReceiveText(null, "0");
		this.events.onSaveGame("0");
		this.events.onSendText("0");
		this.events.onUnitComplete(null);
		this.events.onUnitCreate(null);
		this.events.onUnitDestroy(null);
		this.events.onUnitDiscover(null);
		this.events.onUnitEvade(null);
		this.events.onUnitHide(null);
		this.events.onUnitMorph(null);
		this.events.onUnitRenegade(null);
		this.events.onUnitShow(null);
		this.events.onEnd(true);
	}
}
