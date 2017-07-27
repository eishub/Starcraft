package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bwapi.Order;
import bwapi.Position;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.DefensiveMatrixPercept;
import eisbw.percepts.OrderPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.QueueSizePercept;
import eisbw.percepts.ResearchingPercept;
import eisbw.percepts.SelfPercept;
import eisbw.percepts.StatusPercept;
import eisbw.percepts.UnitLoadedPercept;
import eisbw.units.ConditionHandler;

/**
 * @author Danny & Harm - The perceiver which handles all the generic percepts.
 *
 */
public class GenericUnitPerceiver extends UnitPerceiver {
	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The perceiving unit.
	 */
	public GenericUnitPerceiver(bwapi.Game api, Unit unit) {
		super(api, unit);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		selfPercept(toReturn);
		statusPercept(toReturn);
		defensiveMatrixPercept(toReturn);
		orderPercept(toReturn);

		UnitType type = BwapiUtility.getType(this.unit);
		if (type.spaceProvided() > 0) {
			List<Unit> loadedUnits = this.unit.getLoadedUnits();
			unitLoadedPercept(toReturn, loadedUnits);
		}
		if (type.canProduce() || type == UnitType.Terran_Nuclear_Silo || type == UnitType.Terran_Vulture) {
			queueSizePercept(toReturn);
		}
		if (type.isBuilding()) {
			researchingPercept(toReturn);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void statusPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> statusPercept = new ArrayList<>(1);
		TilePosition pos = this.unit.getTilePosition();
		statusPercept.add(new StatusPercept(this.unit.getHitPoints(), this.unit.getShields(), this.unit.getEnergy(),
				new ConditionHandler(this.api, this.unit).getConditions(), (int) Math.toDegrees(this.unit.getAngle()),
				pos.getX(), pos.getY(), BwapiUtility.getRegion(pos, this.api)));
		toReturn.put(new PerceptFilter(Percepts.STATUS, Filter.Type.ON_CHANGE), statusPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void selfPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> selfPercept = new ArrayList<>(1);
		UnitType type = BwapiUtility.getType(this.unit);
		selfPercept.add(new SelfPercept(this.unit.getID(), BwapiUtility.getName(type)));
		toReturn.put(new PerceptFilter(Percepts.SELF, Filter.Type.ONCE), selfPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void defensiveMatrixPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		if (this.unit.isDefenseMatrixed()) {
			List<Percept> defensiveMatrixPercept = new ArrayList<>(1);
			defensiveMatrixPercept.add(new DefensiveMatrixPercept(this.unit.getDefenseMatrixPoints()));
			toReturn.put(new PerceptFilter(Percepts.DEFENSIVEMATRIX, Filter.Type.ALWAYS), defensiveMatrixPercept);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 * @param loadedUnits
	 *            The loaded units
	 */
	private void unitLoadedPercept(Map<PerceptFilter, List<Percept>> toReturn, List<Unit> loadedUnits) {
		List<Percept> unitLoadedPercept = new ArrayList<>(loadedUnits.size());
		for (Unit u : loadedUnits) {
			if (BwapiUtility.isValid(u)) {
				unitLoadedPercept.add(new UnitLoadedPercept(u.getID()));
			}
		}
		if (!unitLoadedPercept.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNITLOADED, Filter.Type.ALWAYS), unitLoadedPercept);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void orderPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> orderPercept = new ArrayList<>(1);
		Order primary = (this.unit.getOrder() == null) ? Order.None : this.unit.getOrder();
		Unit targetUnit = (this.unit.getTarget() == null) ? this.unit.getOrderTarget() : this.unit.getTarget();
		Position targetPos = (this.unit.getTargetPosition() == null) ? this.unit.getOrderTargetPosition()
				: this.unit.getTargetPosition();
		Order secondary = (this.unit.getSecondaryOrder() == null) ? Order.None : this.unit.getSecondaryOrder();
		orderPercept.add(new OrderPercept(primary.toString(), (targetUnit == null) ? -1 : targetUnit.getID(),
				(targetPos == null) ? -1 : targetPos.toTilePosition().getX(),
				(targetPos == null) ? -1 : targetPos.toTilePosition().getY(), secondary.toString()));
		toReturn.put(new PerceptFilter(Percepts.ORDER, Filter.Type.ON_CHANGE), orderPercept);
	}

	private void researchingPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> researchPercepts = new ArrayList<>(2);
		TechType tech = this.unit.getTech();
		if (tech != null && tech != TechType.None && tech != TechType.Unknown) {
			researchPercepts.add(new ResearchingPercept(BwapiUtility.getName(tech)));
		}
		UpgradeType upgrade = this.unit.getUpgrade();
		if (upgrade != null && upgrade != UpgradeType.None && upgrade != UpgradeType.Unknown) {
			researchPercepts.add(new ResearchingPercept(BwapiUtility.getName(upgrade)));
		}
		if (!researchPercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchPercepts);
		}
	}

	private void queueSizePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> queueSizePercept = new ArrayList<>(1);
		UnitType type = BwapiUtility.getType(this.unit);
		if (type == UnitType.Zerg_Hatchery || type == UnitType.Zerg_Lair || type == UnitType.Zerg_Hive) {
			queueSizePercept.add(new QueueSizePercept(this.unit.getLarva().size()));
		} else if (type == UnitType.Terran_Nuclear_Silo) {
			queueSizePercept.add(new QueueSizePercept(this.unit.hasNuke() ? 1 : 0));
		} else if (type == UnitType.Terran_Vulture) {
			queueSizePercept.add(new QueueSizePercept(this.unit.getSpiderMineCount()));
		} else if (type == UnitType.Protoss_Carrier) {
			queueSizePercept
					.add(new QueueSizePercept(this.unit.getTrainingQueue().size() + this.unit.getInterceptorCount()));
		} else if (type == UnitType.Protoss_Reaver) {
			queueSizePercept
					.add(new QueueSizePercept(this.unit.getTrainingQueue().size() + this.unit.getScarabCount()));
		} else {
			queueSizePercept.add(new QueueSizePercept(this.unit.getTrainingQueue().size()));
		}
		toReturn.put(new PerceptFilter(Percepts.QUEUESIZE, Filter.Type.ON_CHANGE), queueSizePercept);
	}
}
