package eisbw.units;

import java.util.ArrayList;
import java.util.List;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.unit.PlayerUnit;

import bwta.BWTA;
import eisbw.BwapiUtility;
import eisbw.percepts.perceivers.GenericUnitPerceiver;
import eisbw.percepts.perceivers.IPerceiver;

/**
 * @author Danny & Harm - The Starcraft Unit Factory which creates the units.
 *
 */
public class StarcraftUnitFactory {
	private final BW bwapi;
	private final BWTA bwta;

	/**
	 * The StarcraftUnitFactory constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public StarcraftUnitFactory(BW bwapi, BWTA bwta) {
		this.bwapi = bwapi;
		this.bwta = bwta;
	}

	/**
	 * Creates a unit.
	 *
	 * @param unit
	 *            - the unit in the game.
	 * @return - a StarCraft unit with perceivers.
	 */
	public StarcraftUnit create(PlayerUnit unit) {
		List<IPerceiver> perceptGenerators = new ArrayList<>(1);
		perceptGenerators.add(new GenericUnitPerceiver(this.bwapi, this.bwta, unit));
		return new StarcraftUnit(perceptGenerators, BwapiUtility.getType(unit));
	}
}
