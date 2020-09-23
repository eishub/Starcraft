package eisbw.percepts;

/**
 * @author Danny & Harm - The data class with all the percept names.
 */
public enum Percepts {
	// defensiveMatrix/1
	DEFENSIVEMATRIX("defensiveMatrix", 1),
	// status/8
	STATUS("status", 8),
	// resources/4
	RESOURCES("resources", 4),
	// mineralField/5
	MINERALFIELD("mineralField", 5),
	// base/6
	BASE("base", 6),
	// chokepoint/6
	CHOKEPOINT("chokepoint", 6),
	// map/3
	MAP("map", 3),
	// vespeneGeyser/5
	VESPENEGEYSER("vespeneGeyser", 5),
	// self/2
	SELF("self", 2),
	// queueSize/1
	QUEUESIZE("queueSize", 1),
	// attacking/2
	ATTACKING("attacking", 2),
	// constructionSite/3
	CONSTRUCTIONSITE("constructionSite", 3),
	// ownRace/1
	OWNRACE("ownRace", 1),
	// enemyPlayer/2
	ENEMYPLAYER("enemyPlayer", 2),
	// enemy/11
	ENEMY("enemy", 11),
	// friendly/2
	FRIENDLY("friendly", 2),
	// underConstruction/6
	UNDERCONSTRUCTION("underConstruction", 6),
	// unitLoaded/1
	UNITLOADED("unitLoaded", 1),
	// researching/1
	RESEARCHING("researching", 1),
	// winner/1
	WINNER("winner", 1),
	// gameframe/1
	FRAME("gameframe", 1),
	// nuke/3
	NUKE("nuke", 3),
	// region/5
	REGION("region", 5),
	// order/6
	ORDER("order", 6),
	// researched/1
	RESEARCHED("researched", 1);

	private final String name;
	private final int arity;

	private Percepts(final String name, final int arity) {
		this.name = name;
		this.arity = arity;
	}

	public int getArity() {
		return this.arity;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
