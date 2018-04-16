package eisbw.actions;

import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.unit.Bunker;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Transporter;

import eis.iilang.Action;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Unloads all units.
 *
 */
public class UnloadAll extends StarcraftLoadingAction {
	/**
	 * The UnloadAll constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public UnloadAll(BW api) {
		super(api);
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public void execute(PlayerUnit unit, Action action) {
		if (unit instanceof Transporter) {
			((Transporter) unit).unloadAll();
		} else if (unit instanceof Bunker) {
			((Bunker) unit).unloadAll();
		}
	}

	@Override
	public String toString() {
		return "unloadAll";
	}
}
