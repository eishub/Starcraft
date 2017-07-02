package eisbw.percepts.perceivers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.DefensiveMatrixPercept;
import eisbw.percepts.OrderPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.SelfPercept;
import eisbw.percepts.StatusPercept;
import eisbw.percepts.UnitLoadedPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.OrderType;
import jnibwapi.types.OrderType.OrderTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the generic percepts.
 *
 */
public class GenericUnitPerceiver extends UnitPerceiver {
	/**
	 * @param api
	 *            The BWAPI.
	 * @param unit
	 *            The perceiving unit.
	 */
	public GenericUnitPerceiver(JNIBWAPI api, Unit unit) {
		super(api, unit);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		defensiveMatrixPercept(toReturn);
		selfPercept(toReturn);
		statusPercept(toReturn);
		orderPercept(toReturn);

		if (this.unit.getType().getSpaceProvided() > 0) {
			List<Unit> loadedUnits = this.unit.getLoadedUnits();
			unitLoadedPercept(toReturn, loadedUnits);
		}

		return toReturn;
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void statusPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> statusPercept = new HashSet<>(1);
		Position pos = this.unit.getPosition();
		int region = BwapiUtility.getRegion(this.unit, this.api.getMap());
		statusPercept.add(new StatusPercept(this.unit.getHitPoints(), this.unit.getShields(), this.unit.getEnergy(),
				new ConditionHandler(this.api, this.unit).getConditions(), pos.getBX(), pos.getBY(), region));
		toReturn.put(new PerceptFilter(Percepts.STATUS, Filter.Type.ON_CHANGE), statusPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void selfPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> selfPercept = new HashSet<>(1);
		selfPercept.add(new SelfPercept(this.unit.getID(), BwapiUtility.getName(this.unit.getType())));
		toReturn.put(new PerceptFilter(Percepts.SELF, Filter.Type.ONCE), selfPercept);
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void defensiveMatrixPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> defensiveMatrixPercept = new HashSet<>(1);
		if (this.unit.isDefenseMatrixed()) {
			defensiveMatrixPercept.add(new DefensiveMatrixPercept(this.unit.getDefenseMatrixPoints()));
			toReturn.put(new PerceptFilter(Percepts.DEFENSIVEMATRIX, Filter.Type.ALWAYS), defensiveMatrixPercept);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 * @param loadedUnits
	 *            The loaded units
	 */
	private void unitLoadedPercept(Map<PerceptFilter, Set<Percept>> toReturn, List<Unit> loadedUnits) {
		Set<Percept> unitLoadedPercept = new HashSet<>(loadedUnits.size());
		for (Unit u : loadedUnits) {
			if (u != null) {
				unitLoadedPercept.add(new UnitLoadedPercept(u.getID()));
			}
		}
		if (!unitLoadedPercept.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNITLOADED, Filter.Type.ALWAYS), unitLoadedPercept);
		}
	}

	/**
	 * @param toReturn
	 *            The percept and reference of which kind of percept it is.
	 */
	private void orderPercept(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> orderPercept = new HashSet<>(1);
		OrderType primary = (this.unit.getOrder() == null) ? OrderTypes.None : this.unit.getOrder();
		Unit target = (this.unit.getTarget() == null) ? this.unit.getOrderTarget() : this.unit.getTarget();
		OrderType secondary = (this.unit.getSecondaryOrder() == null) ? OrderTypes.None : this.unit.getSecondaryOrder();
		orderPercept
				.add(new OrderPercept(primary.getName(), (target == null) ? -1 : target.getID(), secondary.getName()));
		toReturn.put(new PerceptFilter(Percepts.ORDER, Filter.Type.ON_CHANGE), orderPercept);
	}
}
