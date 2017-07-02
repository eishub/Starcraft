package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.percepts.Percepts;
import eisbw.percepts.QueueSizePercept;
import eisbw.percepts.RallyPointPercept;
import eisbw.percepts.RallyUnitPercept;
import eisbw.percepts.ResearchingPercept;
import eisbw.percepts.UpgradePercept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position.Positions;
import jnibwapi.Unit;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the building percepts.
 *
 */
public class BuildingPerceiver extends UnitPerceiver {
	/**
	 * @param api
	 *            The BWAPI
	 * @param unit
	 *            The perceiving unit.
	 */
	public BuildingPerceiver(JNIBWAPI api, Unit unit) {
		super(api, unit);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		rallyPointPercept(toReturn);
		rallyUnitPercept(toReturn);
		queueSizePercept(toReturn);
		upgradingPercept(toReturn);
		researchedPercept(toReturn);
		return toReturn;
	}

	private void researchedPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> researchedPercept = new HashSet<>(1);
		if (this.unit.getTech() != null && this.unit.getTech().getID() != TechTypes.None.getID()
				&& this.unit.getTech().getID() != TechTypes.Unknown.getID()) {
			researchedPercept.add(new ResearchingPercept(this.unit.getTech().getName()));
			toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchedPercept);
		}
	}

	private void rallyPointPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> rallyPointPercept = new HashSet<>(1);
		if (this.unit.getRallyPosition() != null && !this.unit.getRallyPosition().equals(Positions.None)) {
			rallyPointPercept.add(
					new RallyPointPercept(this.unit.getRallyPosition().getBX(), this.unit.getRallyPosition().getBY()));
			toReturn.put(new PerceptFilter(Percepts.RALLYPOINT, Filter.Type.ON_CHANGE), rallyPointPercept);
		}
	}

	private void upgradingPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> upgradingPercept = new HashSet<>(1);
		if (this.unit.isUpgrading()) {
			upgradingPercept.add(new UpgradePercept(this.unit.getUpgrade().getName()));
			toReturn.put(new PerceptFilter(Percepts.UPGRADING, Filter.Type.ALWAYS), upgradingPercept);
		}
	}

	private void queueSizePercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> queueSizePercept = new HashSet<>(1);
		if (this.unit.getType().getID() == UnitTypes.Zerg_Hatchery.getID()
				|| this.unit.getType().getID() == UnitTypes.Zerg_Lair.getID()
				|| this.unit.getType().getID() == UnitTypes.Zerg_Hive.getID()) {
			queueSizePercept.add(new QueueSizePercept(this.unit.getLarvaCount()));
		} else {
			queueSizePercept.add(new QueueSizePercept(this.unit.getTrainingQueueSize()));
		}
		toReturn.put(new PerceptFilter(Percepts.QUEUESIZE, Filter.Type.ON_CHANGE), queueSizePercept);
	}

	private void rallyUnitPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> rallyUnitPercept = new HashSet<>(1);
		if (this.unit.getRallyUnit() != null) {
			rallyUnitPercept.add(new RallyUnitPercept(this.unit.getRallyUnit().getID()));
			toReturn.put(new PerceptFilter(Percepts.RALLYUNIT, Filter.Type.ON_CHANGE), rallyUnitPercept);
		}
	}
}
