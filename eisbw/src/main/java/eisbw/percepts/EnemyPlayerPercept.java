package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Danny & Harm - The Enemy layer percept.
 *
 */
public class EnemyPlayerPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 *            The name of the enemy player.
	 * @param race
	 *            The enemy player's race.
	 */
	public EnemyPlayerPercept(String name, String race) {
		super(Percepts.ENEMYPLAYER, new Identifier(name), new Identifier(race));
	}
}
