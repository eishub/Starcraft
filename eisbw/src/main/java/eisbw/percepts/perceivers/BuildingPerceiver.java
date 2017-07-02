package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.percepts.Percepts;
import eisbw.percepts.QueueSizePercept;
import eisbw.percepts.ResearchingPercept;

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
	public BuildingPerceiver(bwapi.Game api, Unit unit) {
		super(api, unit);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		researchedPercept(toReturn);
		queueSizePercept(toReturn);
		return toReturn;
	}

	private void researchedPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> researchedPercepts = new HashSet<>(2);
		if (this.unit.getTech() != null && this.unit.getTech() != TechType.None) {
			researchedPercepts.add(new ResearchingPercept(this.unit.getTech().toString()));
		}
		if (this.unit.getUpgrade() != null && this.unit.getUpgrade() != UpgradeType.None) {
			researchedPercepts.add(new ResearchingPercept(this.unit.getUpgrade().toString()));
		}
		toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchedPercepts);
	}

	private void queueSizePercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> queueSizePercept = new HashSet<>(1);
		if (this.unit.getType() == UnitType.Zerg_Hatchery || this.unit.getType() == UnitType.Zerg_Lair
				|| this.unit.getType() == UnitType.Zerg_Hive) {
			queueSizePercept.add(new QueueSizePercept(this.unit.getLarva().size()));
		} else {
			queueSizePercept.add(new QueueSizePercept(this.unit.getTrainingQueue().size()));
		}
		toReturn.put(new PerceptFilter(Percepts.QUEUESIZE, Filter.Type.ON_CHANGE), queueSizePercept);
	}
}
