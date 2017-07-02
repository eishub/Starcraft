package eisbw.units;

import java.util.LinkedList;
import java.util.List;

import bwapi.Unit;
import eisbw.percepts.perceivers.BuildingPerceiver;
import eisbw.percepts.perceivers.GenericUnitPerceiver;
import eisbw.percepts.perceivers.IPerceiver;
import eisbw.percepts.perceivers.WorkerPerceiver;

/**
 * @author Danny & Harm - The Starcraft Unit Factory which creates the units.
 *
 */
public class StarcraftUnitFactory {
	private final bwapi.Game api;

	/**
	 * The StarcraftUnitFactory constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StarcraftUnitFactory(bwapi.Game api) {
		this.api = api;
	}

	// These perceptgenerators are only added on init, so a building that can
	// fly can not move when built, so please take caution and think when adding
	// percepts
	/**
	 * Creates a unit.
	 *
	 * @param unit
	 *            - the unit in the game.
	 * @return - a StarCraft unit with perceivers.
	 */
	public StarcraftUnit create(Unit unit) {
		List<IPerceiver> perceptGenerators = new LinkedList<>();
		perceptGenerators.add(new GenericUnitPerceiver(this.api, unit));

		if (unit.getType().isBuilding()) {
			perceptGenerators.add(new BuildingPerceiver(this.api, unit));
		}
		if (unit.getType().isWorker()) {
			perceptGenerators.add(new WorkerPerceiver(this.api, unit));
		}

		return new StarcraftUnit(perceptGenerators, unit.getType().isWorker());
	}
}
