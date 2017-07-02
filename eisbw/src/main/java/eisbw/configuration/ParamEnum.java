package eisbw.configuration;

/**
 * @author Danny & Harm - The parameter enums.
 *
 */
public enum ParamEnum {
	MAP("map"), OWN_RACE("own_race"), DEBUG("debug"), DRAWMAPINFO("draw_mapinfo"),
	//
	DRAWUNITINFO("draw_unitinfo"), SC_DIR("starcraft_location"), AUTO_MENU("auto_menu"),
	//
	ENEMY_RACE("enemy_race"), SPEED("game_speed"), INVULNERABLE("invulnerable"), MAPAGENT("map_agent");

	private final String parameter;

	private ParamEnum(String name) {
		this.parameter = name;
	}

	public String getParam() {
		return this.parameter;
	}
}
