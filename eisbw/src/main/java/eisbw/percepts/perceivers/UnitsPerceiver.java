package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.Player;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.GasMiningFacility;
import org.openbw.bwapi4j.unit.MineralPatch;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.SpellCaster;
import org.openbw.bwapi4j.unit.Unit;
import org.openbw.bwapi4j.unit.VespeneGeyser;

import bwta.BWTA;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackingPercept;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FramePercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResourcesPercept;
import eisbw.percepts.UnderConstructionPercept;
import eisbw.percepts.VespeneGeyserPercept;
import eisbw.units.ConditionHandler;

/**
 * @author Danny & Harm - The perceiver which handles all the unit percepts.
 *
 */
public class UnitsPerceiver extends Perceiver {
	private final int frame;
	private final Map<Integer, EnemyPercept> enemies;

	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(BW bwapi, BWTA bwta, Map<Integer, EnemyPercept> enemies) {
		super(bwapi, bwta);
		this.frame = bwapi.getInteractionHandler().getFrameCount();
		this.enemies = enemies;
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		framePercept(toReturn);
		resourcesPercepts(toReturn);
		unitsPercepts(toReturn);
	}

	private void framePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> framepercept = new ArrayList<>(1);
		framepercept.add(new FramePercept(this.frame));
		toReturn.put(new PerceptFilter(Percepts.FRAME, Filter.Type.ON_CHANGE), framepercept);
	}

	private void resourcesPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		Player self = this.bwapi.getInteractionHandler().self();
		if (self != null) { // for tests
			List<Percept> resourcePercept = new ArrayList<>(1);
			resourcePercept.add(new ResourcesPercept(self.gatheredMinerals(), self.gatheredGas(), self.supplyUsed(),
					self.supplyTotal()));
			toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);
		}
		List<Percept> minerals = new LinkedList<>();
		List<Percept> geysers = new LinkedList<>();
		for (Unit u : this.bwapi.getAllUnits()) {
			if (u instanceof MineralPatch) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(((MineralPatch) u).getResources() / 100.0);
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getId(), (int) amount, pos.getX(),
						pos.getY(), getRegion(u));
				minerals.add(mineralfield);
			} else if (u instanceof VespeneGeyser) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(((VespeneGeyser) u).getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getId(), (int) amount, pos.getX(), pos.getY(),
						getRegion(u));
				geysers.add(geyser);
			} else if (u instanceof GasMiningFacility) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(((GasMiningFacility) u).getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getId(), (int) amount, pos.getX(), pos.getY(),
						getRegion(u));
				geysers.add(geyser);
			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}

	private void unitsPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> newunitpercepts = new LinkedList<>();
		List<Percept> friendlypercepts = new LinkedList<>();
		List<Percept> enemypercepts = new LinkedList<>();
		List<Percept> attackingpercepts = new LinkedList<>();

		// perceive friendly units
		setUnitPercepts(this.bwapi.getUnits(this.bwapi.getInteractionHandler().self()), newunitpercepts,
				friendlypercepts, attackingpercepts);
		// perceive enemy units
		setUnitPercepts(this.bwapi.getUnits(this.bwapi.getInteractionHandler().enemy()), null, enemypercepts,
				attackingpercepts);

		if (!friendlypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		}
		if (!enemypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		}
		if (!attackingpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		}
		if (!newunitpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNDERCONSTRUCTION, Filter.Type.ALWAYS), newunitpercepts);
		}
	}

	/**
	 * @param u
	 * @return The region for the given unit (from a cache if it was seen before).
	 */
	private int getRegion(Unit u) {
		return BwapiUtility.getRegion(u.getTilePosition(), this.bwta);
	}

	/**
	 * Sets some of the generic Unit percepts.
	 *
	 * @param units
	 *            The perceived units
	 * @param newunitpercepts
	 *            - list with newUnitPercepts; if this is passed (not null) we
	 *            assume we want friendly units in unitpercepts
	 * @param unitpercepts
	 *            - list with unitPercepts
	 * @param attackingpercepts
	 *            - list with attackingPercepts
	 * @param percepts
	 *            The list of percepts
	 * @param toReturn
	 *            - the map that will be returned
	 */
	private void setUnitPercepts(List<PlayerUnit> units, List<Percept> newunitpercepts, List<Percept> unitpercepts,
			List<Percept> attackingpercepts) {
		for (PlayerUnit u : units) {
			UnitType type = BwapiUtility.getType(u);
			if (type == null) {
				continue;
			}
			if (newunitpercepts != null) { // friendly
				String unittype = BwapiUtility.getName(type); // TODO: support zerg eggs
				unitpercepts.add(new FriendlyPercept(u.getId(), unittype));
				if (!u.isCompleted()) {
					TilePosition pos = u.getTilePosition();
					newunitpercepts.add(new UnderConstructionPercept(u.getId(), u.getHitPoints() + u.getShields(),
							pos.getX(), pos.getY(), getRegion(u)));
				}
			} else { // enemy
				long orientation = 45 * Math.round(Math.toDegrees(u.getAngle()) / 45.0);
				TilePosition pos = u.getTilePosition();
				this.enemies.put(u.getId(),
						new EnemyPercept(u.getId(), BwapiUtility.getName(type), u.getHitPoints(), u.getShields(),
								(u instanceof SpellCaster) ? ((SpellCaster) u).getEnergy() : 0,
								new ConditionHandler(this.bwapi, u).getConditions(), (int) orientation, pos.getX(),
								pos.getY(), getRegion(u), this.frame));
				if (type.canAttack()) {
					Unit target = ((MobileUnit) u).getTargetUnit(); // TODO: support orderTarget?
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new AttackingPercept(u.getId(), target.getId()));
					}
				}
			}
		}
		if (newunitpercepts == null) {
			for (EnemyPercept percept : this.enemies.values()) {
				unitpercepts.add(percept);
			}
		}
	}
}
