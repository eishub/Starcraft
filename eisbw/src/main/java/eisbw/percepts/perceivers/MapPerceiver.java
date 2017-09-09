package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.percepts.BasePercept;
import eisbw.percepts.ChokepointRegionPercept;
import eisbw.percepts.EnemyPlayerPercept;
import eisbw.percepts.MapPercept;
import eisbw.percepts.OwnRacePercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.RegionPercept;
import jnibwapi.BaseLocation;
import jnibwapi.ChokePoint;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Region;

/**
 * @author Danny & Harm - The perceiver which handles all the map percepts.
 *
 */
public class MapPerceiver extends Perceiver {
	/**
	 * The MapPerceiver constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public MapPerceiver(JNIBWAPI api) {
		super(api);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		jnibwapi.Map map = this.api.getMap();

		List<Percept> mapPercept = new ArrayList<>(1);
		String mapname = map.getName().replaceAll("[^\\x20-\\x7E]", "");
		mapPercept.add(new MapPercept(mapname, map.getSize().getBX(), map.getSize().getBY()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		Player self = this.api.getSelf();
		if (self != null) {
			List<Percept> ownRacePercept = new ArrayList<>(1);
			ownRacePercept.add(new OwnRacePercept(self.getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.OWNRACE, Filter.Type.ONCE), ownRacePercept);
		}
		Set<Player> enemies = this.api.getEnemies();
		if (!enemies.isEmpty()) {
			List<Percept> enemyRacePercept = new ArrayList<>(1);
			Player enemy = enemies.iterator().next();
			enemyRacePercept.add(new EnemyPlayerPercept(enemy.getName(), enemy.getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.ENEMYPLAYER, Filter.Type.ONCE), enemyRacePercept);
		} // FIXME: we only support 1 enemy now

		List<BaseLocation> bases = map.getBaseLocations();
		List<Percept> basePercepts = new ArrayList<>(bases.size());
		for (BaseLocation location : bases) {
			Position pos = location.getPosition();
			Percept basePercept = new BasePercept(location.isStartLocation(), location.getMinerals(), location.getGas(),
					pos.getBX(), pos.getBY(), location.getRegion().getID());
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		List<ChokePoint> chokepoints = map.getChokePoints();
		List<Percept> chokepointPercepts = new ArrayList<>(chokepoints.size());
		for (ChokePoint cp : chokepoints) {
			Percept chokeRegionPercept = new ChokepointRegionPercept(cp.getFirstSide().getBX(),
					cp.getFirstSide().getBY(), cp.getSecondSide().getBX(), cp.getSecondSide().getBY(),
					cp.getFirstRegion().getID(), cp.getSecondRegion().getID());
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		List<Region> regions = map.getRegions();
		List<Percept> regionPercepts = new ArrayList<>(regions.size());
		for (Region r : regions) {
			Position center = r.getCenter();
			int height = map.getGroundHeight(center);
			Set<Region> connectedRegions = r.getConnectedRegions();
			List<Parameter> connected = new ArrayList<>(connectedRegions.size());
			for (Region c : connectedRegions) {
				connected.add(new Numeral(c.getID()));
			}
			Percept regionPercept = new RegionPercept(r.getID(), center.getBX(), center.getBY(), height, connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);
	}
}
