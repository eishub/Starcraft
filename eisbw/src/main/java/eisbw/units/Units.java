package eisbw.units;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Unit;

import eisbw.BwapiUtility;
import eisbw.StarcraftEnvironmentImpl;

/**
 * @author Danny & Harm - The data class which keeps track of all the units.
 *
 */
public class Units {
	protected final StarcraftEnvironmentImpl environment;
	protected final Map<Integer, String> unitNames;
	protected final Map<String, PlayerUnit> unitMap;
	protected final Map<PlayerUnit, StarcraftUnit> starcraftUnits;
	protected final Queue<PlayerUnit> uninitializedUnits;

	/**
	 * Constructor.
	 *
	 * @param environment
	 *            - the SC environment
	 */
	public Units(StarcraftEnvironmentImpl environment) {
		this.environment = environment;
		this.unitNames = new ConcurrentHashMap<>();
		this.unitMap = new ConcurrentHashMap<>();
		this.starcraftUnits = new ConcurrentHashMap<>();
		this.uninitializedUnits = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Adds a unit to the game data.
	 *
	 * @param unit
	 *            The unit to add.
	 * @param factory
	 *            The object which creates all starcraft units.
	 */
	public void addUnit(PlayerUnit unit, StarcraftUnitFactory factory) {
		if (BwapiUtility.isValid(unit) /* && !unit.isInvincible() FIELD IS THERE BUT NO GETTER */) {
			String unitName = BwapiUtility.getName(unit);
			this.unitNames.put(unit.getId(), unitName);
			this.unitMap.put(unitName, unit);
			StarcraftUnit scUnit = factory.create(unit);
			this.starcraftUnits.put(unit, scUnit);
			this.uninitializedUnits.add(unit);
		}
	}

	/**
	 * Removes a unit from game data.
	 *
	 * @param unit
	 *            The unit to delete.
	 */
	public void deleteUnit(PlayerUnit unit) {
		String unitName = BwapiUtility.getName(unit);
		this.unitMap.remove(unitName);
		this.unitNames.remove(unit.getId());
		this.starcraftUnits.remove(unit);
		this.uninitializedUnits.remove(unit);

		this.environment.deleteFromEnvironment(unitName);
	}

	public String getUnitName(PlayerUnit unit) {
		return this.unitNames.get(unit.getId());
	}

	public PlayerUnit getUnit(String name) {
		return this.unitMap.get(name);
	}

	public StarcraftUnit getStarcraftUnit(Unit unit) {
		return this.starcraftUnits.get(unit);
	}

	public Queue<PlayerUnit> getUninitializedUnits() {
		return this.uninitializedUnits;
	}

	/**
	 * Clean units, let garbage collector remove the remains.
	 */
	public void clean() {
		for (PlayerUnit unit : this.unitMap.values()) {
			deleteUnit(unit);
		}
	}
}
