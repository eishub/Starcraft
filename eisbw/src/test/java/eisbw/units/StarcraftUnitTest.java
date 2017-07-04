package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Percept;
import eisbw.percepts.perceivers.IPerceiver;
import eisbw.percepts.perceivers.PerceptFilter;

public class StarcraftUnitTest {

	private StarcraftUnit unit;
	@Mock
	private IPerceiver perceiver;
	private java.util.Map<PerceptFilter, Set<Percept>> percepts;

	/**
	 * Initialize variables and mocks.
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void start() {
		MockitoAnnotations.initMocks(this);
		this.percepts = new HashMap<>();
		this.percepts.put(null, null);
		when(this.perceiver.perceive(any(java.util.Map.class))).thenReturn(this.percepts);
		List<IPerceiver> list = new LinkedList<>();
		list.add(this.perceiver);
		this.unit = new StarcraftUnit(list, false);
	}

	@Test
	public void test() {
		assertEquals(0, this.unit.perceive().size());
		assertFalse(this.unit.isWorker());
	}

}
