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
 */
public class MapPerceiver extends Perceiver {
	/**
	 * The MapPerceiver constructor.
	 *
	 * @param api The BWAPI
	 */
	public MapPerceiver(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public void perceive(final Map<PerceptFilter, List<Percept>> toReturn) {
		final jnibwapi.Map map = this.api.getMap();

		final List<Percept> mapPercept = new ArrayList<>(1);
		final String mapname = map.getName().replaceAll("[^\\x20-\\x7E]", "");
		mapPercept.add(new MapPercept(mapname, map.getSize().getBX(), map.getSize().getBY()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		final Player self = this.api.getSelf();
		if (self != null) {
			final List<Percept> ownRacePercept = new ArrayList<>(1);
			ownRacePercept.add(new OwnRacePercept(self.getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.OWNRACE, Filter.Type.ONCE), ownRacePercept);
		}
		final Set<Player> enemies = this.api.getEnemies();
		if (!enemies.isEmpty()) {
			final List<Percept> enemyRacePercept = new ArrayList<>(1);
			final Player enemy = enemies.iterator().next();
			enemyRacePercept.add(new EnemyPlayerPercept(enemy.getName(), enemy.getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.ENEMYPLAYER, Filter.Type.ONCE), enemyRacePercept);
		} // FIXME: we only support 1 enemy now

		final List<BaseLocation> bases = map.getBaseLocations();
		final List<Percept> basePercepts = new ArrayList<>(bases.size());
		for (final BaseLocation location : bases) {
			final Position pos = location.getPosition();
			final Percept basePercept = new BasePercept(location.isStartLocation(), location.getMinerals(),
					location.getGas(), pos.getBX(), pos.getBY(), location.getRegion().getID());
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		final List<ChokePoint> chokepoints = map.getChokePoints();
		final List<Percept> chokepointPercepts = new ArrayList<>(chokepoints.size());
		for (final ChokePoint cp : chokepoints) {
			final Percept chokeRegionPercept = new ChokepointRegionPercept(cp.getFirstSide().getBX(),
					cp.getFirstSide().getBY(), cp.getSecondSide().getBX(), cp.getSecondSide().getBY(),
					cp.getFirstRegion().getID(), cp.getSecondRegion().getID());
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		final List<Region> regions = map.getRegions();
		final List<Percept> regionPercepts = new ArrayList<>(regions.size());
		for (final Region r : regions) {
			final Position center = r.getCenter();
			final int height = map.getGroundHeight(center);
			final Set<Region> connectedRegions = r.getConnectedRegions();
			final List<Parameter> connected = new ArrayList<>(connectedRegions.size());
			for (final Region c : connectedRegions) {
				connected.add(new Numeral(c.getID()));
			}
			final Percept regionPercept = new RegionPercept(r.getID(), center.getBX(), center.getBY(), height,
					connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);
	}
}
