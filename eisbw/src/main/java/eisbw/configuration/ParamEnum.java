package eisbw.configuration;

/**
 * @author Danny & Harm - The parameter enums.
 */
public enum ParamEnum {
	MAP("map"), OWN_RACE("own_race"), DEBUG("debug"), DRAWMAPINFO("draw_mapinfo"), SEED("seed"),
	//
	DRAWUNITINFO("draw_unitinfo"), SC_DIR("starcraft_location"), AUTO_MENU("auto_menu"),
	//
	GAME_TYPE("game_type"), ENEMY_RACE("enemy_race"), SPEED("game_speed"), INVULNERABLE("invulnerable"),
	//
	MANAGERS("managers"), PERCEPTS("percepts");

	private final String parameter;

	private ParamEnum(final String name) {
		this.parameter = name;
	}

	public String getParam() {
		return this.parameter;
	}
}
