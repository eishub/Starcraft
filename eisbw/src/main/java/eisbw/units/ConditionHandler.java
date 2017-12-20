package eisbw.units;

import java.util.LinkedList;
import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.Burrowable;
import org.openbw.bwapi4j.unit.CommandCenter;
import org.openbw.bwapi4j.unit.Drone;
import org.openbw.bwapi4j.unit.Factory;
import org.openbw.bwapi4j.unit.Firebat;
import org.openbw.bwapi4j.unit.Marine;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.NuclearSilo;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Probe;
import org.openbw.bwapi4j.unit.Reaver;
import org.openbw.bwapi4j.unit.SCV;
import org.openbw.bwapi4j.unit.ScienceFacility;
import org.openbw.bwapi4j.unit.SiegeTank;
import org.openbw.bwapi4j.unit.Starport;
import org.openbw.bwapi4j.unit.Vulture;

import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;

/**
 * @author Danny & Harm - The condition perceiver.
 *
 */
public class ConditionHandler {
	protected final BW api;
	protected final PlayerUnit unit;

	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The unit.
	 */
	public ConditionHandler(BW api, PlayerUnit unit) {
		this.api = api;
		this.unit = unit;
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a terran unit.
	 */
	private void setTerranConditions(List<Parameter> conditions) {
		if (this.unit instanceof Firebat && ((Firebat) this.unit).isStimmed()) {
			conditions.add(new Identifier("stimmed"));
		}
		if (this.unit instanceof Marine && ((Marine) this.unit).isStimmed()) {
			conditions.add(new Identifier("stimmed"));
		}
		if (this.unit instanceof SiegeTank && ((SiegeTank) this.unit).isSieged()) {
			conditions.add(new Identifier("sieged"));
		}
		// building-specific
		// if (this.unit instanceof FlyingBuilding && ((FlyingBuilding)
		// this.unit).isLifted()) {
		// FIXME: FlyingBuilding is private in lib atm.
		// conditions.add(new Identifier("lifted"));
		// }
		if (this.unit instanceof CommandCenter && ((CommandCenter) this.unit).getComsatStation() != null) {
			conditions.add(new Identifier(UnitType.Terran_Comsat_Station.toString()));
		}
		if (this.unit instanceof CommandCenter && ((CommandCenter) this.unit).getNuclearSilo() != null) {
			conditions.add(new Identifier(UnitType.Terran_Nuclear_Silo.toString()));
		}
		if (this.unit instanceof Factory && ((Factory) this.unit).getMachineShop() != null) {
			conditions.add(new Identifier(UnitType.Terran_Machine_Shop.toString()));
		}
		if (this.unit instanceof Starport && ((Starport) this.unit).getControlTower() != null) {
			conditions.add(new Identifier(UnitType.Terran_Control_Tower.toString()));
		}
		if (this.unit instanceof ScienceFacility && ((ScienceFacility) this.unit).getCovertOps() != null) {
			conditions.add(new Identifier(UnitType.Terran_Covert_Ops.toString()));
		}
		if (this.unit instanceof ScienceFacility && ((ScienceFacility) this.unit).getPhysicsLab() != null) {
			conditions.add(new Identifier(UnitType.Terran_Physics_Lab.toString()));
		}
		if (this.unit instanceof NuclearSilo && ((NuclearSilo) this.unit).hasNuke()) {
			conditions.add(new Identifier("nukeReady"));
		}
		// for vultures
		if (this.unit instanceof Vulture && ((Vulture) this.unit).getSpiderMineCount() > 0) {
			conditions.add(new Identifier("hasMines"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setProtossConditions(List<Parameter> conditions) {
		if (!this.unit.isPowered()) {
			conditions.add(new Identifier("unpowered"));
		}
		// for Reavers
		if (this.unit instanceof Reaver && ((Reaver) this.unit).getScarabCount() > 0) {
			conditions.add(new Identifier("hasScarabs"));
		}
		// for (friendly) hallucinations from High Templars
		if (this.unit instanceof MobileUnit && ((MobileUnit) this.unit).isHallucination()) {
			conditions.add(new Identifier("hallucination"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setZergConditions(List<Parameter> conditions) {
		// if (this.unit.isMorphing()) {
		// conditions.add(new Identifier("morphing"));
		// FIXME: not supported in lib atm. }
		if (this.unit instanceof Burrowable && ((Burrowable) this.unit).isBurrowed()) {
			conditions.add(new Identifier("burrowed"));
		}
	}

	/**
	 * Sets all conditions caused by Terran units.
	 *
	 * @param conditions
	 *            The conditions of the unit
	 */
	private void terranAbilityConditions(List<Parameter> conditions) {
		MobileUnit unit = (MobileUnit) this.unit;
		// caused by a Medic
		if (unit.isBlind()) {
			conditions.add(new Identifier("blinded"));
		}
		// caused by a Ghost
		if (unit.isLockedDown()) {
			conditions.add(new Identifier("lockDowned"));
		}
		// caused by a Science Vessel
		if (unit.isIrradiated()) {
			conditions.add(new Identifier("irradiated"));
		}
		// caused by a Science Vessel
		if (unit.isDefenseMatrixed()) {
			conditions.add(new Identifier("defenseMatrixed"));
		}
		// caused by Medic heal or SCV repair
		if (unit.isBeingHealed()) {
			conditions.add(new Identifier("beingHealed"));
		}
	}

	/**
	 * Sets all conditions caused by Protoss units.
	 *
	 * @param conditions
	 *            The conditions of the unit
	 */
	private void protossAbilityConditions(List<Parameter> conditions) {
		MobileUnit unit = (MobileUnit) this.unit;
		// caused by a High Templar
		if (unit.isUnderStorm()) {
			conditions.add(new Identifier("underStorm"));
		}
		// caused by an Arbiter
		if (unit.isStasised()) {
			conditions.add(new Identifier("stasised"));
		}
		// caused by a Dark Archon
		if (unit.isMaelstrommed()) {
			conditions.add(new Identifier("maelstrommed"));
		}
		// caused by a Corsair
		if (unit.isUnderDisruptionWeb()) {
			conditions.add(new Identifier("disruptionWebbed"));
		}
	}

	/**
	 * Sets all conditions caused by Zerg units.
	 *
	 * @param conditions
	 *            The conditions of the unit
	 */
	private void zergAbilityConditions(List<Parameter> conditions) {
		MobileUnit unit = (MobileUnit) this.unit;
		// caused by a Queen
		if (unit.isEnsnared()) {
			conditions.add(new Identifier("ensnared"));
		}
		// caused by a Queen
		if (unit.isParasited()) {
			conditions.add(new Identifier("parasited"));
		}
		// caused by a Defiler
		if (unit.isPlagued()) {
			conditions.add(new Identifier("plagued"));
		}
		// caused by a Defiler
		if (unit.isUnderDarkSwarm()) {
			conditions.add(new Identifier("darkSwarmed"));
		}
		// caused by a Defiler
		if (unit.getAcidSporeCount() > 0) {
			conditions.add(new Identifier("acidSpored"));
		}
	}

	/**
	 * Sets all the conditions caused by abilities.
	 *
	 * @param conditions
	 *            The conditions of the unit
	 */
	private void setAbilityConditions(List<Parameter> conditions) {
		terranAbilityConditions(conditions);
		protossAbilityConditions(conditions);
		zergAbilityConditions(conditions);
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of the (moving) unit.
	 */
	private void setMovingConditions(List<Parameter> conditions) {
		MobileUnit unit = (MobileUnit) this.unit;
		if (unit.isMoving()) {
			conditions.add(new Identifier("moving"));
		}
		// if (unit.isStuck()) {
		// is generated quite a lot :(
		// conditions.add(new Identifier("stuck"));
		// }
		if (unit.isFollowing()) {
			conditions.add(new Identifier("following"));
		}
		if (unit.isPatrolling()) {
			conditions.add(new Identifier("patrolling"));
		}
		if (unit.isHoldingPosition()) {
			conditions.add(new Identifier("holding"));
		}
		// if (unit.isLoaded()) { FIXME: no supported by lib atm.
		// conditions.add(new Identifier("loaded"));
		// }
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of generic unit.
	 */
	private void setGenericConditions(List<Parameter> conditions, UnitType type) {
		if (this.unit.isIdle()) {
			conditions.add(new Identifier("idle"));
		}
		if (type.isFlyer()) { // useful shortcut
			conditions.add(new Identifier("flying"));
		}
		if (!this.unit.isCompleted()) { // isBeingConstructed can be false for
										// terran buildings not being worked on
			conditions.add(new Identifier("beingConstructed"));
		}
		if (this.unit.isCloaked()) {
			conditions.add(new Identifier("cloaked"));
		}
		if (BwapiUtility.getPlayer(this.unit) != this.api.getInteractionHandler().self() && this.unit.isDetected()) {
			conditions.add(new Identifier("detected"));
		}
		if (this.unit.isAttacking()) { // includes medic heal
			conditions.add(new Identifier("attacking"));
		}
		if (this.unit.isUnderAttack()) {
			conditions.add(new Identifier("underAttack"));
		}
		// if (this.unit.isStartingAttack()) {
		// conditions.add(new Identifier("startingAttack"));
		// }
		if (this.unit.getAirWeaponCooldown() > 0 || this.unit.getGroundWeaponCooldown() > 0
				|| this.unit.getSpellCooldown() > 0) {
			conditions.add(new Identifier("coolingDown"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of the worker units.
	 */
	private void setWorkerConditions(List<Parameter> conditions) {
		if (this.unit instanceof Drone) {
			Drone unit = (Drone) this.unit;
			if (unit.isCarryingGas() || unit.isCarryingMinerals()) {
				conditions.add(new Identifier("carrying"));
			}
			if (unit.isGatheringGas() || unit.isGatheringMinerals()) {
				conditions.add(new Identifier("gathering"));
			}
		} else if (this.unit instanceof Probe) {
			Probe unit = (Probe) this.unit;
			if (unit.isCarryingGas() || unit.isCarryingMinerals()) {
				conditions.add(new Identifier("carrying"));
			}
			if (unit.isGatheringGas() || unit.isGatheringMinerals()) {
				conditions.add(new Identifier("gathering"));
			}
		} else if (this.unit instanceof SCV) {
			SCV unit = (SCV) this.unit;
			if (unit.isCarryingGas() || unit.isCarryingMinerals()) {
				conditions.add(new Identifier("carrying"));
			}
			if (unit.isGatheringGas() || unit.isGatheringMinerals()) {
				conditions.add(new Identifier("gathering"));
			}
			if (unit.isConstructing()) {
				conditions.add(new Identifier("constructing"));
			}
			if (unit.isRepairing()) {
				conditions.add(new Identifier("repairing"));
			}
		}
	}

	/**
	 * @return A list of conditions of the unit.
	 */
	public List<Parameter> getConditions() {
		List<Parameter> conditions = new LinkedList<>();
		UnitType type = BwapiUtility.getType(this.unit);
		if (type == null) {
			return conditions;
		}

		setGenericConditions(conditions, type);

		if (type.getRace() == Race.Terran) {
			setTerranConditions(conditions);
		} else if (type.getRace() == Race.Protoss) {
			setProtossConditions(conditions);
		} else if (type.getRace() == Race.Zerg) {
			setZergConditions(conditions);
		}

		if (type.isWorker()) {
			setWorkerConditions(conditions);
		}
		if (type.canMove()) {
			setMovingConditions(conditions);
			setAbilityConditions(conditions);
		}

		return conditions;
	}
}
