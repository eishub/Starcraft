package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Region;
import eis.eis2java.translation.Filter;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.BasePercept;
import eisbw.percepts.ChokepointRegionPercept;
import eisbw.percepts.EnemyRacePercept;
import eisbw.percepts.MapPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.RegionPercept;

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
	public MapPerceiver(bwapi.Game api) {
		super(api);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> mapPercept = new HashSet<>(1);
		mapPercept.add(new MapPercept(this.api.mapWidth(), this.api.mapHeight()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		Set<Percept> enemyRacePercept = new HashSet<>(1);
		enemyRacePercept.add(new EnemyRacePercept(this.api.enemy().getRace().toString().toLowerCase()));
		toReturn.put(new PerceptFilter(Percepts.ENEMYRACE, Filter.Type.ONCE), enemyRacePercept);

		/** Distance calculation between resource groups and base location **/
		Map<Integer, Position> distanceMatrix = new HashMap<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isMineralField()) {
				if (!distanceMatrix.containsKey(u.getResourceGroup())) {
					distanceMatrix.put(u.getResourceGroup(), u.getPosition());
				}
			}
		}
		List<BaseLocation> baseLocations = BWTA.getBaseLocations();
		Set<Percept> basePercepts = new HashSet<>(baseLocations.size());
		for (BaseLocation location : baseLocations) {
			Percept basePercept = new BasePercept(location.isStartLocation(), location.getTilePosition().getX(),
					location.getTilePosition().getY(), BwapiUtility.getRegionId(location.getRegion(), this.api));
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		List<Chokepoint> chokePoints = BWTA.getChokepoints();
		Set<Percept> chokepointPercepts = new HashSet<>(chokePoints.size());
		for (Chokepoint cp : chokePoints) {
			TilePosition firstSide = cp.getSides().first.toTilePosition();
			TilePosition secondSide = cp.getSides().second.toTilePosition();
			Region firstRegion = cp.getRegions().first;
			Region secondRegion = cp.getRegions().second;
			Percept chokeRegionPercept = new ChokepointRegionPercept(firstSide.getX(), firstSide.getY(),
					secondSide.getX(), secondSide.getY(), BwapiUtility.getRegionId(firstRegion, this.api),
					BwapiUtility.getRegionId(secondRegion, this.api));
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		List<Region> regions = BWTA.getRegions();
		Set<Percept> regionPercepts = new HashSet<>(regions.size());
		for (Region r : regions) {
			bwapi.Region apir = this.api.getRegionAt(r.getCenter());
			TilePosition center = r.getCenter().toTilePosition();
			boolean highground = apir.isHigherGround();
			List<Region> neighbors = r.getReachableRegions();
			List<Parameter> connected = new ArrayList<>(neighbors.size());
			for (Region c : neighbors) {
				bwapi.Region apic = this.api.getRegionAt(c.getCenter());
				connected.add(new Numeral(apic.getID()));
			}
			Percept regionPercept = new RegionPercept(apir.getID(), center.getX(), center.getY(), highground,
					connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);

		return toReturn;
	}
}
