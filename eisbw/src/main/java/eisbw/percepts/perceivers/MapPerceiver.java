package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eis.eis2java.translation.Filter;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.percepts.BasePercept;
import eisbw.percepts.ChokepointRegionPercept;
import eisbw.percepts.EnemyRacePercept;
import eisbw.percepts.MapPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.RegionPercept;
import jnibwapi.BaseLocation;
import jnibwapi.ChokePoint;
import jnibwapi.JNIBWAPI;
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
		mapPercept.add(new MapPercept(map.getSize().getBX(), map.getSize().getBY()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		if (!this.api.getEnemies().isEmpty()) {
			List<Percept> enemyRacePercept = new ArrayList<>(1);
			enemyRacePercept.add(
					new EnemyRacePercept(this.api.getEnemies().iterator().next().getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.ENEMYRACE, Filter.Type.ONCE), enemyRacePercept);
		} // FIXME: we only support 1 enemy now

		List<Percept> basePercepts = new ArrayList<>(map.getBaseLocations().size());
		for (BaseLocation location : map.getBaseLocations()) {
			Position pos = location.getPosition();
			Percept basePercept = new BasePercept(location.isStartLocation(), location.getMinerals(), location.getGas(),
					pos.getBX(), pos.getBY(), location.getRegion().getID());
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		List<Percept> chokepointPercepts = new ArrayList<>(map.getChokePoints().size());
		for (ChokePoint cp : map.getChokePoints()) {
			Percept chokeRegionPercept = new ChokepointRegionPercept(cp.getFirstSide().getBX(),
					cp.getFirstSide().getBY(), cp.getSecondSide().getBX(), cp.getSecondSide().getBY(),
					cp.getFirstRegion().getID(), cp.getSecondRegion().getID());
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		List<Percept> regionPercepts = new ArrayList<>(map.getRegions().size());
		for (Region r : map.getRegions()) {
			Position center = r.getCenter();
			int height = map.getGroundHeight(center);
			List<Parameter> connected = new ArrayList<>(r.getConnectedRegions().size());
			for (Region c : r.getConnectedRegions()) {
				connected.add(new Numeral(c.getID()));
			}
			Percept regionPercept = new RegionPercept(r.getID(), center.getBX(), center.getBY(), height, connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);
	}
}
