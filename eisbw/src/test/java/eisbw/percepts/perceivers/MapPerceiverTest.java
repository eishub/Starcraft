package eisbw.percepts.perceivers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import jnibwapi.BaseLocation;
import jnibwapi.ChokePoint;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Region;

public class MapPerceiverTest {

	private MapPerceiver perciever;
	@Mock
	private JNIBWAPI bwapi;
	@Mock
	private jnibwapi.Map map;
	@Mock
	private Position mapsize;
	@Mock
	private BaseLocation baselocation;
	@Mock
	private Region region;
	@Mock
	private ChokePoint chokepoint;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.bwapi.getMap()).thenReturn(this.map);
		when(this.map.getSize()).thenReturn(this.mapsize);
		when(this.mapsize.getBX()).thenReturn(10);
		when(this.mapsize.getBY()).thenReturn(11);

		this.perciever = new MapPerceiver(this.bwapi);
	}

	@Test
	public void mapsize_test() {
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void baseLocation_test() {
		LinkedList<BaseLocation> locs = new LinkedList<>();
		locs.add(this.baselocation);
		when(this.map.getBaseLocations()).thenReturn(locs);
		when(this.baselocation.getPosition()).thenReturn(new Position(3, 4, PosType.BUILD));
		when(this.baselocation.isStartLocation()).thenReturn(true);
		when(this.baselocation.getRegion()).thenReturn(this.region);
		when(this.region.getID()).thenReturn(5);

		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void chokepoint_test() {
		LinkedList<ChokePoint> locs = new LinkedList<>();
		locs.add(this.chokepoint);
		when(this.map.getChokePoints()).thenReturn(locs);
		when(this.chokepoint.getFirstSide()).thenReturn(new Position(5, 6, PosType.BUILD));
		when(this.chokepoint.getCenter()).thenReturn(new Position(6, 6, PosType.BUILD));
		when(this.chokepoint.getSecondSide()).thenReturn(new Position(6, 7, PosType.BUILD));
		when(this.chokepoint.getFirstRegion()).thenReturn(new Region(new int[] { 1, 1, 1 }, 0, new int[] {}));
		when(this.chokepoint.getSecondRegion()).thenReturn(new Region(new int[] { 2, 2, 2 }, 0, new int[] {}));

		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

}
