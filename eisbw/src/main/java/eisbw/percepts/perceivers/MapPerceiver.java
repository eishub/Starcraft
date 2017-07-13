package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bwapi.Pair;
import bwapi.Position;
import bwapi.TilePosition;
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
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> mapPercept = new ArrayList<>(1);
		mapPercept.add(new MapPercept(this.api.mapWidth(), this.api.mapHeight()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		List<Percept> enemyRacePercept = new ArrayList<>(1);
		enemyRacePercept.add(new EnemyRacePercept(BwapiUtility.getName(this.api.enemy().getRace())));
		toReturn.put(new PerceptFilter(Percepts.ENEMYRACE, Filter.Type.ONCE), enemyRacePercept);

		List<BaseLocation> baseLocations = BWTA.getBaseLocations();
		List<Percept> basePercepts = new ArrayList<>(baseLocations.size());
		for (BaseLocation location : baseLocations) {
			TilePosition pos = location.getTilePosition();
			Percept basePercept = new BasePercept(location.isStartLocation(), pos.getX(), pos.getY(),
					BwapiUtility.getRegion(pos, this.api));
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		List<Chokepoint> chokePoints = BWTA.getChokepoints();
		List<Percept> chokepointPercepts = new ArrayList<>(chokePoints.size());
		for (Chokepoint cp : chokePoints) {
			Pair<Position, Position> sides = cp.getSides();
			TilePosition firstSide = sides.first.toTilePosition();
			TilePosition secondSide = sides.second.toTilePosition();
			Pair<Region, Region> regions = cp.getRegions();
			int firstRegion = BwapiUtility.getRegion(regions.first.getCenter().toTilePosition(), this.api);
			int secondRegion = BwapiUtility.getRegion(regions.second.getCenter().toTilePosition(), this.api);
			Percept chokeRegionPercept = new ChokepointRegionPercept(firstSide.getX(), firstSide.getY(),
					secondSide.getX(), secondSide.getY(), firstRegion, secondRegion);
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		List<Region> regions = BWTA.getRegions();
		List<Percept> regionPercepts = new ArrayList<>(regions.size());
		for (Region r : regions) {
			TilePosition center = r.getCenter().toTilePosition();
			bwapi.Region apir = this.api.getRegionAt(center.toPosition());
			boolean highground = apir.isHigherGround();
			List<Region> neighbors = r.getReachableRegions();
			List<Parameter> connected = new ArrayList<>(neighbors.size());
			for (Region c : neighbors) {
				int apic = BwapiUtility.getRegion(c.getCenter().toTilePosition(), this.api);
				connected.add(new Numeral(apic));
			}
			Percept regionPercept = new RegionPercept(apir.getID(), center.getX(), center.getY(), highground,
					connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);
	}
}
