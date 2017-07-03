package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.ConstructionSitePercept;
import eisbw.percepts.Percepts;

/**
 * @author Danny & Harm - The perceiver which handles all the construction site
 *         percepts.
 *
 */
public class ConstructionSitePerceiver extends Perceiver {
	public final static int steps = 2;

	/**
	 * The ConstructionSitePerceiver constructor.
	 *
	 * @param api
	 *            The BWAPI.
	 */
	public ConstructionSitePerceiver(bwapi.Game api) {
		super(api);
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveTerran(TilePosition pos, Unit worker, Set<Percept> percepts) {
		if (this.api.canBuildHere(pos, UnitType.Terran_Missile_Turret, worker, true)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(),
					BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api)));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveProtosss(TilePosition pos, Unit worker, Set<Percept> percepts) {
		boolean nearPylon = this.api.canBuildHere(pos, UnitType.Protoss_Photon_Cannon, worker, true);
		if (nearPylon || this.api.canBuildHere(pos, UnitType.Protoss_Pylon, worker, true)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(),
					BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api), nearPylon));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveZerg(TilePosition pos, Unit worker, Set<Percept> percepts) {
		boolean onCreep = this.api.canBuildHere(pos, UnitType.Zerg_Creep_Colony, worker, true);
		if (onCreep || this.api.canBuildHere(pos, UnitType.Terran_Missile_Turret, worker, true)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(),
					BwapiUtility.getRegionId(BWTA.getRegion(pos), this.api), onCreep));
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Unit worker = null;
		for (Unit unit : this.api.self().getUnits()) {
			if (unit.getType().isWorker()) {
				worker = unit;
				break;
			}
		}
		int mapWidth = (worker == null) ? 0 : this.api.mapWidth();
		int mapHeight = (worker == null) ? 0 : this.api.mapHeight();

		Set<Percept> percepts = new HashSet<>();
		for (int x = 0; x < mapWidth; x += steps) {
			for (int y = 0; y < mapHeight; y += steps) {
				TilePosition pos = new TilePosition(x, y);
				if (this.api.isBuildable(pos)) {
					if (this.api.self().getRace() == Race.Terran) {
						perceiveTerran(pos, worker, percepts);
					} else if (this.api.self().getRace() == Race.Protoss) {
						perceiveProtosss(pos, worker, percepts);
					} else if (this.api.self().getRace() == Race.Zerg) {
						perceiveZerg(pos, worker, percepts);
					}
				}
			}
		}
		toReturn.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), percepts);
		return toReturn;
	}
}
