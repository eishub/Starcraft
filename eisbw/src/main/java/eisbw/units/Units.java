package eisbw.units;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import bwapi.Unit;
import eisbw.BwapiUtility;
import eisbw.StarcraftEnvironmentImpl;

/**
 * @author Danny & Harm - The data class which keeps track of all the units.
 *
 */
public class Units {
	protected final StarcraftEnvironmentImpl environment;
	protected final Map<Integer, String> unitNames;
	protected final Map<String, Unit> unitMap;
	protected final Map<Unit, StarcraftUnit> starcraftUnits;
	protected final Queue<Unit> uninitializedUnits;

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
	public void addUnit(Unit unit, StarcraftUnitFactory factory) {
		if (BwapiUtility.isValid(unit) && !unit.isInvincible()) {
			String unitName = BwapiUtility.getName(unit);
			this.unitNames.put(unit.getID(), unitName);
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
	public void deleteUnit(Unit unit) {
		String unitName = BwapiUtility.getName(unit);
		this.unitMap.remove(unitName);
		this.unitNames.remove(unit.getID());
		this.starcraftUnits.remove(unit);
		this.uninitializedUnits.remove(unit);

		this.environment.deleteFromEnvironment(unitName);
	}

	public String getUnitName(int id) {
		return this.unitNames.get(id);
	}

	public Unit getUnit(String name) {
		return this.unitMap.get(name);
	}

	public StarcraftUnit getStarcraftUnit(Unit unit) {
		return this.starcraftUnits.get(unit);
	}

	public Queue<Unit> getUninitializedUnits() {
		return this.uninitializedUnits;
	}

	/**
	 * Clean units, let garbage collector remove the remains.
	 */
	public void clean() {
		for (Unit unit : this.unitMap.values()) {
			deleteUnit(unit);
		}
	}
}
