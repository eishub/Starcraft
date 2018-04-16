package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.CommandCenter;
import org.openbw.bwapi4j.unit.Factory;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.ResearchingFacility;
import org.openbw.bwapi4j.unit.ScienceFacility;
import org.openbw.bwapi4j.unit.Starport;
import org.openbw.bwapi4j.unit.TrainingFacility;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Cancels the action of the current unit.
 *
 */
public class Cancel extends StarcraftAction {
	/**
	 * The Cancel constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Cancel(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return type.isBuilding() || type.canProduce() || type == UnitType.Terran_Nuclear_Silo;
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		if (unit instanceof TrainingFacility && ((TrainingFacility) unit).isTraining()) {
			((TrainingFacility) unit).cancelTrain();
		}
		if (unit instanceof ResearchingFacility && ((ResearchingFacility) unit).isResearching()) {
			((ResearchingFacility) unit).cancelResearch();
		} else if (unit instanceof ResearchingFacility && ((ResearchingFacility) unit).isUpgrading()) {
			((ResearchingFacility) unit).cancelUpgrade();
		} else if (unit instanceof CommandCenter) {
			((CommandCenter) unit).cancelAddon();
		} else if (unit instanceof Factory) {
			((Factory) unit).cancelAddon();
		} else if (unit instanceof ScienceFacility) {
			((ScienceFacility) unit).cancelAddon();
		} else if (unit instanceof Starport) {
			((Starport) unit).cancelAddon();
		}
	}

	@Override
	public String toString() {
		return "cancel";
	}
}
