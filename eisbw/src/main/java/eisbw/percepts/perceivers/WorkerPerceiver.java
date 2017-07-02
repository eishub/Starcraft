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

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		resourcesPercepts(toReturn);
		return toReturn;
	}

	private void resourcesPercepts(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> minerals = new HashSet<>();
		Set<Percept> geysers = new HashSet<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isMineralField()) {
				int region = BwapiUtility.getRegion(u, this.api.getMap());
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				minerals.add(mineralfield);
			} else if (BwapiUtility.isValid(u) && u.getType().getID() == UnitTypes.Resource_Vespene_Geyser.getID()) {
				int region = BwapiUtility.getRegion(u, this.api.getMap());
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				geysers.add(geyser);
			}
		}
		for (Unit u : this.api.getMyUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isRefinery()) {
				int region = BwapiUtility.getRegion(u, this.api.getMap());
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}
}
