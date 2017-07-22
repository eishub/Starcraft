package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eisbw.BwapiUtility;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class StarcraftUnitFactoryTest {
	private StarcraftUnitFactory factory;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;

	@Test
	public void test() {
		MockitoAnnotations.initMocks(this);
		this.factory = new StarcraftUnitFactory(null);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(this.unit);
		assertEquals(1, this.factory.create(this.unit).perceivers.size());
	}
}
