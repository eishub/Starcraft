package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.OrderType;
import jnibwapi.types.OrderType.OrderTypes;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType.UpgradeTypes;

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
	public GenericUnitPerceiver(JNIBWAPI api, Unit unit) {
		super(api, unit);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		selfPercept(toReturn);
		statusPercept(toReturn);
		defensiveMatrixPercept(toReturn);
		orderPercept(toReturn);

		if (this.unit.getType().getSpaceProvided() > 0) {
			List<Unit> loadedUnits = this.unit.getLoadedUnits();
			unitLoadedPercept(toReturn, loadedUnits);
		}
		if (this.unit.getType().isProduceCapable()
				|| this.unit.getType().getID() == UnitTypes.Terran_Nuclear_Silo.getID()) {
			queueSizePercept(toReturn);
		}
		if (this.unit.getType().isBuilding()) {
			researchingPercept(toReturn);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void statusPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> statusPercept = new ArrayList<>(1);
		Position pos = this.unit.getPosition();
		int region = BwapiUtility.getRegion(pos, this.api.getMap());
		statusPercept.add(new StatusPercept(this.unit.getHitPoints(), this.unit.getShields(), this.unit.getEnergy(),
				new ConditionHandler(this.api, this.unit).getConditions(), pos.getBX(), pos.getBY(), region));
		toReturn.put(new PerceptFilter(Percepts.STATUS, Filter.Type.ON_CHANGE), statusPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void selfPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> selfPercept = new ArrayList<>(1);
		selfPercept.add(new SelfPercept(this.unit.getID(), BwapiUtility.getName(this.unit.getType())));
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
		OrderType primary = (this.unit.getOrder() == null) ? OrderTypes.None : this.unit.getOrder();
		Unit targetUnit = (this.unit.getTarget() == null) ? this.unit.getOrderTarget() : this.unit.getTarget();
		Position targetPos = this.unit.getTargetPosition();
		OrderType secondary = (this.unit.getSecondaryOrder() == null) ? OrderTypes.None : this.unit.getSecondaryOrder();
		orderPercept.add(new OrderPercept(primary.getName(), (targetUnit == null) ? -1 : targetUnit.getID(),
				(targetPos == null) ? -1 : targetPos.getBX(), (targetPos == null) ? -1 : targetPos.getBY(),
				secondary.getName()));
		toReturn.put(new PerceptFilter(Percepts.ORDER, Filter.Type.ON_CHANGE), orderPercept);
	}

	private void researchingPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> researchPercepts = new ArrayList<>(2);
		if (this.unit.getTech() != null && this.unit.getTech().getID() != TechTypes.None.getID()
				&& this.unit.getTech().getID() != TechTypes.Unknown.getID()) {
			researchPercepts.add(new ResearchingPercept(this.unit.getTech().getName()));
		}
		if (this.unit.getUpgrade() != null && this.unit.getUpgrade().getID() != UpgradeTypes.None.getID()
				&& this.unit.getUpgrade().getID() != UpgradeTypes.Unknown.getID()) {
			researchPercepts.add(new ResearchingPercept(this.unit.getUpgrade().getName()));
		}
		if (!researchPercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchPercepts);
		}
	}

	private void queueSizePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> queueSizePercept = new ArrayList<>(1);
		if (this.unit.getType().getID() == UnitTypes.Zerg_Hatchery.getID()
				|| this.unit.getType().getID() == UnitTypes.Zerg_Lair.getID()
				|| this.unit.getType().getID() == UnitTypes.Zerg_Hive.getID()) {
			queueSizePercept.add(new QueueSizePercept(this.unit.getLarvaCount()));
		} else {
			queueSizePercept.add(new QueueSizePercept(this.unit.getTrainingQueueSize()));
		}
		toReturn.put(new PerceptFilter(Percepts.QUEUESIZE, Filter.Type.ON_CHANGE), queueSizePercept);
	}
}
