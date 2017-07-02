package eisbw.percepts.perceivers;

import java.util.HashSet;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import eis.eis2java.translation.Filter;
//import eis.iilang.Identifier;
//import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackingPercept;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.NewUnitPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResourcesPercept;
import eisbw.units.ConditionHandler;

/**
 * @author Danny & Harm - The perceiver which handles all the unit percepts.
 *
 */
public class UnitsPerceiver extends Perceiver {
	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(bwapi.Game api) {
		super(api);
	}

	/**
	 * Sets some of the generic Unit percepts.
	 *
	 * @param units
	 *            The perceived units
	 * @param newunitpercepts
	 *            - list with newUnitPercepts; if this is passed (not null) we
	 *            assume we want friendly units in unitpercepts
	 * @param unitpercepts
	 *            - list with unitPercepts
	 * @param attackingpercepts
	 *            - list with attackingPercepts
	 * @param percepts
	 *            The list of percepts
	 * @param toReturn
	 *            - the map that will be returned
	 */
	private void setUnitPercepts(List<Unit> units, Set<Percept> newunitpercepts, Set<Percept> unitpercepts,
			Set<Percept> attackingpercepts) {
		for (Unit u : units) {
			if (!BwapiUtility.isValid(u)) {
				continue;
			}
			ConditionHandler conditionHandler = new ConditionHandler(this.api, u);
			if (newunitpercepts != null) {
				String unittype = (u.getType() == UnitType.Zerg_Egg) ? u.getBuildType().toString()
						: BwapiUtility.getName(u.getType());
				unitpercepts.add(new FriendlyPercept(u.getID(), unittype, conditionHandler.getConditions()));
				if (u.isBeingConstructed()) {
					newunitpercepts.add(new NewUnitPercept(u.getID(), u.getTilePosition().getX(),
							u.getTilePosition().getY(), u.getRegion().getID()));
				}
			} else {
				unitpercepts.add(new EnemyPercept(u.getID(), BwapiUtility.getName(u.getType()), u.getHitPoints(),
						u.getShields(), u.getEnergy(), conditionHandler.getConditions(), u.getTilePosition().getX(),
						u.getTilePosition().getY(), u.getRegion().getID()));
				if (u.getType().canAttack()) {
					Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new AttackingPercept(u.getID(), target.getID()));
					}
				}
			}
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> newunitpercepts = new HashSet<>();
		Set<Percept> friendlypercepts = new HashSet<>();
		Set<Percept> enemypercepts = new HashSet<>();
		Set<Percept> attackingpercepts = new HashSet<>();

		// perceive friendly units
		Player self = this.api.self();
		setUnitPercepts(self.getUnits(), newunitpercepts, friendlypercepts, attackingpercepts);
		// perceive enemy units
		Player enemy = this.api.enemy();
		setUnitPercepts(enemy.getUnits(), null, enemypercepts, attackingpercepts);

		toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		toReturn.put(new PerceptFilter(Percepts.NEWUNIT, Filter.Type.ALWAYS), newunitpercepts);

		Set<Percept> resourcePercept = new HashSet<>(1);
		resourcePercept.add(new ResourcesPercept(self.minerals(), self.gas(), self.supplyUsed(), self.supplyTotal()));
		toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);

		return toReturn;
	}
}
