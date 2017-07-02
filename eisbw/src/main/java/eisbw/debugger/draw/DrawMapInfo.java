package eisbw.debugger.draw;

import java.util.List;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.Game;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;
import jnibwapi.BaseLocation;
import jnibwapi.ChokePoint;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.util.BWColor;

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
	protected void drawOnMap(JNIBWAPI api) throws TranslationException {
		drawBases(api);
		drawChokepoints(api);
		drawConstructionSites(api);
	}

	private void drawBases(JNIBWAPI api) throws TranslationException {
		for (BaseLocation base : api.getMap().getBaseLocations()) {
			api.drawCircle(base.getCenter(), 75, BWColor.Purple, false, false);
			api.drawText(base.getPosition(), base.getPosition().getBX() + ", " + base.getPosition().getBY(), false);
			if (base.isStartLocation()) {
				api.drawText(base.getCenter(), "Starting Location", false);
			}
		}
	}

	private void drawChokepoints(JNIBWAPI api) throws TranslationException {
		for (ChokePoint cp : api.getMap().getChokePoints()) {
			api.drawLine(cp.getFirstSide(), cp.getSecondSide(), BWColor.Yellow, false);
			api.drawCircle(cp.getCenter(), (int) cp.getRadius(), BWColor.Red, false, false);
			api.drawText(cp.getCenter(), "(" + cp.getCenter().getBX() + "," + cp.getCenter().getBY() + ")", false);
		}
	}

	private void drawConstructionSites(JNIBWAPI api) throws TranslationException {
		Translator translator = Translator.getInstance();
		List<Percept> percepts = this.game.getConstructionSites();
		int size = ConstructionSitePerceiver.steps;
		for (Percept percept : percepts) {
			List<Parameter> params = percept.getParameters();
			int xpos = translator.translate2Java(params.get(0), Integer.class);
			int ypos = translator.translate2Java(params.get(1), Integer.class);
			if (api.getSelf().getRace().getID() == RaceTypes.Terran.getID()) {
				api.drawBox(new Position(xpos, ypos, PosType.BUILD),
						new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Blue, false, false);
			} else {
				boolean bool = translator.translate2Java(params.get(3), Boolean.class);
				if (bool) {
					api.drawBox(new Position(xpos, ypos, PosType.BUILD),
							new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Blue, false, false);
				} else {
					api.drawBox(new Position(xpos, ypos, PosType.BUILD),
							new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Red, false, false);
				}
			}
		}
	}
}
