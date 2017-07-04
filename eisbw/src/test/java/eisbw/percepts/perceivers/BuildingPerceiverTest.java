package eisbw.percepts.perceivers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UpgradeType.UpgradeTypes;

public class BuildingPerceiverTest {
	private BuildingPerceiver perciever;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;
	@Mock
	private JNIBWAPI api;
	@Mock
	private Player self;

	/**
	 * Initialize mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		when(this.api.getSelf()).thenReturn(this.self);
		when(this.unit.getID()).thenReturn(1);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.getName()).thenReturn("name");
		when(this.unitType.getSpaceProvided()).thenReturn(1);
		when(this.unit.getTrainingQueueSize()).thenReturn(1);
		when(this.unit.getUpgrade()).thenReturn(UpgradeTypes.Adrenal_Glands);
		when(this.unit.getLoadedUnits()).thenReturn(new LinkedList<Unit>());

		this.perciever = new BuildingPerceiver(this.api, this.unit);
	}

	@Test
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
		when(this.unitType.getSpaceProvided()).thenReturn(0);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());

		toReturn.clear();
		when(this.self.isResearched(any(TechType.class))).thenReturn(true);
		this.perciever.perceive(toReturn);
		assertEquals(1, toReturn.size());
	}
}
