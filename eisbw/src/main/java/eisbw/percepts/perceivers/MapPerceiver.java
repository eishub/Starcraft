package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.BWMap;
import org.openbw.bwapi4j.Player;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.unit.MineralPatch;
import org.openbw.bwapi4j.unit.VespeneGeyser;

import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Region;
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
	public MapPerceiver(BW bwapi, BWTA bwta) {
		super(bwapi, bwta);
	}

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		BWMap map = this.bwapi.getBWMap();

		List<Percept> mapPercept = new ArrayList<>(1);
		String mapname = map.toString().replaceAll("[^\\x20-\\x7E]", "");
		mapPercept.add(new MapPercept(mapname, map.mapWidth(), map.mapHeight()));
		toReturn.put(new PerceptFilter(Percepts.MAP, Filter.Type.ONCE), mapPercept);

		Player self = this.bwapi.getInteractionHandler().self();
		if (self != null) {
			List<Percept> ownRacePercept = new ArrayList<>(1);
			ownRacePercept.add(new OwnRacePercept(self.getRace().toString().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.OWNRACE, Filter.Type.ONCE), ownRacePercept);
		}
		Player enemy = this.bwapi.getInteractionHandler().enemy();
		if (enemy != null) {
			List<Percept> enemyRacePercept = new ArrayList<>(1);
			enemyRacePercept.add(new EnemyPlayerPercept(enemy.getName(), enemy.getRace().toString().toLowerCase()));
			toReturn.put(new PerceptFilter(Percepts.ENEMYPLAYER, Filter.Type.ONCE), enemyRacePercept);
		} // FIXME: we only support 1 enemy now

		List<BaseLocation> bases = this.bwta.getBaseLocations();
		List<Percept> basePercepts = new ArrayList<>(bases.size());
		for (BaseLocation location : bases) {
			TilePosition pos = location.getTilePosition();
			int minerals = 0;
			for (MineralPatch patch : location.getMinerals()) {
				minerals += patch.getResources();
			}
			int gas = 0;
			for (VespeneGeyser geyser : location.getGeysers()) {
				gas += geyser.getResources();
			}
			Percept basePercept = new BasePercept(location.isStartLocation(), minerals, gas, pos.getX(), pos.getY(),
					location.getRegion().hashCode());
			basePercepts.add(basePercept);
		}
		toReturn.put(new PerceptFilter(Percepts.BASE, Filter.Type.ONCE), basePercepts);

		List<Chokepoint> chokepoints = this.bwta.getChokepoints();
		List<Percept> chokepointPercepts = new ArrayList<>(chokepoints.size());
		for (Chokepoint cp : chokepoints) {
			TilePosition first = cp.getSides().first.toTilePosition();
			TilePosition second = cp.getSides().second.toTilePosition();
			Percept chokeRegionPercept = new ChokepointRegionPercept(first.getX(), first.getY(), second.getX(),
					second.getY(), cp.getRegions().first.hashCode(), cp.getRegions().second.hashCode());
			chokepointPercepts.add(chokeRegionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.CHOKEPOINT, Filter.Type.ONCE), chokepointPercepts);

		List<Region> regions = this.bwta.getRegions();
		List<Percept> regionPercepts = new ArrayList<>(regions.size());
		for (Region r : regions) {
			TilePosition center = r.getCenter().toTilePosition();
			int height = map.getGroundHeight(center);
			List<Region> connectedRegions = r.getReachableRegions();
			List<Parameter> connected = new ArrayList<>(connectedRegions.size());
			for (Region c : connectedRegions) {
				connected.add(new Numeral(c.hashCode()));
			}
			Percept regionPercept = new RegionPercept(r.hashCode(), center.getX(), center.getY(), height, connected);
			regionPercepts.add(regionPercept);
		}
		toReturn.put(new PerceptFilter(Percepts.REGION, Filter.Type.ONCE), regionPercepts);
	}
}
