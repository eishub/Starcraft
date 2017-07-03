package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.VespeneGeyserPercept;

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
	public WorkerPerceiver(bwapi.Game api, Unit unit) {
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
				TilePosition pos = this.unit.getTilePosition();
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), u.getResources(), pos.getX(),
						pos.getY(), BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api));
				minerals.add(mineralfield);
			} else if (BwapiUtility.isValid(u) && u.getType() == UnitType.Resource_Vespene_Geyser) {
				TilePosition pos = this.unit.getTilePosition();
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(), pos.getX(),
						pos.getY(), BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api));
				geysers.add(geyser);
			}
		}
		for (Unit u : this.api.self().getUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isRefinery()) {
				TilePosition pos = this.unit.getTilePosition();
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(), pos.getX(),
						pos.getY(), BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api));
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}
}
