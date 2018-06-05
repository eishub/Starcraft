package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eis.eis2java.translation.Filter;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
//import eis.iilang.Identifier;
//import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackingPercept;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FramePercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResearchedPercept;
import eisbw.percepts.ResourcesPercept;
import eisbw.percepts.UnderConstructionPercept;
import eisbw.percepts.VespeneGeyserPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType;
import jnibwapi.types.UpgradeType.UpgradeTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the unit percepts.
 *
 */
public class UnitsPerceiver extends Perceiver {
	private final int frame;
	private final Map<Integer, EnemyPercept> enemies;
	private final List<Unit> myUnits;
	private final List<Unit> enemyUnits;
	private final List<Unit> neutralUnits;

	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(JNIBWAPI api, Map<Integer, EnemyPercept> enemies) {
		super(api);
		this.frame = api.getFrameCount();
		this.enemies = enemies;
		this.myUnits = api.getMyUnits();
		this.enemyUnits = api.getEnemyUnits();
		this.neutralUnits = api.getNeutralUnits();
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		framePercept(toReturn);
		resourcesPercepts(toReturn);
		researchedPercept(toReturn);
		unitsPercepts(toReturn);
	}

	private void framePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> framepercept = new ArrayList<>(1);
		framepercept.add(new FramePercept(this.api.getFrameCount()));
		toReturn.put(new PerceptFilter(Percepts.FRAME, Filter.Type.ON_CHANGE), framepercept);
	}

	private void resourcesPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		Player self = this.api.getSelf();
		if (self != null) { // for tests
			List<Percept> resourcePercept = new ArrayList<>(1);
			resourcePercept.add(new ResourcesPercept(self.getMinerals(), self.getGas(), self.getSupplyUsed(),
					self.getSupplyTotal()));
			toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);
		}
		List<Percept> minerals = new LinkedList<>();
		List<Percept> geysers = new LinkedList<>();
		for (Unit u : this.neutralUnits) {
			UnitType type = BwapiUtility.getType(u);
			if (type != null && type.isMineralField()) {
				Position pos = u.getPosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), (int) amount, pos.getBX(),
						pos.getBY(), getRegion(u));
				minerals.add(mineralfield);
			} else if (type == UnitTypes.Resource_Vespene_Geyser) {
				Position pos = u.getPosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), (int) amount, pos.getBX(),
						pos.getBY(), getRegion(u));
				geysers.add(geyser);
			}
		}
		for (Unit u : this.myUnits) {
			UnitType type = BwapiUtility.getType(u);
			if (type != null && type.isRefinery()) {
				Position pos = u.getPosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), (int) amount, pos.getBX(),
						pos.getBY(), getRegion(u));
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}

	private void researchedPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		Player self = this.api.getSelf(); // TODO: should do this for enemy too (but that doesn't seem to work)
		List<Parameter> researched = new LinkedList<>();
		for (UpgradeType upgrade : UpgradeTypes.getAllUpgradeTypes()) {
			if (upgrade.getUpgradeTimeBase() > 0) {
				int level = self.getUpgradeLevel(upgrade);
				if (level > 0) {
					if (upgrade.getMaxRepeats() > 1) {
						for (int i = level; i > 0; --i) {
							researched.add(new Identifier(upgrade.getName() + " " + i));
						}
					} else {
						researched.add(new Identifier(upgrade.getName()));
					}
				}
			}
		}
		for (TechType tech : TechTypes.getAllTechTypes()) {
			if (tech.getResearchTime() > 0 && self.isResearched(tech)) {
				researched.add(new Identifier(tech.getName()));
			}
		}
		List<Percept> ownPercept = new ArrayList<>(1);
		ownPercept.add(new ResearchedPercept(researched));
		toReturn.put(new PerceptFilter(Percepts.RESEARCHED, Filter.Type.ON_CHANGE), ownPercept);
	}

	private void unitsPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> friendlypercepts = new LinkedList<>();
		List<Percept> newunitpercepts = new LinkedList<>();
		setMyUnitPercepts(friendlypercepts, newunitpercepts);

		List<Percept> enemypercepts = new LinkedList<>();
		List<Percept> attackingpercepts = new LinkedList<>();
		setEnemyUnitPercepts(enemypercepts, attackingpercepts);

		if (!friendlypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		}
		if (!newunitpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNDERCONSTRUCTION, Filter.Type.ALWAYS), newunitpercepts);
		}
		if (!enemypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		}
		if (!attackingpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		}
	}

	/**
	 * @param u
	 * @return The region for the given unit (from a cache if it was seen before).
	 */
	private int getRegion(Unit u) {
		return BwapiUtility.getRegion(u.getPosition(), this.api.getMap());
	}

	private void setMyUnitPercepts(List<Percept> unitpercepts, List<Percept> newunitpercepts) {
		Map<Integer, Integer> constructing = new HashMap<>();
		for (Unit u : this.myUnits) {
			if ((u.isConstructing() || u.isTraining()) && u.getBuildUnit() != null) {
				constructing.put(u.getBuildUnit().getID(), u.getID());
			}
		}
		for (Unit u : this.myUnits) {
			UnitType type = BwapiUtility.getType(u);
			if (type == null || type.isAddon()) {
				continue;
			}
			String unittype = (type == UnitTypes.Zerg_Egg) ? u.getBuildType().getName() : BwapiUtility.getName(type);
			unitpercepts.add(new FriendlyPercept(u.getID(), unittype));
			if (!u.isCompleted()) {
				Position pos = u.getPosition();
				int builderId = constructing.containsKey(u.getID()) ? constructing.get(u.getID()) : -1;
				newunitpercepts.add(new UnderConstructionPercept(u.getID(), builderId,
						u.getHitPoints() + u.getShields(), pos.getBX(), pos.getBY(), getRegion(u)));
			}
		}
	}

	private void setEnemyUnitPercepts(List<Percept> unitpercepts, List<Percept> attackingpercepts) {
		List<Unit> units = new ArrayList<>(this.enemyUnits);
		units.addAll(this.neutralUnits);
		for (Unit u : units) {
			UnitType type = BwapiUtility.getType(u);
			if (type == null || type.isAddon() || type.isMineralField() || type == UnitTypes.Resource_Vespene_Geyser
					|| type == UnitTypes.Critter_Bengalaas || type == UnitTypes.Critter_Kakaru
					|| type == UnitTypes.Critter_Ragnasaur || type == UnitTypes.Critter_Rhynadon
					|| type == UnitTypes.Critter_Scantid || type == UnitTypes.Critter_Ursadon) {
				continue;
			}
			long orientation = 45 * Math.round(Math.toDegrees(u.getAngle()) / 45.0);
			Position pos = u.getPosition();
			this.enemies.put(u.getID(),
					new EnemyPercept(u.getID(), BwapiUtility.getName(type), u.getHitPoints(), u.getShields(),
							u.getEnergy(), new ConditionHandler(this.api, u).getConditions(), (int) orientation,
							pos.getBX(), pos.getBY(), getRegion(u), this.frame));
			if (type.isAttackCapable()) {
				Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
				if (target != null) {
					attackingpercepts.add(new AttackingPercept(u.getID(), target.getID()));
				}
			}
		}
		for (EnemyPercept percept : this.enemies.values()) {
			unitpercepts.add(percept);
		}
	}
}
