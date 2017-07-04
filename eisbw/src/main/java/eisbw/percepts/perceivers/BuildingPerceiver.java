package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.percepts.Percepts;
import eisbw.percepts.QueueSizePercept;
import eisbw.percepts.ResearchingPercept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType.UpgradeTypes;

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
		researchedPercept(toReturn);
		queueSizePercept(toReturn);
		return toReturn;
	}

	private void researchedPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> researchedPercepts = new HashSet<>(2);
		if (this.unit.getTech() != null && this.unit.getTech().getID() != TechTypes.None.getID()
				&& this.unit.getTech().getID() != TechTypes.Unknown.getID()) {
			researchedPercepts.add(new ResearchingPercept(this.unit.getTech().getName()));
		}
		if (this.unit.getUpgrade() != null && this.unit.getUpgrade().getID() != UpgradeTypes.None.getID()
				&& this.unit.getUpgrade().getID() != UpgradeTypes.Unknown.getID()) {
			researchedPercepts.add(new ResearchingPercept(this.unit.getUpgrade().getName()));
		}
		if (!researchedPercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.RESEARCHING, Filter.Type.ALWAYS), researchedPercepts);
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
}
