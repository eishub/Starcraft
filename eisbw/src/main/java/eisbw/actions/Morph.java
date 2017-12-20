package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.CreepColony;
import org.openbw.bwapi4j.unit.Drone;
import org.openbw.bwapi4j.unit.Hatchery;
import org.openbw.bwapi4j.unit.Hydralisk;
import org.openbw.bwapi4j.unit.Lair;
import org.openbw.bwapi4j.unit.Larva;
import org.openbw.bwapi4j.unit.Mutalisk;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Spire;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Makes the unit morph into a specified unit.
 *
 */
public class Morph extends StarcraftAction {
	/**
	 * The Morph constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Morph(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 1 && parameters.get(0) instanceof Identifier) {
			UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null;
		}
		return false;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.getRace() == Race.Zerg;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String typestring = ((Identifier) parameters.get(0)).getValue();
		UnitType type = getUnitType(typestring);

		if (unit instanceof Larva) {
			((Larva) unit).morph(type);
		} else if (unit instanceof Drone) {
			((Drone) unit).morph(type);
		} else if (unit instanceof CreepColony) {
			((CreepColony) unit).morph(type);
		} else if (unit instanceof Mutalisk) {
			((Mutalisk) unit).morph();
		} else if (unit instanceof Hydralisk) {
			((Hydralisk) unit).morph();
		} else if (unit instanceof Hatchery) {
			((Hatchery) unit).morph();
		} else if (unit instanceof Lair) {
			((Lair) unit).morph();
		} else if (unit instanceof Spire) {
			((Spire) unit).morph();
		}
	}

	@Override
	public String toString() {
		return "morph(Type)";
	}
}
