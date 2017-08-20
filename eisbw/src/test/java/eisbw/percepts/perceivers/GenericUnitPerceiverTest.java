package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.RaceType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class GenericUnitPerceiverTest {
	private GenericUnitPerceiver perciever;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private Player self;
	@Mock
	private JNIBWAPI api;
	@Mock
	private RaceType race;

	private Set<Player> toReturn;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		this.toReturn = new HashSet<>(1);
		this.toReturn.add(this.self);
		when(this.api.getEnemies()).thenReturn(this.toReturn);

		when(this.self.getRace()).thenReturn(RaceTypes.None);

		when(this.api.getSelf()).thenReturn(this.self);
		when(this.self.getMinerals()).thenReturn(50);
		when(this.self.getGas()).thenReturn(90);
		when(this.self.getSupplyUsed()).thenReturn(10);
		when(this.self.getSupplyTotal()).thenReturn(20);

		when(this.unit.isExists()).thenReturn(true);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unit.getID()).thenReturn(1);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.getName()).thenReturn("type");
		when(this.unit.getHitPoints()).thenReturn(25);
		when(this.unit.getShields()).thenReturn(30);
		when(this.unit.getPosition()).thenReturn(new Position(2, 1, PosType.BUILD));
		when(this.unit.getEnergy()).thenReturn(100);
		when(this.unitType.getMaxEnergy()).thenReturn(110);
		BwapiUtility.clearCache(this.unit);

		this.perciever = new GenericUnitPerceiver(this.api, this.unit);
	}

	@Test
	public void size_test() {
		Map<PerceptFilter, List<Percept>> ret = new HashMap<>();
		when(this.race.getName()).thenReturn("race");
		when(this.self.getRace()).thenReturn(this.race);
		this.perciever.perceive(ret);
		assertEquals(3, ret.size());

		ret.clear();
		this.toReturn = new HashSet<>();
		when(this.api.getEnemies()).thenReturn(this.toReturn);
		this.perciever.perceive(ret);
		assertEquals(3, ret.size());

		ret.clear();
		when(this.unitType.getMaxEnergy()).thenReturn(0);
		when(this.unit.isDefenseMatrixed()).thenReturn(true);
		this.perciever.perceive(ret);
		assertEquals(4, ret.size());
	}

}
