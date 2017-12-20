package eisbw;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openbw.bwapi4j.Player;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Unit;

import bwta.BWTA;
import bwta.Region;

/**
 * @author Danny & Harm - The Utility class of the BWAPI.
 *
 */
public class BwapiUtility {
	private static final Map<String, UnitType> unitTypeMap = new HashMap<>();
	private static final Map<String, TechType> techTypeMap = new HashMap<>();
	private static final Map<String, UpgradeType> upgradeTypeMap = new HashMap<>();
	private static final Map<Integer, UnitType> typeCache = new HashMap<>();
	private static final Map<Integer, Player> playerCache = new HashMap<>();
	private static final Map<Point, Integer> regionCache = new HashMap<>();

	private BwapiUtility() {
		// Private constructor for static class.
	}

	public static boolean isValid(Unit unit) {
		return unit != null && unit.exists() && unit.isVisible();
	}

	/**
	 * Get the name of a unit.
	 *
	 * @param unit
	 *            - the unit that has to be named.
	 * @return the name of the unit.
	 */
	public static String getName(Unit unit) {
		String name = StringUtils.deleteWhitespace(getName(getType(unit)) + unit.getId());
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public static String getName(UnitType unittype) {
		if (unittype == null) {
			return "";
		} else if (unittype == UnitType.Terran_Siege_Tank_Tank_Mode
				|| unittype == UnitType.Terran_Siege_Tank_Siege_Mode) {
			return "Terran Siege Tank";
		} else {
			return unittype.toString();
		}
	}

	public static UnitType getType(Unit unit) {
		int id = unit.getId();
		UnitType type = typeCache.get(id);
		if (type == null && isValid(unit)) {
			type = unit.getInitialType(); // INITIAL?
			typeCache.put(id, type);
		}
		return type;
	}

	public static Player getPlayer(PlayerUnit unit) {
		int id = unit.getId();
		Player player = playerCache.get(id);
		if (player == null && isValid(unit)) {
			player = unit.getPlayer();
			playerCache.put(id, player);
		}
		return player;
	}

	public static int getRegion(TilePosition position, BWTA map) {
		Point point = new Point(position.getX(), position.getY());
		Integer regionId = regionCache.get(point);
		if (regionId == null) {
			Region region = (map == null) ? null : map.getRegion(position);
			regionId = (region == null) ? 0 : region.hashCode(); // TODO: hack (no getID?)
			regionCache.put(point, regionId);
		}
		return regionId.intValue();
	}

	public static void clearCache(Unit unit) {
		typeCache.remove(unit.getId());
		playerCache.remove(unit.getId());
	}

	/**
	 * Get the EIS unittype.
	 *
	 * @param unit
	 *            the unit
	 * @return the type of a unit.
	 */
	public static String getEisUnitType(Unit unit) {
		String result = StringUtils.deleteWhitespace(getName(getType(unit)));
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
			for (UnitType ut : UnitType.values()) {
				unitTypeMap.put(ut.toString(), ut);
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
			for (TechType tt : TechType.values()) {
				techTypeMap.put(tt.toString(), tt);
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
			for (UpgradeType tt : UpgradeType.values()) {
				upgradeTypeMap.put(tt.toString(), tt);
			}
		}
		if (type.length() > 2 && Character.isDigit(type.charAt(type.length() - 1))) {
			type = type.substring(0, type.length() - 2);
		}
		return upgradeTypeMap.get(type);
	}
}
