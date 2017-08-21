package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eisbw.percepts.perceivers.IPerceiver;
import jnibwapi.types.UnitType;

public class StarcraftUnitTest {
	private StarcraftUnit unit;

	@Mock
	private IPerceiver perceiver;
	@Mock
	private UnitType type;

	/**
	 * Initialize variables and mocks.
	 */
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);

		List<IPerceiver> list = new ArrayList<>(1);
		list.add(this.perceiver);
		this.unit = new StarcraftUnit(list, this.type);
	}

	@Test
	public void test() {
		assertEquals(0, this.unit.perceive().size());
		assertFalse(this.unit.isWorker());
	}
}
