package eisbw.percepts.perceivers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.iilang.Percept;

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
	private bwapi.Game api;
	@Mock
	private Player self;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.getRace()).thenReturn(Race.Terran);
		when(this.unit.getResourceGroup()).thenReturn(1);
		when(this.unit.getResources()).thenReturn(2);
		when(this.unit.getTilePosition()).thenReturn(new TilePosition(1, 2));

		when(this.unit3.getResourceGroup()).thenReturn(1);
		when(this.unit3.getResources()).thenReturn(2);
		when(this.unit3.getTilePosition()).thenReturn(new TilePosition(1, 2));

		this.units = new ArrayList<>(4);
		this.units.add(this.unit);
		this.units.add(this.unit4);
		this.units.add(this.unit3);
		this.units.add(this.unit2);

		when(this.self.getUnits()).thenReturn(this.units);
		when(this.api.getNeutralUnits()).thenReturn(this.units);

		when(this.unit2.isVisible()).thenReturn(false);
		when(this.unit3.isVisible()).thenReturn(true);
		when(this.unit4.isVisible()).thenReturn(true);
		when(this.unit3.getType()).thenReturn(this.unitType2);
		when(this.unit2.getType()).thenReturn(this.unitType2);
		when(this.unit4.getType()).thenReturn(this.unitType3);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unitType.toString()).thenReturn("Resource Mineral Field");
		when(this.unitType2.toString()).thenReturn("Resource Vespene Geyser");
		when(this.unitType3.toString()).thenReturn("No Resource");

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
		when(this.unitType.getRace()).thenReturn(Race.None);
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
		when(this.unitType.maxHitPoints()).thenReturn(1);
		Map<PerceptFilter, Set<Percept>> ret = new HashMap<>();
		assertFalse(this.perciever.perceive(ret).isEmpty());
		when(this.unit.getHitPoints()).thenReturn(1);
		assertFalse(this.perciever.perceive(ret).isEmpty());
	}

	@Test
	public void geyser_test() {
		List<Unit> units = new ArrayList<>(1);
		units.add(this.unit);
		when(this.api.getNeutralUnits()).thenReturn(units);
		when(this.unit.getType()).thenReturn(UnitType.Resource_Vespene_Geyser);
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
