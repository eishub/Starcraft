package eisbw.debugger.draw;

import java.util.List;

import bwapi.Color;
import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Region;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.Game;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;

/**
 * @author Harm & Danny.
 *
 */
public class DrawMapInfo extends IDraw {
	/**
	 * Draw map information (bases, chokepoints, construction sites).
	 *
	 * @param game
	 *            - the game object
	 */
	public DrawMapInfo(Game game) {
		super(game);
	}

	@Override
	protected void drawOnMap(bwapi.Game api) throws TranslationException {
		drawRegions(api);
		drawBases(api);
		drawChokepoints(api);
		drawConstructionSites(api);
	}

	private void drawRegions(bwapi.Game api) throws TranslationException {
		for (Region region : BWTA.getRegions()) {
			List<Position> p = region.getPolygon().getPoints();
			for (int j = 0; j < p.size(); ++j) {
				api.drawLineMap(p.get(j), p.get((j + 1) % p.size()), Color.Green);
			}
			api.drawTextMap(region.getCenter(),
					"Region " + BwapiUtility.getRegionId(region, api) + " ("
							+ region.getCenter().toTilePosition().getX() + ","
							+ region.getCenter().toTilePosition().getY() + ")");
		}
	}

	private void drawBases(bwapi.Game api) throws TranslationException {
		for (BaseLocation base : BWTA.getBaseLocations()) {
			api.drawCircleMap(base.getPosition(), 75, Color.Purple, false);
			api.drawTextMap(base.getPosition(), base.getPosition().getX() + ", " + base.getPosition().getY());
			if (base.isStartLocation()) {
				api.drawTextMap(base.getPosition(), "Starting Location");
			}
		}
	}

	private void drawChokepoints(bwapi.Game api) throws TranslationException {
		for (Chokepoint cp : BWTA.getChokepoints()) {
			api.drawLineMap(cp.getSides().first.getPoint(), cp.getSides().second.getPoint(), Color.Yellow);
			api.drawCircleMap(cp.getCenter(), (int) cp.getWidth(), Color.Red, false);
			api.drawTextMap(cp.getCenter(),
					"(" + cp.getCenter().toTilePosition().getX() + "," + cp.getCenter().toTilePosition().getY() + ")");
		}
	}

	private void drawConstructionSites(bwapi.Game api) throws TranslationException {
		Translator translator = Translator.getInstance();
		List<Percept> percepts = this.game.getConstructionSites();
		int size = ConstructionSitePerceiver.steps;
		for (Percept percept : percepts) {
			List<Parameter> params = percept.getParameters();
			int xpos = translator.translate2Java(params.get(0), Integer.class);
			int ypos = translator.translate2Java(params.get(1), Integer.class);
			if (api.self().getRace() == Race.Terran) {
				api.drawBoxMap(new TilePosition(xpos, ypos).toPosition(),
						new TilePosition(xpos + size, ypos + size).toPosition(), Color.Blue, false);
			} else {
				boolean bool = translator.translate2Java(params.get(3), Boolean.class);
				if (bool) {
					api.drawBoxMap(new TilePosition(xpos, ypos).toPosition(),
							new TilePosition(xpos + size, ypos + size).toPosition(), Color.Blue, false);
				} else {
					api.drawBoxMap(new TilePosition(xpos, ypos).toPosition(),
							new TilePosition(xpos + size, ypos + size).toPosition(), Color.Red, false);
				}
			}
		}
	}
}
