package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Player;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import eisbw.BwapiUtility;

public class ConditionHandlerTest {
	private ConditionHandler handler;

	@Mock
	private Unit unit;
	@Mock
	private bwapi.Game api;
	@Mock
	private UnitType unitType;
	@Mock
	private Player self;

	/**
	 * Init mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		BwapiUtility.clearValidCache();
		BwapiUtility.clearPlayerCache();

		when(this.self.getRace()).thenReturn(Race.None);

		when(this.unit.exists()).thenReturn(true);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unit.getType()).thenReturn(this.unitType);
		when(this.unitType.toString()).thenReturn("name");
		when(this.unitType.getRace()).thenReturn(Race.None);
		when(this.unit.getID()).thenReturn(0);
		BwapiUtility.clearCache(this.unit);

		this.handler = new ConditionHandler(this.api, this.unit);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void idle_test() {
		when(this.unit.isIdle()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		assertEquals("idle", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void completed_test() {
		when(this.unit.isCompleted()).thenReturn(false);
		assertEquals("beingConstructed", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void cloaked_test() {
		when(this.unit.isCloaked()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		assertEquals("cloaked", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void moving_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isMoving()).thenReturn(true);
		assertEquals("moving", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void following_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isFollowing()).thenReturn(true);
		assertEquals("following", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void loaded_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isLoaded()).thenReturn(true);
		assertEquals("loaded", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void stimmed_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unitType.getRace()).thenReturn(Race.Terran);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isStimmed()).thenReturn(true);
		assertEquals("stimmed", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void sieged_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unitType.getRace()).thenReturn(Race.Terran);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isSieged()).thenReturn(true);
		assertEquals("sieged", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void blinded_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isBlind()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		assertEquals("blinded", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void lockedDown_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isLockedDown()).thenReturn(true);
		assertEquals("lockDowned", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void irradiated_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isIrradiated()).thenReturn(true);
		assertEquals("irradiated", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void underStorm_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isUnderStorm()).thenReturn(true);
		assertEquals("underStorm", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void stasised_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isStasised()).thenReturn(true);
		assertEquals("stasised", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void maelstrommed_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isMaelstrommed()).thenReturn(true);
		assertEquals("maelstrommed", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void disruptionWebbed_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isUnderDisruptionWeb()).thenReturn(true);
		assertEquals("disruptionWebbed", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void ensnared_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isEnsnared()).thenReturn(true);
		assertEquals("ensnared", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void parasited_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isParasited()).thenReturn(true);
		assertEquals("parasited", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void plagued_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isPlagued()).thenReturn(true);
		assertEquals("plagued", this.handler.getConditions().get(0).toProlog());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void darkSwarmed_test() {
		when(this.unitType.canMove()).thenReturn(true);
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isUnderDarkSwarm()).thenReturn(true);
		assertEquals("darkSwarmed", this.handler.getConditions().get(0).toProlog());
	}

	@Test
	public void conditions_gas_test() {
		when(this.unit.isCarryingGas()).thenReturn(true);
		assertEquals(1, this.handler.getConditions().size());
	}

	@Test
	public void conditions_minerals_test() {
		when(this.unit.isCarryingMinerals()).thenReturn(true);
		assertEquals(1, this.handler.getConditions().size());
	}

	@Test
	public void conditions_constructing_test() {
		when(this.unit.isConstructing()).thenReturn(true);
		assertEquals(1, this.handler.getConditions().size());
	}

	@Test
	public void conditions_worker_test() {
		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unitType.isWorker()).thenReturn(true);
		when(this.unit.isCarryingGas()).thenReturn(true);
		when(this.unit.isConstructing()).thenReturn(true);
		assertEquals(2, this.handler.getConditions().size());

		when(this.unit.isCarryingGas()).thenReturn(false);
		when(this.unit.isCarryingMinerals()).thenReturn(true);
		when(this.unit.isConstructing()).thenReturn(false);
		assertEquals(1, this.handler.getConditions().size());
		when(this.unit.isCarryingMinerals()).thenReturn(false);
		assertEquals(0, this.handler.getConditions().size());
	}

	@Test
	public void conditions_building_test() {
		when(this.unitType.isBuilding()).thenReturn(true);
		when(this.unitType.getRace()).thenReturn(Race.Terran);

		when(this.unit.isCompleted()).thenReturn(true);
		when(this.unit.isLifted()).thenReturn(true);
		when(this.unit.getAddon()).thenReturn(this.unit);

		assertEquals(2, this.handler.getConditions().size());

		when(this.self.getRace()).thenReturn(Race.Terran);

		assertEquals(2, this.handler.getConditions().size());

		when(this.unit.isLifted()).thenReturn(false);
		when(this.unit.getAddon()).thenReturn(null);

		assertEquals(0, this.handler.getConditions().size());
	}
}
