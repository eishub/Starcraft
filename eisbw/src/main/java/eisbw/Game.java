package eisbw;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import eis.eis2java.translation.Filter;
import eis.exceptions.ManagementException;
import eis.iilang.Percept;
import eisbw.percepts.FramePercept;
import eisbw.percepts.NukePercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.WinnerPercept;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;
import eisbw.percepts.perceivers.MapPerceiver;
import eisbw.percepts.perceivers.PerceptFilter;
import eisbw.percepts.perceivers.UnitsPerceiver;
import eisbw.units.StarcraftUnit;
import eisbw.units.Units;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - The game class where the percepts are updated.
 *
 */
public class Game {
	protected volatile Map<String, Map<PerceptFilter, Set<Percept>>> percepts;
	protected Units units; // overriden in test
	protected volatile Map<PerceptFilter, Set<Percept>> constructionPercepts;
	protected volatile Map<PerceptFilter, Set<Percept>> framePercepts;
	protected volatile Map<PerceptFilter, Set<Percept>> endGamePercepts;
	protected volatile Map<PerceptFilter, Set<Percept>> nukePercepts;
	protected final StarcraftEnvironmentImpl env;
	private volatile Map<PerceptFilter, Set<Percept>> mapPercepts;
	private final Map<String, Map<String, Set<Percept>>> previous;

	/**
	 * Constructor.
	 *
	 * @param environment
	 *            - the environment
	 */
	public Game(StarcraftEnvironmentImpl environment) {
		this.units = new Units(environment);
		this.percepts = new HashMap<>();
		this.constructionPercepts = new HashMap<>();
		this.mapPercepts = new HashMap<>();
		this.previous = new HashMap<>();
		this.env = environment;
	}

	public void mapAgent() {
		if (this.env.mapAgent()) {
			this.env.addToEnvironment("mapAgent", "mapAgent");
		}
	}

	/**
	 * Update the map.
	 *
	 * @param api
	 *            - the API.
	 */
	public void updateMap(JNIBWAPI api) {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		new MapPerceiver(api).perceive(toReturn);
		this.mapPercepts = toReturn;
	}

	/**
	 * Updates the percepts.
	 *
	 * @param bwapi
	 *            - the game bridge
	 */
	public void update(JNIBWAPI bwapi) {
		processUninitializedUnits();
		Map<String, Map<PerceptFilter, Set<Percept>>> unitPerceptHolder = new HashMap<>();
		Map<PerceptFilter, Set<Percept>> globalPercepts = getGlobalPercepts(bwapi);
		Map<String, StarcraftUnit> unitList = this.units.getStarcraftUnits();
		for (Entry<String, StarcraftUnit> unit : unitList.entrySet()) {
			Map<PerceptFilter, Set<Percept>> thisUnitPercepts = new HashMap<>(unit.getValue().perceive());
			if (!this.env.mapAgent()) {
				thisUnitPercepts.putAll(globalPercepts); // UnitsPerceiver
				if (unit.getValue().isWorker() && this.constructionPercepts != null) {
					thisUnitPercepts.putAll(this.constructionPercepts);
				}
				if (this.nukePercepts != null) {
					thisUnitPercepts.putAll(this.nukePercepts);
				}
				if (this.endGamePercepts != null) {
					thisUnitPercepts.putAll(this.endGamePercepts);
				}
				if (this.mapPercepts != null) {
					thisUnitPercepts.putAll(this.mapPercepts);
				}
				if (this.framePercepts != null) {
					thisUnitPercepts.putAll(this.framePercepts);
				}
			}
			unitPerceptHolder.put(unit.getKey(), thisUnitPercepts);
		}
		if (this.env.mapAgent()) {
			Map<PerceptFilter, Set<Percept>> thisUnitPercepts = new HashMap<>(globalPercepts);
			if (this.constructionPercepts != null) {
				thisUnitPercepts.putAll(this.constructionPercepts);
			}
			if (this.nukePercepts != null) {
				thisUnitPercepts.putAll(this.nukePercepts);
			}
			if (this.endGamePercepts != null) {
				thisUnitPercepts.putAll(this.endGamePercepts);
			}
			if (this.mapPercepts != null) {
				thisUnitPercepts.putAll(this.mapPercepts);
			}
			if (this.framePercepts != null) {
				thisUnitPercepts.putAll(this.framePercepts);
			}
			unitPerceptHolder.put("mapAgent", thisUnitPercepts);
		}
		this.percepts = unitPerceptHolder;
	}

	private void processUninitializedUnits() {
		if (this.units.getUninitializedUnits() != null) {
			List<Unit> toAdd = new LinkedList<>();
			Unit unit;
			while ((unit = this.units.getUninitializedUnits().poll()) != null) {
				String unitName = BwapiUtility.getUnitName(unit);
				if (unit.isCompleted() && this.percepts.containsKey(unitName)) {
					this.env.addToEnvironment(unitName, BwapiUtility.getEisUnitType(unit));
				} else {
					toAdd.add(unit);
				}
			}
			this.units.getUninitializedUnits().addAll(toAdd);
		}
	}

