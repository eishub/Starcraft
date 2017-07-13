package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;
import eisbw.BwapiUtility;

public class GenericUnitPerceiverTest {
	private GenericUnitPerceiver perciever;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private Player self;
	@Mock
	private bwapi.Game api;
	@Mock
	private Race race;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		BwapiUtility.clearPlayerCache();

		when(this.api.enemy()).thenReturn(this.self);

		when(this.self.getRace()).thenReturn(Race.None);

		when(this.api.self()).thenReturn(this.self);
		when(this.self.minerals()).thenReturn(50);
		when(this.self.gas()).thenReturn(90);
		when(this.self.supplyUsed()).thenReturn(10);
		when(this.self.supplyTotal()).thenReturn(20);

		when(this.unit.getID()).thenReturn(1);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("type");

		when(this.unit.getHitPoints()).thenReturn(25);
		when(this.unit.getShields()).thenReturn(30);
		when(this.unit.getTilePosition()).thenReturn(new TilePosition(2, 1));

		when(this.unit.getEnergy()).thenReturn(100);
		when(this.unitType.maxEnergy()).thenReturn(110);
		this.perciever = new GenericUnitPerceiver(this.api, this.unit);
	}

	// @Test FIXME (native call)
	public void size_test() {
		Map<PerceptFilter, List<Percept>> ret = new HashMap<>();
		when(this.race.toString()).thenReturn("race");
		when(this.self.getRace()).thenReturn(this.race);
		this.perciever.perceive(ret);
		assertEquals(3, ret.size());

		ret.clear();
		when(this.api.enemy()).thenReturn(null);
		this.perciever.perceive(ret);
		assertEquals(3, ret.size());

		ret.clear();
		when(this.unitType.maxEnergy()).thenReturn(0);
		when(this.unit.isDefenseMatrixed()).thenReturn(true);
		this.perciever.perceive(ret);
		assertEquals(4, ret.size());
	}
}
