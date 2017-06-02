package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.percepts.ConstructionSitePercept;
import eisbw.percepts.Percepts;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the construction site
 *         percepts.
 *
 */
public class ConstructionSitePerceiver extends Perceiver {
	/**
	 * The ConstructionSitePerceiver constructor.
	 * 
	 * @param api
	 *            The BWAPI.
	 */
	public ConstructionSitePerceiver(JNIBWAPI api) {
		super(api);
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param xpos
	 *            The x-coordinate
	 * @param ypos
	 *            The y-coordinate
	 * @param illegals
	 *            A list of illegal build places.
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveTerran(Position pos, int xpos, int ypos, Set<Percept> percepts) {
		if (api.canBuildHere(pos, UnitType.UnitTypes.Terran_Command_Center, true)) {
			percepts.add(new ConstructionSitePercept(xpos, ypos));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param xpos
	 *            The x-coordinate
	 * @param ypos
	 *            The y-coordinate
	 * @param illegals
	 *            A list of illegal build places.
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveZerg(Position pos, int xpos, int ypos, Set<Percept> percepts) {
		if (api.canBuildHere(pos, UnitTypes.Zerg_Hatchery, true)) {
			boolean creep = api.canBuildHere(pos, UnitTypes.Zerg_Defiler_Mound, true);
			percepts.add(new ConstructionSitePercept(xpos, ypos, creep));
		}
	}

	/**
	 * @param pos
	 *            The current evaluated position
	 * @param xpos
	 *            The x-coordinate
	 * @param ypos
	 *            The y-coordinate
	 * @param illegals
	 *            A list of illegal build places.
	 * @param percepts
	 *            The list of perceived constructionsites
	 */
	private void perceiveProtosss(Position pos, int xpos, int ypos, Set<Percept> percepts) {
		if (api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Nexus, true)) {
			boolean nearPylon = api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Gateway, true);
			percepts.add(new ConstructionSitePercept(xpos, ypos, nearPylon));
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();
		jnibwapi.Map map = api.getMap();
		int mapWidth = map.getSize().getBX();
		int mapHeight = map.getSize().getBY();

		for (int x = 0; x < mapWidth; x += 3) {
			for (int y = 0; y < mapHeight; y += 3) {
				Position pos = new Position(x, y, Position.PosType.BUILD);
				if (map.isBuildable(pos)) {
					if (api.getSelf().getRace().getID() == RaceTypes.Terran.getID()) {
						perceiveTerran(pos, x, y, percepts);
					} else if (api.getSelf().getRace().getID() == RaceTypes.Zerg.getID()) {
						perceiveZerg(pos, x, y, percepts);
					} else if (api.getSelf().getRace().getID() == RaceTypes.Protoss.getID()) {
						perceiveProtosss(pos, x, y, percepts);
					}
				}
			}
		}
		toReturn.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), percepts);
		return toReturn;
	}
}
