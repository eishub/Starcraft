package eisbw.units;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import eisbw.BwapiUtility;
import eisbw.StarcraftEnvironmentImpl;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - The data class which keeps track of all the units.
 *
 */
public class Units {
	protected final Map<String, Unit> unitMap;
	protected final Map<Integer, String> unitNames;
	protected final Map<String, StarcraftUnit> starcraftUnits;
	protected final StarcraftEnvironmentImpl environment;
	protected final Queue<Unit> uninitializedUnits;

	/**
	 * Constructor.
	 *
	 * @param environment
	 *            - the SC environment
	 */
	public Units(StarcraftEnvironmentImpl environment) {
		this.unitMap = new ConcurrentHashMap<>();
		this.unitNames = new ConcurrentHashMap<>();
		this.starcraftUnits = new ConcurrentHashMap<>();
		this.uninitializedUnits = new ConcurrentLinkedQueue<>();
		this.environment = environment;
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
		String unitName = BwapiUtility.getName(unit);
		this.unitMap.put(unitName, unit);
		this.unitNames.put(unit.getID(), unitName);
		this.starcraftUnits.put(unitName, factory.create(unit));
		this.uninitializedUnits.add(unit);
	}

	/**
	 * Removes a unit from game data.
	 *
	 * @param unitName
	 *            The unit name.
	 * @param id
	 *            The id of the unit.
	 */
	public Unit deleteUnit(String unitName, int id) {
		Unit unit = this.unitMap.remove(unitName);
		this.unitNames.remove(id);
		this.starcraftUnits.remove(unitName);
		this.uninitializedUnits.remove(unit);
		this.environment.deleteFromEnvironment(unitName);
		return unit;
	}

	public Map<String, Unit> getUnits() {
		return Collections.unmodifiableMap(this.unitMap);
	}

	public Map<Integer, String> getUnitNames() {
		return Collections.unmodifiableMap(this.unitNames);
	}

	public Map<String, StarcraftUnit> getStarcraftUnits() {
		return Collections.unmodifiableMap(this.starcraftUnits);
	}

	/**
	 * Clean units, let garbage collector remove the remains.
	 */
	public void clean() {
		for (Entry<Integer, String> entry : this.unitNames.entrySet()) {
			deleteUnit(entry.getValue(), entry.getKey());
		}
	}

	public Queue<Unit> getUninitializedUnits() {
		return this.uninitializedUnits;
	}
}
