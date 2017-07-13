package eisbw.configuration;

import java.util.Map;
import java.util.Map.Entry;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - This class handles all the possible configurations.
 *
 */
public class Configuration {
	protected RaceString ownRace = null;
	protected RaceString enemyRace = new RaceString("random");
	protected String map = null;
	protected String scDir = "C:\\Starcraft";
	protected String autoMenu = "SINGLE_PLAYER";
	protected String gameType = "MELEE";
	protected int speed = 50;
	protected BooleanString debug = new BooleanString("false");
	protected BooleanString drawMapInfo = new BooleanString("false");
	protected BooleanString drawUnitInfo = new BooleanString("false");
	protected BooleanString invulnerable = new BooleanString("false");
	protected BooleanString mapAgent = new BooleanString("true");
	protected int seed = 0;

	/**
	 * The Configuration constructor.
	 *
	 * @param parameters
	 *            The given config parameters.
	 * @throws TranslationException
	 *             One of the config parameters is not valid.
	 */
	public Configuration(Map<String, Parameter> parameters) throws TranslationException {
		parseParams(parameters);
	}

	private void parseParams(Map<String, Parameter> parameters) throws TranslationException {
		Translator translator = Translator.getInstance();
		for (Entry<String, Parameter> entry : parameters.entrySet()) {
			ParamEnum param = translator.translate2Java(new Identifier(entry.getKey()), ParamEnum.class);
			switch (param) {
			case OWN_RACE:
				this.ownRace = translator.translate2Java(entry.getValue(), RaceString.class);
				break;
			case ENEMY_RACE:
				this.enemyRace = translator.translate2Java(entry.getValue(), RaceString.class);
				break;
			case MAP:
				this.map = translator.translate2Java(entry.getValue(), String.class);
				break;
			case SC_DIR:
				this.scDir = translator.translate2Java(entry.getValue(), String.class);
				break;
			case AUTO_MENU:
				this.autoMenu = translator.translate2Java(entry.getValue(), String.class);
				break;
			case GAME_TYPE:
				this.gameType = translator.translate2Java(entry.getValue(), String.class);
				break;
			case SPEED:
				this.speed = translator.translate2Java(entry.getValue(), int.class);
				break;
			case DEBUG:
				this.debug = translator.translate2Java(entry.getValue(), BooleanString.class);
				break;
			case DRAWMAPINFO:
				this.drawMapInfo = translator.translate2Java(entry.getValue(), BooleanString.class);
				break;
			case DRAWUNITINFO:
				this.drawUnitInfo = translator.translate2Java(entry.getValue(), BooleanString.class);
				break;
			case INVULNERABLE:
				this.invulnerable = translator.translate2Java(entry.getValue(), BooleanString.class);
				break;
			case MAPAGENT:
				this.mapAgent = translator.translate2Java(entry.getValue(), BooleanString.class);
				break;
			case SEED:
				this.seed = translator.translate2Java(entry.getValue(), Integer.class);
				break;
			default:
				// Unreachable clause.
				break;
			}
		}
	}

	public String getOwnRace() {
		return this.ownRace.getData();
	}

	public String getEnemyRace() {
		return this.enemyRace.getData();
	}

	public String getMap() {
		return this.map;
	}

	public String getScDir() {
		return this.scDir;
	}

	public String getAutoMenu() {
		return this.autoMenu.toUpperCase();
	}

	public String getGameType() {
		return this.gameType.toUpperCase();
	}

	public int getSpeed() {
		return this.speed;
	}

	public boolean getDebug() {
		return this.debug.getValue();
	}

	public boolean getDrawMapInfo() {
		return this.drawMapInfo.getValue();
	}

	public boolean getDrawUnitInfo() {
		return this.drawUnitInfo.getValue();
	}

	public boolean getInvulnerable() {
		return this.invulnerable.getValue();
	}

	public boolean getMapAgent() {
		return this.mapAgent.getValue();
	}

	public int getSeed() {
		return this.seed;
	}
}
