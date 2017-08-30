package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Danny & Harm - The Enemy Race percept.
 *
 */
public class EnemyRacePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 *            The name of the enemy race.
	 */
	public EnemyRacePercept(String name) {
		super(Percepts.ENEMYRACE, new Identifier(name));
	}
}
