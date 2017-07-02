package eisbw.percepts.perceivers;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.ConstructionSitePercept;
import eisbw.percepts.Percepts;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
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
	 * @param xpos
	 *            The x-coordinate.
	 * @param ypos
	 *            The y-coordinate.
	 * @param illegals
	 *            A list of illegal build places.
	 * @return Check whether the given ConstructionSite is legal or not.
	 */
	private boolean checkConstructionSite(int xpos, int ypos, List<Point> illegals) {
		Point possible = new Point(xpos, ypos);
		for (Point illegal : illegals) {
			if (illegal.distance(possible) < 3) {
				return false;
			}
		}
		return true;
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
	private void perceiveTerran(Position pos, int xpos, int ypos, List<Point> illegals, Set<Percept> percepts) {
		if (checkConstructionSite(xpos, ypos, illegals)
				&& this.api.canBuildHere(pos, UnitType.UnitTypes.Terran_Command_Center, true)) {
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
	private void perceiveProtosss(Position pos, int xpos, int ypos, List<Point> illegals, Set<Percept> percepts) {
		if (checkConstructionSite(xpos, ypos, illegals)
				&& this.api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Nexus, true)) {
			boolean nearPylon = this.api.canBuildHere(pos, UnitType.UnitTypes.Protoss_Gateway, true);
			percepts.add(new ConstructionSitePercept(xpos, ypos, nearPylon));
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
	private void perceiveZerg(Position pos, int xpos, int ypos, List<Point> illegals, Set<Percept> percepts) {
		if (checkConstructionSite(xpos, ypos, illegals) && this.api.canBuildHere(pos, UnitTypes.Zerg_Hatchery, true)) {
			boolean creep = this.api.canBuildHere(pos, UnitTypes.Zerg_Defiler_Mound, true);
			percepts.add(new ConstructionSitePercept(xpos, ypos, creep));
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> percepts = new HashSet<>();
		jnibwapi.Map map = this.api.getMap();
		int mapWidth = map.getSize().getBX();
		int mapHeight = map.getSize().getBY();

		List<Point> illegals = new LinkedList<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && (u.getType().isMineralField()
					|| u.getType().getID() == UnitTypes.Resource_Vespene_Geyser.getID())) {
				illegals.add(new Point(u.getTilePosition().getBX(), u.getTilePosition().getBY()));
			}
		}

		for (int x = 0; x < mapWidth; x += 3) {
			for (int y = 0; y < mapHeight; y += 3) {
				Position pos = new Position(x, y, Position.PosType.BUILD);
				if (map.isBuildable(pos)) {
					if (this.api.getSelf().getRace().getID() == RaceTypes.Terran.getID()) {
						perceiveTerran(pos, x, y, illegals, percepts);
					} else if (this.api.getSelf().getRace().getID() == RaceTypes.Protoss.getID()) {
						perceiveProtosss(pos, x, y, illegals, percepts);
					} else if (this.api.getSelf().getRace().getID() == RaceTypes.Zerg.getID()) {
						perceiveZerg(pos, x, y, illegals, percepts);
					}
				}
			}
		}
		toReturn.put(new PerceptFilter(Percepts.CONSTRUCTIONSITE, Filter.Type.ALWAYS), percepts);
		return toReturn;
	}
}
