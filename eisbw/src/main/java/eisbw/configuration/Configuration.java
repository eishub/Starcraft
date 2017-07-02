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
	protected String scDir = null;
	protected String autoMenu = "OFF";
	protected BooleanString debug = new BooleanString("false");
	protected BooleanString drawMapInfo = new BooleanString("false");
	protected BooleanString drawUnitInfo = new BooleanString("false");
	protected int speed = 20;
	protected BooleanString invulnerable = new BooleanString("false");
	protected BooleanString mapAgent = new BooleanString("false");

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
			case DEBUG:
				setDebug(translator.translate2Java(entry.getValue(), BooleanString.class));
				break;
			case DRAWMAPINFO:
				setDrawMapInfo(translator.translate2Java(entry.getValue(), BooleanString.class));
				break;
			case DRAWUNITINFO:
				setDrawUnitInfo(translator.translate2Java(entry.getValue(), BooleanString.class));
				break;
			case MAP:
				setMap(translator.translate2Java(entry.getValue(), String.class));
				break;
			case OWN_RACE:
				setOwnRace(translator.translate2Java(entry.getValue(), RaceString.class));
				break;
			case ENEMY_RACE:
				setEnemyRace(translator.translate2Java(entry.getValue(), RaceString.class));
				break;
			case SC_DIR:
				setScDir(translator.translate2Java(entry.getValue(), String.class));
				break;
			case AUTO_MENU:
				setAutoMenu(translator.translate2Java(entry.getValue(), String.class));
				break;
			case SPEED:
				setSpeed(translator.translate2Java(entry.getValue(), int.class));
				break;
			case INVULNERABLE:
				setInvulnerable(translator.translate2Java(entry.getValue(), BooleanString.class));
				break;
			case MAPAGENT:
				setMapAgent(translator.translate2Java(entry.getValue(), BooleanString.class));
				break;
			default:
				// Unreachable clause.
				break;
			}
		}
	}

	private void setEnemyRace(RaceString race) {
		this.enemyRace = race;
	}

	private void setInvulnerable(BooleanString inv) {
		this.invulnerable = inv;
	}

	private void setMapAgent(BooleanString mapagent) {
		this.mapAgent = mapagent;
	}

	private void setSpeed(int speed) {
		this.speed = speed;
	}

	private void setScDir(String dir) {
		this.scDir = dir;
	}

	private void setDebug(BooleanString debug) {
		this.debug = debug;
	}

	private void setDrawMapInfo(BooleanString draw) {
		this.drawMapInfo = draw;
	}

	private void setDrawUnitInfo(BooleanString draw) {
		this.drawUnitInfo = draw;
	}

	private void setMap(String map) {
		this.map = map;
	}

	private void setAutoMenu(String autoMenu) {
		this.autoMenu = autoMenu;
	}

	private void setOwnRace(RaceString race) {
		this.ownRace = race;
	}

	public boolean getInvulnerable() {
		return this.invulnerable.getValue();
	}

	public boolean getMapAgent() {
		return this.mapAgent.getValue();
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

	public String getMap() {
		return this.map;
	}

	public String getOwnRace() {
		return this.ownRace.getData();
	}

	public String getEnemyRace() {
		return this.enemyRace.getData();
	}

	public String getScDir() {
		return this.scDir;
	}

	public String getAutoMenu() {
		return this.autoMenu;
	}
}
