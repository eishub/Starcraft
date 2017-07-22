package eisbw.percepts.perceivers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.ConstructionSitePercept;
import eisbw.percepts.Percepts;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.types.RaceType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

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
	public ConstructionSitePerceiver(JNIBWAPI api) {
		super(api);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> percepts = new LinkedList<>();
		jnibwapi.Map map = this.api.getMap();
		int mapWidth = map.getSize().getBX();
		int mapHeight = map.getSize().getBY();
		RaceType race = (this.api.getSelf() == null) ? RaceTypes.Unknown : this.api.getSelf().getRace();
		for (int x = 0; x < mapWidth; x += steps) {
			for (int y = 0; y < mapHeight; y += steps) {
				Position pos = new Position(x, y, Position.PosType.BUILD);
				if (map.isBuildable(pos) && this.api.isVisible(pos)) {
					if (race == RaceTypes.Terran) {
						perceiveTerran(pos, percepts);
					} else if (race == RaceTypes.Protoss) {
						perceiveProtosss(pos, percepts);
					} else if (race == RaceTypes.Zerg) {
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
	private void perceiveTerran(Position pos, List<Percept> percepts) {
		if (this.api.canBuildHere(pos, UnitType.UnitTypes.Terran_Missile_Turret, false)) {
			percepts.add(new ConstructionSitePercept(pos.getBX(), pos.getBY(), getRegion(pos)));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveProtosss(Position pos, List<Percept> percepts) {
		boolean nearPylon = this.api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Photon_Cannon, false);
		if (nearPylon || this.api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Pylon, false)) {
			percepts.add(new ConstructionSitePercept(pos.getBX(), pos.getBY(), getRegion(pos), nearPylon));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveZerg(Position pos, List<Percept> percepts) {
		boolean onCreep = this.api.canBuildHere(pos, UnitTypes.Zerg_Creep_Colony, false);
		if (onCreep || this.api.canBuildHere(pos, UnitTypes.Terran_Missile_Turret, false)) {
			percepts.add(new ConstructionSitePercept(pos.getBX(), pos.getBY(), getRegion(pos), onCreep));
		}
	}

	private int getRegion(Position pos) {
		return BwapiUtility.getRegion(pos, this.api.getMap());
	}
}
