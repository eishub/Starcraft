package eisbw.percepts.perceivers;

import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eis.eis2java.translation.Filter;
//import eis.iilang.Identifier;
//import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackingPercept;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResourcesPercept;
import eisbw.percepts.UnderConstructionPercept;
import eisbw.percepts.VespeneGeyserPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
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

	@Override
	public void perceive(Map<PerceptFilter, List<Percept>> toReturn) {
		resourcesPercepts(toReturn);
		unitsPercepts(toReturn);
	}

	private void resourcesPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		Player self = this.api.getSelf();
		if (self != null) { // for tests
			List<Percept> resourcePercept = new ArrayList<>(1);
			resourcePercept.add(new ResourcesPercept(self.getMinerals(), self.getGas(), self.getSupplyUsed(),
					self.getSupplyTotal()));
			toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);
		}
		List<Percept> minerals = new LinkedList<>();
		List<Percept> geysers = new LinkedList<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (u.getType().isMineralField() && BwapiUtility.isValid(u)) {
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), getRegion(u));
				minerals.add(mineralfield);
			} else if (u.getType().getID() == UnitTypes.Resource_Vespene_Geyser.getID() && BwapiUtility.isValid(u)) {
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), getRegion(u));
				geysers.add(geyser);
			}
		}
		for (Unit u : this.api.getMyUnits()) {
			if (u.getType().isRefinery() && BwapiUtility.isValid(u)) {
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), getRegion(u));
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}

	private void unitsPercepts(Map<PerceptFilter, List<Percept>> toReturn) {
		List<Percept> newunitpercepts = new LinkedList<>();
		List<Percept> friendlypercepts = new LinkedList<>();
		List<Percept> enemypercepts = new LinkedList<>();
		List<Percept> attackingpercepts = new LinkedList<>();

		// perceive friendly units
		setUnitPercepts(this.api.getMyUnits(), newunitpercepts, friendlypercepts, attackingpercepts);
		// perceive enemy units
		setUnitPercepts(this.api.getEnemyUnits(), null, enemypercepts, attackingpercepts);

		if (!friendlypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		}
		if (!enemypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		}
		if (!attackingpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		}
		if (!newunitpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNDERCONSTRUCTION, Filter.Type.ALWAYS), newunitpercepts);
		}
	}

	/**
	 * @param u
	 * @return The region for the given unit (from a cache if it was seen before).
	 */
	private int getRegion(Unit u) {
		return BwapiUtility.getRegion(u.getPosition(), this.api.getMap());
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
	private void setUnitPercepts(List<Unit> units, List<Percept> newunitpercepts, List<Percept> unitpercepts,
			List<Percept> attackingpercepts) {
		for (Unit u : units) {
			if (!BwapiUtility.isValid(u)) {
				continue;
			}
			if (newunitpercepts != null) {
				String unittype = (u.getType().getID() == UnitTypes.Zerg_Egg.getID()) ? u.getBuildType().getName()
						: BwapiUtility.getName(u.getType());
				unitpercepts.add(new FriendlyPercept(u.getID(), unittype));
				if (!u.isCompleted()) {
					newunitpercepts.add(new UnderConstructionPercept(u.getID(), u.getHitPoints() + u.getShields(),
							u.getPosition().getBX(), u.getPosition().getBY(), getRegion(u)));
				}
			} else {
				unitpercepts.add(new EnemyPercept(u.getID(), BwapiUtility.getName(u.getType()), u.getHitPoints(),
						u.getShields(), u.getEnergy(), new ConditionHandler(this.api, u).getConditions(),
						u.getPosition().getBX(), u.getPosition().getBY(), getRegion(u)));
				if (u.getType().isAttackCapable()) {
					Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new AttackingPercept(u.getID(), target.getID()));
					}
				}
			}
		}
	}
}
