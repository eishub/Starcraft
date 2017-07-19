package eisbw;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jnibwapi.Position;
import jnibwapi.Region;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType;
import jnibwapi.types.UpgradeType.UpgradeTypes;

/**
 * @author Danny & Harm - The Utility class of the BWAPI.
 *
 */
public class BwapiUtility {
	private static final Map<String, UnitType> unitTypeMap = new HashMap<>();
	private static final Map<String, TechType> techTypeMap = new HashMap<>();
	private static final Map<String, UpgradeType> upgradeTypeMap = new HashMap<>();
	private static final Map<Entry<Integer, Integer>, Integer> regionCache = new HashMap<>();

	private BwapiUtility() {
		// Private constructor for static class.
	}

	public static boolean isValid(Unit unit) {
		return unit != null && unit.isExists() && unit.isVisible() && !(unit.isBeingConstructed() && unit.isLoaded());
	}

	/**
	 * Get the name of a unit.
	 *
	 * @param unit
	 *            - the unit that has to be named.
	 * @return the name of the unit.
	 */
	public static String getName(Unit unit) {
		String name = (getName(unit.getType()) + unit.getID()).replace(" ", "");
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public static String getName(UnitType unittype) {
		String type = unittype.getName();
		if (type.startsWith("Terran Siege Tank")) {
			return "Terran Siege Tank";
		} else {
			return type;
		}
	}

	public static int getRegion(Position position, jnibwapi.Map map) {
		Entry<Integer, Integer> pos = new SimpleEntry<>(position.getBX(), position.getBY());
		Integer regionId = regionCache.get(pos);
		if (regionId == null) {
			Region region = (map == null) ? null : map.getRegion(position);
			regionId = (region == null) ? 0 : region.getID();
			regionCache.put(pos, regionId);
		}
		return regionId.intValue();
	}

	/**
	 * Get the EIS unittype.
	 *
	 * @param unittype
	 *            the unittype
	 * @return the type of a unit.
	 */
	public static String getEisUnitType(UnitType type) {
		String result = getName(type).replace(" ", "");
		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	/**
	 * Convert EIS type to unit.
	 *
	 * @param type
	 *            - the type to be converted.
	 * @return the unit.
	 */
	public static UnitType getUnitType(String type) {
		if (type.equals("Terran Siege Tank")) {
			type += " Tank Mode";
		}
		if (unitTypeMap.isEmpty()) {
			for (UnitType ut : UnitTypes.getAllUnitTypes()) {
				unitTypeMap.put(ut.getName(), ut);
			}
		}
		return unitTypeMap.get(type);
	}

	/**
	 * Convert type string to a techtype.
	 *
	 * @param type
	 *            - the string to be converted.
	 * @return a techtype.
	 */
	public static TechType getTechType(String type) {
		if (techTypeMap.isEmpty()) {
			for (TechType tt : TechTypes.getAllTechTypes()) {
				techTypeMap.put(tt.getName(), tt);
			}
		}
		return techTypeMap.get(type);
	}

	/**
	 * Convert a string to a upgradetype.
	 *
	 * @param type
	 *            - the string to be converted.
	 * @return a upgradetype.
	 */
	public static UpgradeType getUpgradeType(String type) {
		if (upgradeTypeMap.isEmpty()) {
			for (UpgradeType tt : UpgradeTypes.getAllUpgradeTypes()) {
				upgradeTypeMap.put(tt.getName(), tt);
			}
		}
		if (type.length() > 2 && Character.isDigit(type.charAt(type.length() - 1))) {
			type = type.substring(0, type.length() - 2);
		}
		return upgradeTypeMap.get(type);
	}
}
