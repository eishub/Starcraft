package eisbw;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import jnibwapi.Position.PosType;
import jnibwapi.types.RaceType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.TechType;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.WeaponType;
import jnibwapi.types.WeaponType.WeaponTypes;

public class KnowledgeExport {

	public static void export() {
		String export = "";
		for (final UnitType type : UnitTypes.getAllUnitTypes()) {
			switch (type.getID()) {
			// TERRAN UNITS
			case 0: // marine
			case 1: // ghost
			case 2: // vulture
			case 3: // goliath
			case 4: // siege tank
			case 7: // scv
			case 8: // wraith
			case 9: // science vessel
			case 11: // dropship
			case 12: // battlecruiser
			case 13: // spider mine (given to vultures)
			case 32: // firebat
			case 34: // medic
			case 58: // valkyrie
				// ZERG UNITS
			case 35: // larva
			case 36: // egg
			case 37: // zergling
			case 38: // hydralisk
			case 39: // ultralisk
			case 41: // drone
			case 42: // overlord
			case 43: // mutalisk
			case 44: // guardian
			case 45: // queen
			case 46: // defiler
			case 47: // scourge
			case 59: // cocoon
			case 62: // devourer
			case 97: // lurker egg
			case 103: // lurker
				// PROTOSS UNITS
			case 60: // corsair
			case 61: // dark templar
			case 63: // dark archon
			case 64: // probe
			case 65: // zealot
			case 66: // dragoon
			case 67: // high templar
			case 68: // archon
			case 69: // shuttle
			case 70: // scout
			case 71: // arbiter
			case 72: // carrier
			case 73: // interceptor (produced by carriers)
			case 83: // reaver
			case 84: // observer
			case 85: // scarab (produced by reavers)
				// TERRAN BUILDINGS
			case 106: // command center
			case 107: // comsat station
			case 108: // nuclear silo
			case 109: // supply depot
			case 110: // refinery
			case 111: // barracks
			case 112: // academy
			case 113: // factory
			case 114: // startport
			case 115: // control tower
			case 116: // science facility
			case 117: // covert ops
			case 118: // physics lab
			case 120: // machine shop
			case 122: // engineering bay
			case 123: // armory
			case 124: // missile turret
			case 125: // bunker
				// ZERG BUILDINGS
			case 131: // hatchery
			case 132: // lair
			case 133: // hive
			case 134: // nydus canal
			case 135: // hydralisk den
			case 136: // defiler mound
			case 137: // greater spire
			case 138: // queens nest
			case 139: // evolution chamber
			case 140: // ultralisk cavern
			case 141: // spire
			case 142: // spawning pool
			case 143: // creep colony
			case 144: // spore colony
			case 146: // sunken colony
			case 149: // extractor
				// PROTOSS BUILDINGS
			case 154: // nexus
			case 155: // robotics facility
			case 156: // pylon
			case 157: // assimilator
			case 159: // observatory
			case 160: // gateway
			case 162: // photon cannon
			case 163: // citadel of adun
			case 164: // cybernetics core
			case 165: // templar archives
			case 166: // forge
			case 167: // stargate
			case 169: // fleet beacon
			case 170: // arbiter tribunal
			case 171: // robotics support bay
			case 172: // shield battery
				export += getUnitType(type) + "\n";
				export += getUnitCosts(type) + "\n";
				export += getUnitStats(type) + "\n";
				export += getUnitMetrics(type) + "\n";
				if (type.isAttackCapable()) {
					export += getUnitCombat(type) + "\n";
				}
				export += "\n";
				break;
			default:
				break;
			}
		}
		// TODO: costs for research types & tech types
		try {
			Files.write(Paths.get(new File("export.pl").toURI()), export.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getUnitType(UnitType type) {
		RaceType race = RaceTypes.getRaceType(type.getRaceID());
		return String.format("type(%s,'%s').", race.getName().toLowerCase(), BwapiUtility.getName(type));
	}

	private static String getUnitCosts(UnitType type) {
		String requirements = "[";
		boolean hadFirst = false;
		if (type.getRequiredTechID() <= 32) {
			TechType required = TechTypes.getTechType(type.getRequiredTechID());
			requirements += "'" + required.getName() + "'";
			hadFirst = true;
		}
		for (int requiredUnit : type.getRequiredUnits().keySet()) {
			UnitType required = UnitTypes.getUnitType(requiredUnit);
			for (int i = 0; i < type.getRequiredUnits().get(requiredUnit); ++i) {
				if (hadFirst) {
					requirements += ",";
				} else {
					hadFirst = true;
				}
				requirements += "'" + required.getName() + "'";
			}
		}
		requirements += "]";
		return String.format("costs('%s',%d,%d,%d,%d,%s).", BwapiUtility.getName(type), type.getMineralPrice(),
				type.getGasPrice(), type.getSupplyRequired() - type.getSupplyProvided(), type.getBuildTime(),
				requirements);
	}

	private static String getUnitStats(UnitType type) {
		List<String> conditionlist = new LinkedList<>();
		if (type.isBuilding()) {
			conditionlist.add("building");
		}
		if (type.isCanMove()) {
			conditionlist.add("canMove");
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
		if (type.isRequiresCreep()) {
			conditionlist.add("requiresCreep");
		}
		if (type.isRequiresPsi()) {
			conditionlist.add("requiresPsi");
		}
		if (type.isRobotic()) {
			conditionlist.add("robotic");
		}
		// TODO: add its abilities as can###
		// TODO: add canTarget### for its weapon/spells
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
		return String.format("stats('%s',%d,%d,%d,%d,%s).", BwapiUtility.getName(type), type.getMaxHitPoints(),
				type.getMaxShields(), type.getMaxEnergy(), (int) (type.getTopSpeed() * 10), conditions);
	}

	private static String getUnitMetrics(UnitType type) {
		int spaceRequired = (type.getSpaceRequired() >= 255) ? 0 : type.getSpaceRequired();
		int spaceProvided = (type.getSpaceProvided() >= 255) ? 0 : type.getSpaceProvided();
		return String.format("metrics('%s',%d,%d,%d,%d).", BwapiUtility.getName(type), type.getTileWidth(),
				type.getTileHeight(), type.getSightRange() / PosType.BUILD.scale, spaceRequired - spaceProvided);
	}

	private static String getUnitCombat(UnitType type) {
		WeaponType ground = type.getGroundWeapon();
		WeaponType air = WeaponTypes.getWeaponType(type.getAirWeaponID());
		WeaponType generic = (ground == null || ground.getID() == WeaponTypes.Unknown.getID()
				|| ground.getID() == WeaponTypes.None.getID()) ? air : ground;
		return String.format("combat('%s',%d,%d,%d,%d,%d).", BwapiUtility.getName(type),
				ground.getDamageAmount() * ground.getDamageFactor(), air.getDamageAmount() * air.getDamageFactor(),
				generic.getDamageCooldown(), generic.getMaxRange() / PosType.BUILD.scale,
				generic.getMedianSplashRadius() / PosType.BUILD.scale);
	}
}
