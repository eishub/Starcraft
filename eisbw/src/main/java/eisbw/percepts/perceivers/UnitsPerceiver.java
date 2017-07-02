package eisbw.percepts.perceivers;

import java.util.HashSet;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
//import eis.iilang.Identifier;
//import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.Attacking;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.NewUnitPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.UnitRegionPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Region;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the unit percepts.
 *
 */
public class UnitsPerceiver extends Perceiver {
	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(JNIBWAPI api) {
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
			Set<Percept> attackingpercepts, Set<Percept> regionpercepts) {
		for (Unit u : units) {
			if (u.isBeingConstructed() && u.isLoaded()) {
				continue; // Fix for the phantom marines bug
			}
			ConditionHandler conditionHandler = new ConditionHandler(this.api, u);
			if (newunitpercepts != null) {
				String unittype = (u.getType().getID() == UnitTypes.Zerg_Egg.getID()) ? u.getBuildType().getName()
						: BwapiUtility.getUnitTypeName(u.getType());
				unitpercepts.add(new FriendlyPercept(unittype, u.getID(), conditionHandler.getConditions()));
				if (u.isBeingConstructed()) {
					newunitpercepts
							.add(new NewUnitPercept(u.getID(), u.getPosition().getBX(), u.getPosition().getBY()));
				}
			} else {
				unitpercepts.add(new EnemyPercept(BwapiUtility.getUnitTypeName(u.getType()), u.getID(),
						u.getHitPoints(), u.getShields(), conditionHandler.getConditions(), u.getPosition().getBX(),
						u.getPosition().getBY()));
				if (u.getType().isAttackCapable()) {
					Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new Attacking(u.getID(), target.getID()));
					}
				}
			}
			Region region = (this.api.getMap() == null) ? null : this.api.getMap().getRegion(u.getPosition());
			if (region != null) {
				regionpercepts.add(new UnitRegionPercept(u.getID(), region.getID()));
			} // FIXME: this should be in the UnitPerceiver for friendlies
				// (without id parameter) and in the enemy percept for enemies
		}
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> newunitpercepts = new HashSet<>();
		Set<Percept> friendlypercepts = new HashSet<>();
		Set<Percept> enemypercepts = new HashSet<>();
		Set<Percept> attackingpercepts = new HashSet<>();
		Set<Percept> regionpercepts = new HashSet<>();

		// perceive friendly units
		setUnitPercepts(this.api.getMyUnits(), newunitpercepts, friendlypercepts, attackingpercepts, regionpercepts);
		// perceive enemy units
		setUnitPercepts(this.api.getEnemyUnits(), null, enemypercepts, attackingpercepts, regionpercepts);

		toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		toReturn.put(new PerceptFilter(Percepts.NEWUNIT, Filter.Type.ON_CHANGE), newunitpercepts);
		toReturn.put(new PerceptFilter(Percepts.UNITREGION, Filter.Type.ON_CHANGE), regionpercepts);

		return toReturn;
	}
}
