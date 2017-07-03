package eisbw.units;

import java.util.LinkedList;
import java.util.List;

import bwapi.Race;
import bwapi.Unit;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;

/**
 * @author Danny & Harm - The condition perceiver.
 *
 */
public class ConditionHandler {
	protected final bwapi.Game api;
	protected final Unit unit;

	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The unit.
	 */
	public ConditionHandler(bwapi.Game api, Unit unit) {
		this.api = api;
		this.unit = unit;
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a terran unit.
	 */
	private void setTerranConditions(List<Parameter> conditions) {
		if (this.unit.isStimmed()) {
			conditions.add(new Identifier("stimmed"));
		}
		if (this.unit.isSieged()) {
			conditions.add(new Identifier("sieged"));
		}
		if (this.unit.isDefenseMatrixed()) {
			conditions.add(new Identifier("defenseMatrixed"));
		}
		// building-specific
		if (this.unit.isLifted()) {
			conditions.add(new Identifier("lifted"));
		}
		if (this.unit.getAddon() != null) {
			conditions.add(new Identifier(BwapiUtility.getName(this.unit.getAddon().getType())));
		}
		if (this.unit.hasNuke()) {
			conditions.add(new Identifier("nukeReady"));
		}
		// for vultures
		if (this.unit.getSpiderMineCount() > 0) {
			conditions.add(new Identifier("hasMines"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setProtossConditions(List<Parameter> conditions) {
		// for reavers
		if (this.unit.getScarabCount() > 0) {
			conditions.add(new Identifier("hasScarabs"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setZergConditions(List<Parameter> conditions) {
		if (this.unit.isMorphing()) {
			conditions.add(new Identifier("morphing"));
		}
		if (this.unit.isBurrowed()) {
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
		// caused by a Medic
		if (this.unit.isBlind()) {
			conditions.add(new Identifier("blinded"));
		}
		// caused by a Ghost
		if (this.unit.isLockedDown()) {
			conditions.add(new Identifier("lockDowned"));
		}
		// caused by a Science Vessel
		if (this.unit.isIrradiated()) {
			conditions.add(new Identifier("irradiated"));
		}
		// caused by medic heal or scv repair
		if (this.unit.isBeingHealed()) {
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
		// caused by a High Templar
		if (this.unit.isUnderStorm()) {
			conditions.add(new Identifier("underStorm"));
		}
		// caused by an Arbiter
		if (this.unit.isStasised()) {
			conditions.add(new Identifier("stasised"));
		}
		// caused by a Dark Archon
		if (this.unit.isMaelstrommed()) {
			conditions.add(new Identifier("maelstrommed"));
		}
		// caused by a Corsair
		if (this.unit.isUnderDisruptionWeb()) {
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
		// caused by a Queen
		if (this.unit.isEnsnared()) {
			conditions.add(new Identifier("ensnared"));
		}
		// caused by a Queen
		if (this.unit.isParasited()) {
			conditions.add(new Identifier("parasited"));
		}
		// caused by a Defiler
		if (this.unit.isPlagued()) {
			conditions.add(new Identifier("plagued"));
		}
		// caused by a Defiler
		if (this.unit.isUnderDarkSwarm()) {
			conditions.add(new Identifier("darkSwarmed"));
		}
		// caused by a Defiler
		if (this.unit.getAcidSporeCount() > 0) {
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
		if (this.unit.isMoving()) {
			conditions.add(new Identifier("moving"));
		}
		// if (unit.isStuck()) {
		// is generated quite a lot :(
		// conditions.add(new Identifier("stuck"));
		// }
		if (this.unit.isFollowing()) {
			conditions.add(new Identifier("following"));
		}
		if (this.unit.isPatrolling()) {
			conditions.add(new Identifier("patrolling"));
		}
		if (this.unit.isHoldingPosition()) {
			conditions.add(new Identifier("holding"));
		}
		if (this.unit.isLoaded()) {
			conditions.add(new Identifier("loaded"));
		}
	}

	/**
	 * @param conditions
	 *            The conditions of the unit
	 * @return The conditions of generic unit.
	 */
	private void setGenericConditions(List<Parameter> conditions) {
		if (this.unit.isIdle()) {
			conditions.add(new Identifier("idle"));
		}
		if (this.unit.getType().isFlyer()) { // useful shortcut
			conditions.add(new Identifier("flying"));
		}
		if (!this.unit.isCompleted()) { // isBeingConstructed can be false for
										// terran buildings not being worked on
			conditions.add(new Identifier("beingConstructed"));
		}
		if (this.unit.isCloaked()) {
			conditions.add(new Identifier("cloaked"));
		}
		if (this.unit.getPlayer() != null && !this.unit.getPlayer().equals(this.api.self()) && this.unit.isDetected()) {
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
		if (this.unit.isCarryingGas() || this.unit.isCarryingMinerals()) {
			conditions.add(new Identifier("carrying"));
		}
		if (this.unit.isGatheringGas() || this.unit.isGatheringMinerals()) {
			conditions.add(new Identifier("gathering"));
		}
		if (this.unit.isConstructing()) {
			conditions.add(new Identifier("constructing"));
		}
		if (this.unit.isRepairing()) {
			conditions.add(new Identifier("repairing"));
		}
	}

	/**
	 * @return A list of conditions of the unit.
	 */
	public List<Parameter> getConditions() {
		List<Parameter> conditions = new LinkedList<>();
		setGenericConditions(conditions);

		if (this.unit.getType().getRace() == Race.Terran) {
			setTerranConditions(conditions);
		} else if (this.unit.getType().getRace() == Race.Protoss) {
			setProtossConditions(conditions);
		} else if (this.unit.getType().getRace() == Race.Zerg) {
			setZergConditions(conditions);
		}

		if (this.unit.getType().isWorker()) {
			setWorkerConditions(conditions);
		}
		if (this.unit.getType().canMove()) {
			setMovingConditions(conditions);
			setAbilityConditions(conditions);
		}

		return conditions;
	}
}
