package eisbw;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.Region;

/**
 * @author Danny & Harm - The Utility class of the BWAPI.
 *
 */
public class BwapiUtility {
	private static final Map<String, UnitType> unitTypeMap = new HashMap<>();
	private static final Map<String, TechType> techTypeMap = new HashMap<>();
	private static final Map<String, UpgradeType> upgradeTypeMap = new HashMap<>();

	private BwapiUtility() {
		// Private constructor for static class.
	}

	public static boolean isValid(Unit unit) {
		return unit.exists() && unit.isVisible() && !(unit.isBeingConstructed() && unit.isLoaded());
	}

	/**
	 * Get the name of a unit.
	 *
	 * @param unit
	 *            - the unit that has to be named.
	 * @return the name of the unit.
	 */
	public static String getName(Unit unit) {
		String name = (getName(unit.getType()) + unit.getID()).replace("_", "").replace(" ", "");
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public static String getName(UnitType unittype) {
		String type = unittype.toString().replace("_", " ");
		if (type.startsWith("Terran Siege Tank")) {
			return "Terran Siege Tank";
		} else {
			return type;
		}
	}

	/**
	 * Get the EIS unittype of a unit.
	 *
	 * @param unit
	 *            - the unit that you want the Type from.
	 * @return the type of a unit.
	 */
	public static String getEisUnitType(Unit unit) {
		String type = unit.getType().toString().replace("_", "");
		return type.substring(0, 1).toLowerCase() + type.substring(1);
	}

	/**
	 * Convert EIS type to unit.
	 *
	 * @param type
	 *            - the type to be converted.
	 * @return the unit.
	 */
	public static UnitType getUnitType(String type) {
		if (unitTypeMap.isEmpty()) {
			for (Field field : UnitType.class.getDeclaredFields()) {
				if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
					try {
						UnitType ut = (UnitType) field.get(null);
						unitTypeMap.put(ut.toString().replace("_", " "), ut);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace(); // TODO Auto-generated
					}
				}
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
			for (Field field : TechType.class.getDeclaredFields()) {
				if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
					try {
						TechType tt = (TechType) field.get(null);
						techTypeMap.put(tt.toString().replace("_", " "), tt);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace(); // TODO Auto-generated
					}
				}
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
			for (Field field : UpgradeType.class.getDeclaredFields()) {
				if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
					try {
						UpgradeType ut = (UpgradeType) field.get(null);
						upgradeTypeMap.put(ut.toString().replace("_", " "), ut);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace(); // TODO Auto-generated
					}
				}
			}
		}

		return upgradeTypeMap.get(type);
	}

	public static int getRegionId(Region region, bwapi.Game api) {
		return api.getRegionAt(region.getCenter()).getID();
	}
}
