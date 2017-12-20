package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.CommandCenter;
import org.openbw.bwapi4j.unit.Factory;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.ScienceFacility;
import org.openbw.bwapi4j.unit.Starport;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Builds an addon for the (terran) building.
 *
 */
public class BuildAddon extends StarcraftAction {
	/**
	 * The BuildAddon constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public BuildAddon(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		if (parameters.size() == 1 && parameters.get(0) instanceof Identifier) {
			UnitType ut = getUnitType(((Identifier) parameters.get(0)).getValue());
			return ut != null && ut.isAddon();
		}
		return false;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isFlyingBuilding();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		String type = ((Identifier) parameters.get(0)).getValue();
		switch (getUnitType(type)) {
		case Terran_Comsat_Station:
			((CommandCenter) unit).buildComsatStation();
			break;
		case Terran_Nuclear_Silo:
			((CommandCenter) unit).buildNuclearSilo();
			break;
		case Terran_Machine_Shop:
			((Factory) unit).buildMachineShop();
			break;
		case Terran_Control_Tower:
			((Starport) unit).buildControlTower();
			break;
		case Terran_Covert_Ops:
			((ScienceFacility) unit).buildCovertOps();
			break;
		case Terran_Physics_Lab:
			((ScienceFacility) unit).buildPhysicslab();
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "buildAddon(Type)";
	}
}
