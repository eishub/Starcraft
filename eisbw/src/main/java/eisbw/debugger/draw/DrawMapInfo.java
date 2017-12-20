package eisbw.debugger.draw;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.MapDrawer;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.Color;
import org.openbw.bwapi4j.type.Race;

import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Region;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eis.iilang.TruthValue;
import eisbw.Game;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;

/**
 * @author Harm & Danny.
 *
 */
public class DrawMapInfo extends IDraw {
	private final BW bwapi;
	private final BWTA bwta;

	/**
	 * Draw map information (bases, chokepoints, construction sites).
	 *
	 * @param game
	 *            The current game.
	 */
	public DrawMapInfo(Game game, BW bwapi, BWTA bwta) {
		super(game);
		this.bwapi = bwapi;
		this.bwta = bwta;
	}

	@Override
	protected void doDraw(MapDrawer api) {
		drawRegions(api);
		drawBases(api);
		drawChokepoints(api);
		drawConstructionSites(api);
	}

	private void drawRegions(MapDrawer api) {
		for (Region region : this.bwta.getRegions()) {
			// FIXME: very expensive draw
			// Position[] p = region.getPolygon();
			// for (int j = 0; j < p.length; ++j) {
			// api.drawLine(p[j], p[(j + 1) % p.length], BWColor.Green, false);
			// }
			TilePosition center = region.getCenter().toTilePosition();
			api.drawTextMap(region.getCenter(),
					"Region " + region.hashCode() + " (" + center.getX() + "," + center.getY() + ")");
		}
	}

	private void drawBases(MapDrawer api) {
		for (BaseLocation base : this.bwta.getBaseLocations()) {
			api.drawCircleMap(base.getPosition(), 75, Color.PURPLE, false);
			TilePosition pos = base.getTilePosition();
			api.drawTextMap(base.getPosition(), pos.getX() + ", " + pos.getY());
			if (base.isStartLocation()) {
				api.drawTextMap(base.getPosition(), "Starting Location");
			}
		}
	}

	private void drawChokepoints(MapDrawer api) {
		for (Chokepoint cp : this.bwta.getChokepoints()) {
			api.drawLineMap(cp.getSides().first, cp.getSides().second, Color.YELLOW);
			api.drawCircleMap(cp.getCenter(), (int) cp.getWidth(), Color.RED, false);
			TilePosition center = cp.getCenter().toTilePosition();
			api.drawTextMap(cp.getCenter(), "(" + center.getX() + "," + center.getY() + ")");
		}
	}

	private void drawConstructionSites(MapDrawer api) {
		List<Percept> percepts = this.game.getConstructionSites();
		int size = ConstructionSitePerceiver.steps;
		for (Percept percept : percepts) {
			List<Parameter> params = percept.getParameters();
			int xpos = ((Numeral) params.get(0)).getValue().intValue();
			int ypos = ((Numeral) params.get(1)).getValue().intValue();
			Position pos1 = new TilePosition(xpos, ypos).toPosition();
			Position pos2 = new TilePosition(xpos + size, ypos + size).toPosition();
			boolean bool = (this.bwapi.getInteractionHandler().self().getRace() == Race.Terran)
					|| ((TruthValue) params.get(3)).getBooleanValue();
			api.drawBoxMap(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), bool ? Color.BLUE : Color.RED, false);
		}
	}
}
