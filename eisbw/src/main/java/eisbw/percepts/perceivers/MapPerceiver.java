package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.BasePercept;
import eisbw.percepts.ChokepointCenterPercept;
import eisbw.percepts.ChokepointPercept;
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
import jnibwapi.Unit;

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
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		jnibwapi.Map map = this.api.getMap();

		Set<Percept> mapPercept = new HashSet<>(1);
		mapPercept.add(new MapPercept(map.getSize().getBX(), map.getSize().getBY()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		if (!this.api.getEnemies().isEmpty()) {
			Set<Percept> enemyRacePercept = new HashSet<>(1);
			enemyRacePercept.add(
					new EnemyRacePercept(this.api.getEnemies().iterator().next().getRace().getName().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.ENEMYRACE, Filter.Type.ONCE), enemyRacePercept);
		} // FIXME: we only support 1 enemy now

		/** Distance calculation between resource groups and base location **/
		Map<Integer, Position> distanceMatrix = new HashMap<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isMineralField()) {
				if (!distanceMatrix.containsKey(u.getResourceGroup())) {
					distanceMatrix.put(u.getResourceGroup(), u.getPosition());
				}
			}
		}
		Set<Percept> basePercepts = new HashSet<>(map.getBaseLocations().size());
		for (BaseLocation location : map.getBaseLocations()) {
			int resourcegroup = -1;
			double distance = Integer.MAX_VALUE;
			for (Entry<Integer, Position> resource : distanceMatrix.entrySet()) {
				double newDist = resource.getValue().getBDistance(location.getPosition());
				if (newDist < distance) {
					resourcegroup = resource.getKey();
					distance = newDist;
				}
			}

			Percept basePercept = new BasePercept(location.getPosition().getBX(), location.getPosition().getBY(),
					location.isStartLocation(), resourcegroup);
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		Set<Percept> chokepointPercepts = new HashSet<>(map.getChokePoints().size());
		for (ChokePoint cp : map.getChokePoints()) {
			Percept chokeCenterPercept = new ChokepointCenterPercept(cp.getCenter().getBX(), cp.getCenter().getBY(),
					(int) cp.getRadius());
			chokepointPercepts.add(chokeCenterPercept);
			Percept chokePercept = new ChokepointPercept(cp.getFirstSide().getBX(), cp.getFirstSide().getBY(),
					cp.getSecondSide().getBX(), cp.getSecondSide().getBY());
			chokepointPercepts.add(chokePercept);
			Percept chokeRegionPercept = new ChokepointRegionPercept(cp.getFirstSide().getBX(),
					cp.getFirstSide().getBY(), cp.getSecondSide().getBX(), cp.getSecondSide().getBY(),
					cp.getFirstRegion().getID(), cp.getSecondRegion().getID());
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		Set<Percept> regionPercepts = new HashSet<>(map.getRegions().size());
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

		return toReturn;
	}
}