	private LinkedList<Percept> translatePercepts(String unitName, Map<PerceptFilter, Set<Percept>> map) {
		LinkedList<Percept> percept = new LinkedList<>();
		if (!this.previous.containsKey(unitName)) {
			this.previous.put(unitName, new HashMap<String, Set<Percept>>());
		}
		for (Entry<PerceptFilter, Set<Percept>> entry : map.entrySet()) {
			switch (entry.getKey().getType()) {
			case ALWAYS:
				percept.addAll(entry.getValue());
				break;
			case ONCE:
				if (!this.previous.get(unitName).containsKey(entry.getKey().getName())) {
					percept.addAll(entry.getValue());
					this.previous.get(unitName).put(entry.getKey().getName(), null);
				}
				break;
			case ON_CHANGE:
				handleOnChangePercept(entry, unitName, percept);
				break;
			case ON_CHANGE_NEG:
				Logger.getLogger("StarCraft logger").warning("Change with negation is not allowed.");
				break;
			default:
				break;
			}
		}
		return percept;
	}

	private void handleOnChangePercept(Entry<PerceptFilter, Set<Percept>> entry, String unitName,
			List<Percept> percept) {
		if (this.previous.get(unitName).containsKey(entry.getKey().getName())) {
			Set<Percept> checkList = new HashSet<>(entry.getValue());
			checkList.removeAll(this.previous.get(unitName).get(entry.getKey().getName()));
			if (!checkList.isEmpty()) {
				this.previous.get(unitName).put(entry.getKey().getName(), entry.getValue());
			}
			percept.addAll(checkList);
		} else {
			this.previous.get(unitName).put(entry.getKey().getName(), entry.getValue());
			percept.addAll(entry.getValue());
		}
	}

	/**
	 * updates the constructionsites in the game.
	 *
	 * @param bwapi
	 *            - the JNIBWAPI
	 */
	public void updateConstructionSites(JNIBWAPI bwapi) {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		new ConstructionSitePerceiver(bwapi).perceive(toReturn);
		this.constructionPercepts = toReturn;
	}

	/**
	 * updates the frame count.
	 *
	 * @param count
	 *            The current frame count (per 50, matching c.site updates)
	 */
	public void updateFrameCount(int count) {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>(1);
		Set<Percept> framepercept = new HashSet<>(1);
		framepercept.add(new FramePercept(count));
		toReturn.put(new PerceptFilter(Percepts.FRAME, Filter.Type.ON_CHANGE), framepercept);
		this.framePercepts = toReturn;
	}

	/**
	 * Updates the endGame percept.
	 *
	 * @param bwapi
	 *            - the JNIBWAPI
	 */
	public void updateEndGamePerceiver(JNIBWAPI bwapi, boolean winner) {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>(1);
		Set<Percept> endgamepercept = new HashSet<>(1);
		endgamepercept.add(new WinnerPercept(winner));
		toReturn.put(new PerceptFilter(Percepts.WINNER, Filter.Type.ALWAYS), endgamepercept);
		this.endGamePercepts = toReturn;
	}

	/**
	 * Updates the endGame percept.
	 *
	 * @param bwapi
	 *            - the JNIBWAPI
	 */
	public void updateNukePerceiver(JNIBWAPI bwapi, Position pos) {
		if (pos == null) {
			this.nukePercepts = null;
		} else {
			Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
			Set<Percept> nukepercept = new HashSet<>(1);
			nukepercept.add(new NukePercept(pos.getBX(), pos.getBY()));
			toReturn.put(new PerceptFilter(Percepts.NUKE, Filter.Type.ON_CHANGE), nukepercept);
			if (this.nukePercepts == null) {
				this.nukePercepts = toReturn;
			} else {
				this.nukePercepts.values().iterator().next().addAll(toReturn.values().iterator().next());
			}
		}
	}

	private Map<PerceptFilter, Set<Percept>> getGlobalPercepts(JNIBWAPI bwapi) {
		Map<PerceptFilter, Set<Percept>> toReturn = new HashMap<>();
		new UnitsPerceiver(bwapi).perceive(toReturn);
		return toReturn;
	}

	/**
	 * Get the percepts of this unit.
	 *
	 * @param entity
	 *            - the name of the unit
	 * @return the percepts
	 */
	public LinkedList<Percept> getPercepts(String entity) {
		if (this.percepts.containsKey(entity)) {
			return translatePercepts(entity, this.percepts.get(entity));
		} else {
			return new LinkedList<>();
		}
	}

	public Units getUnits() {
		return this.units;
	}

	/**
	 * get the constructionSites.
	 *
	 * @return the constructionsites.
	 */
	public List<Percept> getConstructionSites() {
		List<Percept> perceptHolder = new LinkedList<>();
		for (Set<Percept> percept : this.constructionPercepts.values()) {
			perceptHolder.addAll(percept);
		}
		return perceptHolder;
	}

	/**
	 * Clean the game data.
	 */
	public void clean() {
		this.units.clean();
		this.percepts = null;
		this.constructionPercepts = null;
		this.endGamePercepts = null;
		this.nukePercepts = null;
		this.framePercepts = null;
		this.mapPercepts = null;
		this.previous.clear();
		try {
			this.env.kill();
		} catch (ManagementException ignore) {
		}
	}

	public int getAgentCount() {
		return this.env.getAgents().size();
	}

	public StarcraftEnvironmentImpl getEnvironment() {
		return this.env;
	}

	/**
	 * @param entity
	 *            The evaluated entity
	 * @return boolean indicating whether the unit is initialized or not.
	 */
	public boolean isInitialized(String entity) {
		return this.percepts.containsKey(entity);
	}
}
