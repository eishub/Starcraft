package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.Order;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;
import org.openbw.bwapi4j.unit.Bunker;
import org.openbw.bwapi4j.unit.Carrier;
import org.openbw.bwapi4j.unit.Hatchery;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.NuclearSilo;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Reaver;
import org.openbw.bwapi4j.unit.ResearchingFacility;
import org.openbw.bwapi4j.unit.SpellCaster;
import org.openbw.bwapi4j.unit.TrainingFacility;
import org.openbw.bwapi4j.unit.Transporter;
import org.openbw.bwapi4j.unit.Unit;
import org.openbw.bwapi4j.unit.Vulture;

import bwta.BWTA;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.OrderPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.QueueSizePercept;
import eisbw.percepts.ResearchingPercept;
import eisbw.percepts.SelfPercept;
import eisbw.percepts.StatusPercept;
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
	public GenericUnitPerceiver(BW bwapi, BWTA bwta, PlayerUnit unit) {
		super(bwapi, bwta, unit);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		selfPercept(toReturn);
		statusPercept(toReturn);
		orderPercept(toReturn);
		defensiveMatrixPercept(toReturn);
		unitLoadedPercept(toReturn);

		UnitType type = BwapiUtility.getType(this.unit);
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
		long orientation = 45 * Math.round(Math.toDegrees(this.unit.getAngle()) / 45.0);
		TilePosition pos = this.unit.getTilePosition();
		int region = BwapiUtility.getRegion(pos, this.bwta);
		statusPercept.add(new StatusPercept(this.unit.getHitPoints(), this.unit.getShields(),
				(this.unit instanceof SpellCaster) ? ((SpellCaster) this.unit).getEnergy() : 0,
				new ConditionHandler(this.bwapi, this.unit).getConditions(), (int) orientation, pos.getX(), pos.getY(),
				region));
		toReturn.put(new PerceptFilter(Percepts.STATUS, Filter.Type.ON_CHANGE), statusPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void selfPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> selfPercept = new ArrayList<>(1);
		UnitType type = BwapiUtility.getType(this.unit);
		selfPercept.add(new SelfPercept(this.unit.getId(), BwapiUtility.getName(type)));
		toReturn.put(new PerceptFilter(Percepts.SELF, Filter.Type.ONCE), selfPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void defensiveMatrixPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		if (this.unit instanceof MobileUnit && ((MobileUnit) this.unit).isDefenseMatrixed()) {
			// List<Percept> defensiveMatrixPercept = new ArrayList<>(1);
			// defensiveMatrixPercept.add(new DefensiveMatrixPercept(((MobileUnit)
			// this.unit).getDefenseMatrixPoints()));
			// toReturn.put(new PerceptFilter(Percepts.DEFENSIVEMATRIX, Filter.Type.ALWAYS),
			// defensiveMatrixPercept);
			// FIXME: not supported by lib atm.
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void unitLoadedPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		if (this.unit instanceof Transporter) {
			Transporter unit = (Transporter) this.unit;
			if (unit.isLoaded()) {
				// List<PlayerUnit> loadedUnits = unit.getLoadedUnits(); // TODO: not supported
				// by lib atm.
				// List<Percept> unitLoadedPercept = new ArrayList<>(loadedUnits.size());
				// for (Unit u : loadedUnits) {
				// if (BwapiUtility.isValid(u)) {
				// unitLoadedPercept.add(new UnitLoadedPercept(u.getId()));
				// }
				// }
				// if (!unitLoadedPercept.isEmpty()) {
				// toReturn.put(new PerceptFilter(Percepts.UNITLOADED, Filter.Type.ALWAYS),
				// unitLoadedPercept);
				// }
			}
		} else if (this.unit instanceof Bunker) {
			Bunker unit = (Bunker) this.unit;
			if (unit.isLoaded()) {
				// List<PlayerUnit> loadedUnits = unit.getLoadedUnits(); // TODO: not supported
				// by lib atm.
				// List<Percept> unitLoadedPercept = new ArrayList<>(loadedUnits.size());
				// for (Unit u : loadedUnits) {
				// if (BwapiUtility.isValid(u)) {
				// unitLoadedPercept.add(new UnitLoadedPercept(u.getId()));
				// }
				// }
				// if (!unitLoadedPercept.isEmpty()) {
				// toReturn.put(new PerceptFilter(Percepts.UNITLOADED, Filter.Type.ALWAYS),
				// unitLoadedPercept);
				// }
			}
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void orderPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> orderPercept = new ArrayList<>(1);
		MobileUnit unit = (MobileUnit) this.unit;
		Order primary = (unit.getOrder() == null) ? Order.None : unit.getOrder();
		Unit targetUnit = (unit.getTargetUnit() == null) ? unit.getOrderTarget() : unit.getTargetUnit();
		TilePosition targetPos = unit.getTargetPosition().toTilePosition();
		Order secondary = (unit.getSecondaryOrder() == null) ? Order.None : unit.getSecondaryOrder();
		orderPercept.add(new OrderPercept(primary.toString(), (targetUnit == null) ? -1 : targetUnit.getId(),
				(targetPos == null) ? -1 : targetPos.getX(), (targetPos == null) ? -1 : targetPos.getY(),
				secondary.toString()));
		toReturn.put(new PerceptFilter(Percepts.ORDER, Filter.Type.ON_CHANGE), orderPercept);
	}

	private void researchingPercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> researchPercepts = new ArrayList<>(2);
		if (this.unit instanceof ResearchingFacility && ((ResearchingFacility) this.unit).isResearching()) {
			researchPercepts.add(new ResearchingPercept(
					BwapiUtility.getName(/* this.unit.getTech() FIXME: not supported */ TechType.Unknown)));
		}
		if (this.unit instanceof ResearchingFacility && ((ResearchingFacility) this.unit).isUpgrading()) {
			researchPercepts.add(new ResearchingPercept(
					BwapiUtility.getName(/* this.unit.getUpgrade() FIXME: not supported */ UpgradeType.Unknown)));
		}
		if (!researchPercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchPercepts);
		}
	}

	private void queueSizePercept(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> queueSizePercept = new ArrayList<>(1);
		if (this.unit instanceof Hatchery) {
			queueSizePercept.add(new QueueSizePercept(((Hatchery) this.unit).getLarva().size()));
		} else if (this.unit instanceof Hatchery) {
			queueSizePercept.add(new QueueSizePercept(((Hatchery) this.unit).getLarva().size()));
		} else if (this.unit instanceof Hatchery) {
			queueSizePercept.add(new QueueSizePercept(((Hatchery) this.unit).getLarva().size()));
		} else if (this.unit instanceof NuclearSilo) {
			queueSizePercept.add(new QueueSizePercept(((NuclearSilo) this.unit).hasNuke() ? 1 : 0));
		} else if (this.unit instanceof Vulture) {
			queueSizePercept.add(new QueueSizePercept(((Vulture) this.unit).getSpiderMineCount()));
		} else if (this.unit instanceof Carrier) {
			queueSizePercept.add(new QueueSizePercept(
					/* ((Carrier) this.unit).getTrainingQueueSize() + FIXME: not supported by lib */
					((Carrier) this.unit).getInterceptorCount()));
		} else if (this.unit instanceof Reaver) {
			queueSizePercept.add(new QueueSizePercept(
					/* ((Reaver) this.unit).getTrainingQueueSize() + FIXME: not supported by lib */ ((Reaver) this.unit)
							.getScarabCount()));
		} else if (this.unit instanceof TrainingFacility) {
			queueSizePercept.add(new QueueSizePercept(((TrainingFacility) this.unit).getTrainingQueueSize()));
		}
		toReturn.put(new PerceptFilter(Percepts.QUEUESIZE, Filter.Type.ON_CHANGE), queueSizePercept);
	}
}
