package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bwapi.Bullet;
import bwapi.BulletType;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackPercept;
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
	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(bwapi.Game api) {
		super(api);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		framePercept(toReturn);
		resourcesPercepts(toReturn);
		unitsPercepts(toReturn);
		attackPercepts(toReturn);
	}

	private void framePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> framepercept = new ArrayList<>(1);
		framepercept.add(new FramePercept(this.api.getFrameCount()));
		toReturn.put(new PerceptFilter(Percepts.FRAME, Filter.Type.ON_CHANGE), framepercept);
	}

	private void resourcesPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		Player self = BwapiUtility.getSelf(this.api);
		if (self != null) { // for tests
			List<Percept> resourcePercept = new ArrayList<>(1);
			resourcePercept
					.add(new ResourcesPercept(self.minerals(), self.gas(), self.supplyUsed(), self.supplyTotal()));
			toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);
		}
		List<Percept> minerals = new LinkedList<>();
		List<Percept> geysers = new LinkedList<>();
		for (Unit u : this.api.getNeutralUnits()) {
			UnitType type = BwapiUtility.getType(u);
			if (type != null && type.isMineralField()) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), (int) amount, pos.getX(),
						pos.getY(), BwapiUtility.getRegion(pos, this.api));
				minerals.add(mineralfield);
			} else if (type == UnitType.Resource_Vespene_Geyser) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), (int) amount, pos.getX(), pos.getY(),
						BwapiUtility.getRegion(pos, this.api));
				geysers.add(geyser);
			}
		}
		for (Unit u : self.getUnits()) {
			UnitType type = BwapiUtility.getType(u);
			if (type != null && type.isRefinery()) {
				TilePosition pos = u.getTilePosition();
				double amount = 100 * Math.ceil(u.getResources() / 100.0);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), (int) amount, pos.getX(), pos.getY(),
						BwapiUtility.getRegion(pos, this.api));
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
		setUnitPercepts(BwapiUtility.getSelf(this.api).getUnits(), newunitpercepts, friendlypercepts,
				attackingpercepts);
		// perceive enemy units
		setUnitPercepts(BwapiUtility.getEnemy(this.api).getUnits(), null, enemypercepts, attackingpercepts);

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
	private void setUnitPercepts(List<Unit> units, List<Percept> newunitpercepts, List<Percept> unitpercepts,
			List<Percept> attackingpercepts) {
		for (Unit u : units) {
			UnitType t = BwapiUtility.getType(u);
			if (t == null) {
				continue;
			}
			int id = u.getID();
			if (newunitpercepts != null) {
				String unittype = (t == UnitType.Zerg_Egg) ? BwapiUtility.getName(u.getBuildType())
						: BwapiUtility.getName(t);
				unitpercepts.add(new FriendlyPercept(id, unittype));
				if (!BwapiUtility.isComplete(u)) {
					TilePosition pos = u.getTilePosition();
					newunitpercepts.add(new UnderConstructionPercept(id, u.getHitPoints() + u.getShields(), pos.getX(),
							pos.getY(), BwapiUtility.getRegion(pos, this.api)));
				}
			} else {
				TilePosition pos = u.getTilePosition();
				unitpercepts.add(
						new EnemyPercept(id, BwapiUtility.getName(t), u.getHitPoints(), u.getShields(), u.getEnergy(),
								new ConditionHandler(this.api, u).getConditions(), (int) Math.toDegrees(u.getAngle()),
								pos.getX(), pos.getY(), BwapiUtility.getRegion(pos, this.api)));
				if (t.canAttack()) {
					Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new AttackingPercept(id, target.getID()));
					}
				}
			}
		}
	}

	private void attackPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> attackpercepts = new LinkedList<>();
		for (Bullet bullet : this.api.getBullets()) {
			if (bullet.exists() && bullet.isVisible()) {
				BulletType type = bullet.getType();
				Unit source = bullet.getSource();
				Unit targetUnit = bullet.getTarget();
				Position targetPos = bullet.getTargetPosition();
				boolean travelling = !targetPos.equals(bullet.getPosition());
				attackpercepts.add(new AttackPercept(BwapiUtility.getName(type), source.getID(),
						(targetUnit == null) ? -1 : targetUnit.getID(),
						(targetPos == null) ? -1 : targetPos.toTilePosition().getX(),
						(targetPos == null) ? -1 : targetPos.toTilePosition().getY(), travelling));
			}
		}
		if (!attackpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ATTACK, Filter.Type.ALWAYS), attackpercepts);
		}
	}
}
