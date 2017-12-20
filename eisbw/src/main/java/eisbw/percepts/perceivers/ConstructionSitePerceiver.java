package eisbw.percepts.perceivers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.BWMap;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;

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

	public ConstructionSitePerceiver(BW bwapi, BWTA bwta) {
		super(bwapi, bwta);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> percepts = new LinkedList<>();
		BWMap map = this.bwapi.getBWMap();
		Race race = (this.bwapi.getInteractionHandler() == null) ? Race.Unknown
				: this.bwapi.getInteractionHandler().self().getRace();
		for (int x = 0; x < map.mapWidth(); x += steps) {
			for (int y = 0; y < map.mapHeight(); y += steps) {
				TilePosition pos = new TilePosition(x, y);
				if (map.isBuildable(pos, false) && map.isVisible(pos)) {
					if (race == Race.Terran) {
						perceiveTerran(pos, percepts);
					} else if (race == Race.Protoss) {
						perceiveProtosss(pos, percepts);
					} else if (race == Race.Zerg) {
						perceiveZerg(pos, percepts);
					}
				}
			}
		}
		toReturn.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), percepts);
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveTerran(TilePosition pos, List<Percept> percepts) {
		if (this.bwapi.canBuildHere(pos, UnitType.Terran_Missile_Turret)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), getRegion(pos)));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveProtosss(TilePosition pos, List<Percept> percepts) {
		boolean nearPylon = this.bwapi.canBuildHere(pos, UnitType.Protoss_Photon_Cannon);
		if (nearPylon || this.bwapi.canBuildHere(pos, UnitType.Protoss_Pylon)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), getRegion(pos), nearPylon));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveZerg(TilePosition pos, List<Percept> percepts) {
		boolean onCreep = this.bwapi.canBuildHere(pos, UnitType.Zerg_Creep_Colony);
		if (onCreep || this.bwapi.canBuildHere(pos, UnitType.Terran_Missile_Turret)) {
			percepts.add(new ConstructionSitePercept(pos.getX(), pos.getY(), getRegion(pos), onCreep));
		}
	}

	private int getRegion(TilePosition pos) {
		return BwapiUtility.getRegion(pos, this.bwta);
	}
}
