package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.VespeneGeyserPercept;
import eisbw.percepts.WorkerActivityPercept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the worker percepts.
 */
public class WorkerPerceiver extends UnitPerceiver {
	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The unit which is about the perceiving.
	 */
	public WorkerPerceiver(JNIBWAPI api, Unit unit) {
		super(api, unit);
	}

	/**
	 * Perceives all the worker percepts minus the terran worker percepts.
	 *
	 * @param toReturn
	 *            The list of percepts
	 */
	private void workerActivity(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>(1);
		if (this.unit.isGatheringGas()) {
			percepts.add(new WorkerActivityPercept("gatheringGas"));
		} else if (this.unit.isGatheringMinerals()) {
			percepts.add(new WorkerActivityPercept("gatheringMinerals"));
		} else if (this.unit.isConstructing()) {
			percepts.add(new WorkerActivityPercept("constructing"));
		} else if (this.unit.isRepairing()) {
			percepts.add(new WorkerActivityPercept("repairing"));
		} else {
			percepts.add(new WorkerActivityPercept("idling"));
		}
		toReturn.put(new PerceptFilter(Percepts.WORKERACTIVITY, Filter.Type.ON_CHANGE), percepts);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		workerActivity(toReturn);
		resourcesPercepts(toReturn);
		return toReturn;
	}

	private void resourcesPercepts(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> minerals = new HashSet<>();
		Set<Percept> geysers = new HashSet<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isMineralField()) {
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), u.getResources(),
						u.getResourceGroup(), u.getPosition().getBX(), u.getPosition().getBY());
				minerals.add(mineralfield);
			} else if (BwapiUtility.isValid(u) && u.getType().getID() == UnitTypes.Resource_Vespene_Geyser.getID()) {
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getResourceGroup(), u.getPosition().getBX(), u.getPosition().getBY());
				geysers.add(geyser);

			}
		}
		for (Unit u : this.api.getMyUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isRefinery()) {
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getResourceGroup(), u.getPosition().getBX(), u.getPosition().getBY());
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}
}
