package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bwapi.Race;
import bwapi.Region;
import bwapi.TilePosition;
import bwapi.UnitType;
import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
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
	private void perceiveTerran(TilePosition pos, Set<Percept> percepts) {
		if (this.api.canBuildHere(pos, UnitType.Terran_Missile_Turret, null, true)) {
			Region region = this.api.getRegionAt(pos.toPosition());
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), region.getID()));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveProtosss(TilePosition pos, Set<Percept> percepts) {
		boolean nearPylon = this.api.canBuildHere(pos, UnitType.Protoss_Photon_Cannon, null, true);
		if (nearPylon || this.api.canBuildHere(pos, UnitType.Protoss_Pylon, null, true)) {
			Region region = this.api.getRegionAt(pos.toPosition());
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), region.getID(), nearPylon));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveZerg(TilePosition pos, Set<Percept> percepts) {
		boolean onCreep = this.api.canBuildHere(pos, UnitType.Zerg_Creep_Colony, null, true);
		if (onCreep || this.api.canBuildHere(pos, UnitType.Terran_Missile_Turret, null, true)) {
			Region region = this.api.getRegionAt(pos.toPosition());
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), region.getID(), onCreep));
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();
		int mapWidth = this.api.mapWidth();
		int mapHeight = this.api.mapHeight();

		for (int x = 0; x < mapWidth; x += steps) {
			for (int y = 0; y < mapHeight; y += steps) {
				TilePosition pos = new TilePosition(x, y);
				if (this.api.isBuildable(pos)) {
					if (this.api.self().getRace() == Race.Terran) {
						perceiveTerran(pos, percepts);
					} else if (this.api.self().getRace() == Race.Protoss) {
						perceiveProtosss(pos, percepts);
					} else if (this.api.self().getRace() == Race.Zerg) {
						perceiveZerg(pos, percepts);
					}
				}
			}
		}
		toReturn.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), percepts);
		return toReturn;
	}
}
