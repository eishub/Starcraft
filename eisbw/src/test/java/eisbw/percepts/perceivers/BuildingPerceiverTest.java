package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
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
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import eis.iilang.Percept;

public class BuildingPerceiverTest {
	private BuildingPerceiver perciever;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
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

		when(this.unit.getRallyPosition()).thenReturn(new Position(1, 1));
		when(this.unit.getRallyUnit()).thenReturn(this.unit);
		when(this.unit.getID()).thenReturn(1);
		List<UnitType> queue = new ArrayList<>(1);
		queue.add(UnitType.Terran_Marine);
		when(this.unit.getTrainingQueue()).thenReturn(queue);
		when(this.unit.isUpgrading()).thenReturn(true);
		when(this.unit.getUpgrade()).thenReturn(UpgradeType.Adrenal_Glands);
		when(this.api.self()).thenReturn(this.self);
		when(this.self.hasResearched(any(TechType.class))).thenReturn(false);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("name");
		when(this.unitType.spaceProvided()).thenReturn(1);
		when(this.unit.getLoadedUnits()).thenReturn(new ArrayList<Unit>(0));

		this.perciever = new BuildingPerceiver(this.api, this.unit);
	}

	@Test
	public void size_test() {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		assertEquals(4, this.perciever.perceive(toReturn).size());
		when(this.unit.getRallyPosition()).thenReturn(Position.None);
		toReturn = new HashMap<>();
		assertEquals(3, this.perciever.perceive(toReturn).size());
		when(this.unit.getRallyUnit()).thenReturn(null);
		toReturn = new HashMap<>();
		assertEquals(2, this.perciever.perceive(toReturn).size());
		toReturn = new HashMap<>();
		when(this.unit.isUpgrading()).thenReturn(false);
		assertEquals(1, this.perciever.perceive(toReturn).size());
		toReturn = new HashMap<>();
		List<Unit> loadedunits = new ArrayList<>(2);
		loadedunits.add(this.unit);
		loadedunits.add(null);
		when(this.unit.getLoadedUnits()).thenReturn(loadedunits);
		assertEquals(1, this.perciever.perceive(toReturn).size());
		toReturn = new HashMap<>();
		when(this.unitType.spaceProvided()).thenReturn(0);
		assertEquals(1, this.perciever.perceive(toReturn).size());
		toReturn = new HashMap<>();
		when(this.self.hasResearched(any(TechType.class))).thenReturn(true);
		assertEquals(1, this.perciever.perceive(toReturn).size());
	}
}
