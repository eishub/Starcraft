package eisbw.percepts.perceivers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;

public class MapPerceiverTest {
	private MapPerceiver perciever;

	@Mock
	private bwapi.Game bwapi;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.bwapi.mapWidth()).thenReturn(0);
		when(this.bwapi.mapHeight()).thenReturn(0);

		this.perciever = new MapPerceiver(this.bwapi);
	}

	@Test
	public void mapsize_test() {
		Map<PerceptFilter, List<Percept>> ret = new HashMap<>();
		this.perciever.perceive(ret);
		assertFalse(ret.isEmpty());
	}
}
