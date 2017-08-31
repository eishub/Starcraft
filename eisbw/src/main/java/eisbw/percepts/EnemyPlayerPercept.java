package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Wesley - The Enemy player percept.
 *
 */
public class EnemyPlayerPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The EnemyPlayerPercept constructor.
	 *
	 * @param name
	 *            The name of the enemy
	 * @param race
	 *            The race of the enemy
	 */
	public EnemyPlayerPercept(String name, String race) {
		super(Percepts.ENEMYPLAYER, new Identifier(name), new Identifier(race));
	}
}
