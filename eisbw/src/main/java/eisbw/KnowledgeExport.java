package eisbw;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WeaponType;

public class KnowledgeExport {

	public static void export() {
		String export = "";
		for (Field field : UnitType.class.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
				try {
					UnitType type = (UnitType) field.get(null);
					if ((type.getRace() == Race.Terran || type.getRace() == Race.Zerg || type.getRace() == Race.Protoss)
							&& !(type.isHero() || type.isPowerup() || type.isSpecialBuilding()
									|| type == UnitType.Terran_Siege_Tank_Siege_Mode
									|| type == UnitType.Special_Cargo_Ship
									|| type == UnitType.Special_Mercenary_Gunship)) {
						export += getUnitType(type);
						export += getUnitCosts(type);
						export += getUnitStats(type);
						export += getUnitMetrics(type);
						if (type.canAttack()) {
							export += getUnitCombat(type);
						}
						export += "\n";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Field field : TechType.class.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
				try {
					TechType type = (TechType) field.get(null);
					if (type != TechType.Unknown && type != TechType.None) {
						export += getTechType(type);
						export += getTechCosts(type);
						export += getTechCombat(type);
						export += "\n";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Field field : UpgradeType.class.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
				try {
					UpgradeType type = (UpgradeType) field.get(null);
					if (type != UpgradeType.Unknown && type != UpgradeType.None && type != UpgradeType.Upgrade_60) {
						export += getUpgradeType(type);
						export += getUpgradeCosts(type);
						export += "\n";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		try {
			Files.write(Paths.get(new File("export.pl").toURI()), export.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getUnitType(UnitType type) {
		return String.format("unit('%s',%s).\n", BwapiUtility.getName(type), getName(type.getRace()));
	}

	private static String getUnitCosts(UnitType type) {
		String requirements = "[";
		boolean hadFirst = false;
		if (type.requiredTech() != TechType.Unknown && type.requiredTech() != TechType.None) {
			requirements += "'" + getName(type.requiredTech()) + "'";
			hadFirst = true;
		}
		for (UnitType required : type.requiredUnits().keySet()) {
			if (required == UnitType.Unknown || required == UnitType.None) {
				continue;
			}
			for (int i = 0; i < type.requiredUnits().get(required); ++i) {
				if (hadFirst) {
					requirements += ",";
				} else {
					hadFirst = true;
				}
				requirements += "'" + BwapiUtility.getName(required) + "'";
			}
		}
		requirements += "]";
		return String.format("costs('%s',%d,%d,%d,%d,%s).\n", BwapiUtility.getName(type), type.mineralPrice(),
				type.gasPrice(), type.supplyRequired() - type.supplyProvided(), type.buildTime(), requirements);
	}

	private static String getUnitStats(UnitType type) {
		List<String> conditionlist = new LinkedList<>();
		if (type.isBuilding()) {
			conditionlist.add("building");
		}
		if (type.canMove()) {
			conditionlist.add("canMove");
		}
		if (type.isFlyingBuilding()) {
			conditionlist.add("canLift");
		}
		if (type.isBurrowable()) {
			conditionlist.add("canBurrow");
		}
		if (type.isDetector()) {
			conditionlist.add("canDetect");
		}
		if (type.isFlyer()) {
			conditionlist.add("flies");
		}
		if (type.isMechanical()) {
			conditionlist.add("mechanical");
		}
		if (type.isOrganic()) {
			conditionlist.add("organic");
		}
		if (type.requiresCreep()) {
			conditionlist.add("requiresCreep");
		}
		if (type.requiresPsi()) {
			conditionlist.add("requiresPsi");
		}
		if (type.isRobotic()) {
			conditionlist.add("robotic");
		}
		if (type.isAddon()) {
			conditionlist.add("addon");
		}
		if (type.isSpell()) {
			conditionlist.add("spell");
		}
		if (type.canProduce() || type == UnitType.Terran_Nuclear_Silo) {
			conditionlist.add("canTrain");
		}
		Collections.sort(conditionlist);
		String conditions = "[";
		boolean hadFirst = false;
		for (String condition : conditionlist) {
			if (hadFirst) {
				conditions += ",";
			} else {
				hadFirst = true;
			}
			conditions += "'" + condition + "'";
		}
		conditions += "]";
		return String.format("stats('%s',%d,%d,%d,%d,%s).\n", BwapiUtility.getName(type),
				type.isInvincible() ? 0 : type.maxHitPoints(), type.isInvincible() ? 0 : type.maxShields(),
				type.maxEnergy(), (int) (type.topSpeed() * 10), conditions);
	}

	private static String getUnitMetrics(UnitType type) {
		int spaceRequired = (type.spaceRequired() >= 255) ? 0 : type.spaceRequired();
		int spaceProvided = (type.spaceProvided() >= 255) ? 0 : type.spaceProvided();
		return String.format("metrics('%s',%d,%d,%d,%d).\n", BwapiUtility.getName(type), type.tileWidth(),
				type.tileHeight(), type.sightRange() / TilePosition.SIZE_IN_PIXELS, spaceRequired - spaceProvided);
	}

	private static String getUnitCombat(UnitType type) {
		WeaponType ground = type.groundWeapon();
		WeaponType air = type.airWeapon();
		WeaponType generic = (ground == null || ground == WeaponType.Unknown || ground == WeaponType.None) ? air
				: ground;
		return String.format("combat('%s',%d,%d,%d,%d,%d).\n", BwapiUtility.getName(type),
				ground.damageAmount() * ground.damageFactor(), air.damageAmount() * air.damageFactor(),
				generic.damageCooldown(), generic.maxRange() / TilePosition.SIZE_IN_PIXELS,
				generic.medianSplashRadius() / TilePosition.SIZE_IN_PIXELS);
	}

	private static String getTechType(TechType type) {
		return String.format("upgrade('%s',%s).\n", getName(type), getName(type.getRace()));
	}

	private static String getTechCosts(TechType type) {
		UnitType researches = type.whatResearches();
		String required = (researches == UnitType.Unknown || researches == UnitType.None) ? ""
				: ("'" + BwapiUtility.getName(researches) + "'");
		return String.format("costs('%s',%d,%d,%d,%d,%s).\n", getName(type), type.mineralPrice(), type.gasPrice(),
				type.energyCost(), type.researchTime(), "[" + required + "]");
	}

	private static String getTechCombat(TechType type) {
		WeaponType weapon = type.getWeapon();
		if (weapon == WeaponType.Unknown || weapon == WeaponType.None) {
			return "";
		} else {
			return String.format("combat('%s',%d,%d,%d,%d,%d).\n", getName(type),
					weapon.targetsGround() ? (weapon.damageAmount() * weapon.damageFactor()) : 0,
					weapon.targetsAir() ? (weapon.damageAmount() * weapon.damageFactor()) : 0, weapon.damageCooldown(),
					weapon.maxRange() / TilePosition.SIZE_IN_PIXELS,
					weapon.medianSplashRadius() / TilePosition.SIZE_IN_PIXELS);
		}
	}

	private static String getUpgradeType(UpgradeType type) {
		if (type.maxRepeats() > 1) {
			String returned = "";
			for (int i = 1; i <= type.maxRepeats(); ++i) {
				returned += String.format("upgrade('%s',%s).\n", getName(type) + " " + i, getName(type.getRace()));
			}
			return returned;
		} else {
			return String.format("upgrade('%s',%s).\n", getName(type), getName(type.getRace()));
		}
	}

	private static String getUpgradeCosts(UpgradeType type) {
		UnitType upgrades = type.whatUpgrades();
		String required = (upgrades == UnitType.Unknown || upgrades == UnitType.None) ? ""
				: ("'" + BwapiUtility.getName(upgrades) + "'");
		String returned = "";
		for (int i = 1; i <= type.maxRepeats(); ++i) {
			String toAdd = (type.maxRepeats() == 1) ? "" : (" " + i);
			returned += String.format("costs('%s',%d,%d,%d,%d,%s).\n", getName(type) + toAdd,
					type.mineralPrice() + (type.mineralPriceFactor() * i),
					type.gasPrice() + (type.gasPriceFactor() * i), 0,
					type.upgradeTime() + (type.upgradeTimeFactor() * i), "[" + required + "]");
		}
		return returned;
	}

	private static String getName(Race race) {
		return race.toString().toLowerCase();
	}

	private static String getName(TechType type) {
		return type.toString().replace("_", " ");
	}

	private static String getName(UpgradeType type) {
		return type.toString().replace("_", " ");
	}
}
