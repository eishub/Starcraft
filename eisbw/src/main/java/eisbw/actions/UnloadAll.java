package eisbw.actions;

import java.util.List;

import eis.iilang.Action;
import eis.iilang.Parameter;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - Unloads all units.
 */
public class UnloadAll extends StarcraftLoadingAction {
	/**
	 * The UnloadAll constructor.
	 *
	 * @param api The BWAPI
	 */
	public UnloadAll(final JNIBWAPI api) {
		super(api);
	}

	@Override
	public boolean isValid(final Action action) {
		final List<Parameter> parameters = action.getParameters();
		return parameters.isEmpty();
	}

	@Override
	public void execute(final Unit unit, final Action action) {
		unit.unloadAll(false);
	}

	@Override
	public String toString() {
		return "unloadAll";
	}
}
