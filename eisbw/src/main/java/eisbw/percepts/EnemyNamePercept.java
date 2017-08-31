package eisbw.percepts;

import eis.iilang.Identifier;

/**
 * @author Wesley - The Enemy name percept.
 *
 */
public class EnemyNamePercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * The EnemyNamePercept constructor.
	 *
	 * @param name
	 *            The name of the enemy
	 */
	public EnemyNamePercept(String name) {
		super(Percepts.ENEMYNAME, new Identifier(name));
	}
}
