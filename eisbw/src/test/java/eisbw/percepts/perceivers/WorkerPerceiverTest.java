package eisbw.percepts.perceivers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

public class WorkerPerceiverTest {

	private WorkerPerceiver perciever;
	private List<Unit> units;
	@Mock
	private Unit unit;
	@Mock
	private Unit unit2;
	@Mock
	private Unit unit3;
	@Mock
	private Unit unit4;
	@Mock
	private UnitType unitType;
	@Mock
	private UnitType unitType2;
	@Mock
	private UnitType unitType3;
	@Mock
	private JNIBWAPI api;
	@Mock
	private Player self;
	private List<Unit> toreturn;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.getID()).thenReturn(RaceTypes.Terran.getID());
		when(this.api.getNeutralUnits()).thenReturn(new LinkedList<Unit>());
		this.toreturn = new LinkedList<>();
		this.toreturn.add(this.unit);
		when(this.api.getMyUnits()).thenReturn(this.toreturn);

		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unit.getResourceGroup()).thenReturn(1);
		when(this.unit.getResources()).thenReturn(2);
		when(this.unit.getPosition()).thenReturn(new Position(1, 2, PosType.BUILD));

		when(this.unit3.getResourceGroup()).thenReturn(1);
		when(this.unit3.getResources()).thenReturn(2);
		when(this.unit3.getPosition()).thenReturn(new Position(1, 2, PosType.BUILD));

		this.units = new LinkedList<>();
		this.units.add(this.unit);
		this.units.add(this.unit4);
		this.units.add(this.unit3);
		this.units.add(this.unit2);

		when(this.api.getMyUnits()).thenReturn(this.units);
		when(this.api.getNeutralUnits()).thenReturn(this.units);

		when(this.unit2.isVisible()).thenReturn(false);
		when(this.unit3.isVisible()).thenReturn(true);
		when(this.unit4.isVisible()).thenReturn(true);
		when(this.unit3.getType()).thenReturn(this.unitType2);
		when(this.unit2.getType()).thenReturn(this.unitType2);
		when(this.unit4.getType()).thenReturn(this.unitType3);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unitType.getName()).thenReturn("Resource Mineral Field");
		when(this.unitType2.getName()).thenReturn("Resource Vespene Geyser");
		when(this.unitType3.getName()).thenReturn("No Resource");

		this.perciever = new WorkerPerceiver(this.api, this.unit);
	}

	@Test
	public void gas_test() {
		when(this.unitType.isWorker()).thenReturn(true);
		when(this.unitType.isMechanical()).thenReturn(true);
		when(this.unit.isGatheringGas()).thenReturn(true);

		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void minerals_test() {
		when(this.unitType.isWorker()).thenReturn(true);
		when(this.unitType.isMechanical()).thenReturn(true);
		when(this.unit.isGatheringMinerals()).thenReturn(true);

		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void constructing_test() {
		when(this.unitType.isWorker()).thenReturn(true);
		when(this.unitType.isMechanical()).thenReturn(true);
		when(this.unit.isConstructing()).thenReturn(true);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void idle_test() {
		when(this.unitType.isWorker()).thenReturn(true);
		when(this.unitType.isMechanical()).thenReturn(true);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void noTerran_test() {
		when(this.unitType.getID()).thenReturn(RaceTypes.None.getID());
		when(this.unitType.isWorker()).thenReturn(true);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
		when(this.unitType.isWorker()).thenReturn(false);
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void repair_test() {
		when(this.unitType.isMechanical()).thenReturn(true);
		when(this.unit.getHitPoints()).thenReturn(0);
		when(this.unitType.getMaxHitPoints()).thenReturn(1);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
		when(this.unit.getHitPoints()).thenReturn(1);
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void geyser_test() {
		when(this.api.getNeutralUnits()).thenReturn(this.toreturn);
		when(this.unit.getType()).thenReturn(UnitType.UnitTypes.Resource_Vespene_Geyser);
		when(this.unit.getPosition()).thenReturn(new Position(1, 2));
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
		when(this.unit.getType()).thenReturn(this.unitType);
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void gathering_test() {
		when(this.unit.isGatheringMinerals()).thenReturn(true);
		when(this.unit.getOrderTarget()).thenReturn(this.unit);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

}
