package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
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

		when(this.api.self()).thenReturn(this.self);
		when(this.unit.getID()).thenReturn(1);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("name");
		when(this.unitType.spaceProvided()).thenReturn(1);
		List<UnitType> queue = new ArrayList<>(1);
		queue.add(null);
		when(this.unit.getTrainingQueue()).thenReturn(queue);
		when(this.unit.getUpgrade()).thenReturn(UpgradeType.Adrenal_Glands);
		when(this.unit.getLoadedUnits()).thenReturn(new ArrayList<Unit>(0));

		this.perciever = new BuildingPerceiver(this.api, this.unit);
	}

	// @Test FIXME (native call)
	public void size_test() {
		Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		this.perciever.perceive(toReturn);
		assertEquals(2, toReturn.size());

		toReturn.clear();
		when(this.unit.getUpgrade()).thenReturn(null);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());

		toReturn.clear();
		List<Unit> loadedunits = new LinkedList<>();
		loadedunits.add(this.unit);
		loadedunits.add(null);
		when(this.unit.getLoadedUnits()).thenReturn(loadedunits);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());

		toReturn.clear();
		when(this.unitType.spaceProvided()).thenReturn(0);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());

		toReturn.clear();
		when(this.self.hasResearched(any(TechType.class))).thenReturn(true);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());
	}
}
