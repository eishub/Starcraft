package eisbw.units;

import java.util.LinkedList;
import java.util.List;

import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;

/**
 * @author Danny & Harm - The condition perceiver.
 */
public class ConditionHandler {
	protected final Unit unit;
	protected final JNIBWAPI api;

	/**
	 * @param api  The BWAPI.
	 * @param unit The unit.
	 */
	public ConditionHandler(final JNIBWAPI api, final Unit unit) {
		this.unit = unit;
		this.api = api;
	}

	/**
	 * @param conditions The conditions of the unit
	 * @return The conditions of a terran unit.
	 */
	private void setTerranConditions(final List<Parameter> conditions) {
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
			final UnitType addon = BwapiUtility.getType(this.unit.getAddon());
			conditions.add(new Identifier(addon.getName()));
		}
		if (this.unit.isNukeReady()) {
			conditions.add(new Identifier("nukeReady"));
		}
		// for vultures
		if (this.unit.getSpiderMineCount() > 0) {
			conditions.add(new Identifier("hasMines"));
		}
	}

	/**
	 * @param conditions The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setProtossConditions(final List<Parameter> conditions) {
		if (this.unit.isUnpowered()) {
			conditions.add(new Identifier("unpowered"));
		}
		// for Carriers
		if (this.unit.getInterceptorCount() > 0) {
			conditions.add(new Identifier("hasInterceptors"));
		}
		// for Reavers
		if (this.unit.getScarabCount() > 0) {
			conditions.add(new Identifier("hasScarabs"));
		}
		// for (friendly) hallucinations from High Templars
		if (this.unit.isHallucination()) {
			conditions.add(new Identifier("hallucination"));
		}
	}

	/**
	 * @param conditions The conditions of the unit
	 * @return The conditions of a protoss unit.
	 */
	private void setZergConditions(final List<Parameter> conditions) {
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
	 * @param conditions The conditions of the unit
	 */
	private void terranAbilityConditions(final List<Parameter> conditions) {
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
		// caused by Medic heal or SCV repair
		if (this.unit.isBeingHealed()) {
			conditions.add(new Identifier("beingHealed"));
		}
	}

	/**
	 * Sets all conditions caused by Protoss units.
	 *
	 * @param conditions The conditions of the unit
	 */
	private void protossAbilityConditions(final List<Parameter> conditions) {
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
	 * @param conditions The conditions of the unit
	 */
	private void zergAbilityConditions(final List<Parameter> conditions) {
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
	 * @param conditions The conditions of the unit
	 */
	private void setAbilityConditions(final List<Parameter> conditions) {
		terranAbilityConditions(conditions);
		protossAbilityConditions(conditions);
		zergAbilityConditions(conditions);
	}

	/**
	 * @param conditions The conditions of the unit
	 * @return The conditions of the (moving) unit.
	 */
	private void setMovingConditions(final List<Parameter> conditions) {
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
	 * @param conditions The conditions of the unit
	 * @return The conditions of generic unit.
	 */
	private void setGenericConditions(final List<Parameter> conditions, final UnitType type) {
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
		if (BwapiUtility.getPlayer(this.unit) != this.api.getSelf() && (this.unit.isCloaked() || this.unit.isBurrowed())
				&& this.unit.isDetected()) {
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
	 * @param conditions The conditions of the unit
	 * @return The conditions of the worker units.
	 */
	private void setWorkerConditions(final List<Parameter> conditions) {
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
		final List<Parameter> conditions = new LinkedList<>();
		final UnitType type = BwapiUtility.getType(this.unit);
		if (type == null) {
			return conditions;
		}

		setGenericConditions(conditions, type);

		if (type.getRaceID() == RaceTypes.Terran.getID()) {
			setTerranConditions(conditions);
		} else if (type.getRaceID() == RaceTypes.Protoss.getID()) {
			setProtossConditions(conditions);
		} else if (type.getRaceID() == RaceTypes.Zerg.getID()) {
			setZergConditions(conditions);
		}

		if (type.isWorker()) {
			setWorkerConditions(conditions);
		}
		if (type.isCanMove()) {
			setMovingConditions(conditions);
			setAbilityConditions(conditions);
		}

		return conditions;
	}
}
