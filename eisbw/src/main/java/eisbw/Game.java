package eisbw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import eis.PerceptUpdate;
import eis.eis2java.translation.Filter;
import eis.exceptions.ManagementException;
import eis.iilang.Percept;
import eisbw.debugger.draw.IDraw;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.NukePercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.WinnerPercept;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;
import eisbw.percepts.perceivers.MapPerceiver;
import eisbw.percepts.perceivers.PerceptFilter;
import eisbw.percepts.perceivers.UnitsPerceiver;
import eisbw.units.StarcraftUnit;
import eisbw.units.StarcraftUnitFactory;
import eisbw.units.Units;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;

/**
 * @author Danny & Harm - The game class where the percepts are updated.
 */
public class Game {
	protected final StarcraftEnvironmentImpl env;
	protected Units units; // overriden in test
	protected int managers; // can increase with action
	protected final Map<String, Set<String>> subscriptions;
	protected final Map<String, IDraw> draws;
	protected volatile Map<String, Map<PerceptFilter, List<Percept>>> percepts;
	protected volatile Map<PerceptFilter, List<Percept>> mapPercepts;
	protected volatile Map<PerceptFilter, List<Percept>> constructionPercepts;
	protected volatile Map<PerceptFilter, List<Percept>> nukePercepts;
	protected volatile Map<PerceptFilter, List<Percept>> endGamePercepts;
	private volatile boolean startedManagers = false;
	private final Map<String, Map<String, List<Percept>>> previous;
	private final Map<Integer, EnemyPercept> enemies;

	public Game(final StarcraftEnvironmentImpl environment, final int managers,
			final Map<String, Set<String>> subscriptions) {
		this.env = environment;
		this.managers = managers;
		this.subscriptions = subscriptions;
		this.units = new Units(environment);
		this.draws = new ConcurrentHashMap<>();
		this.percepts = new ConcurrentHashMap<>();
		this.previous = new ConcurrentHashMap<>();
		this.enemies = new ConcurrentHashMap<>();
	}

	public void startManagers() {
		for (int i = 1; i <= this.managers; ++i) {
			this.env.addToEnvironment("manager" + i, "manager");
		}
		try { // give the managers a second to start-up
			Thread.sleep(1000);
		} catch (final InterruptedException ignore) {
		} finally {
			this.startedManagers = true;
		}
	}

	public void startNewManager() {
		this.env.addToEnvironment("manager" + (++this.managers), "manager");
	}

	public StarcraftEnvironmentImpl getEnvironment() {
		return this.env;
	}

	public int getAgentCount() {
		return this.env.getAgents().size();
	}

	public void addUnit(final Unit unit, final StarcraftUnitFactory factory) {
		this.units.addUnit(unit, factory);
	}

	public void deleteUnit(final int id) {
		this.units.deleteUnit(id);
		this.enemies.remove(id);
	}

	public Unit getUnit(final String name) {
		return this.units.getUnit(name);
	}

	public String getUnitName(final int id) {
		return this.units.getUnitName(id);
	}

	public List<IDraw> getDraws() {
		return new ArrayList<>(this.draws.values());
	}

	/**
	 * Updates the percepts.
	 *
	 * @param bwapi the game bridge
	 */
	public void update(final JNIBWAPI bwapi) {
		processUninitializedUnits();
		final Map<String, Map<PerceptFilter, List<Percept>>> unitPerceptHolder = new HashMap<>();
		final Map<PerceptFilter, List<Percept>> globalPercepts = getGlobalPercepts(bwapi);
		for (final Unit unit : bwapi.getMyUnits()) {
			final StarcraftUnit scUnit = this.units.getStarcraftUnit(unit);
			if (scUnit == null) {
				continue;
			}
			final String unitname = this.units.getUnitName(unit.getID());
			final String unittype = BwapiUtility.getEisUnitType(unit);
			final Map<PerceptFilter, List<Percept>> percepts = getUnitPercepts(unitname, unittype, globalPercepts);
			percepts.putAll(scUnit.perceive());
			unitPerceptHolder.put(unitname, percepts);
		}
		for (int i = 1; i <= this.managers; ++i) {
			final String managername = "manager" + i;
			final Map<PerceptFilter, List<Percept>> thisUnitPercepts = getUnitPercepts(managername, "manager",
					globalPercepts);
			unitPerceptHolder.put(managername, thisUnitPercepts);
		}
		this.percepts = unitPerceptHolder;
	}

	private Map<PerceptFilter, List<Percept>> getUnitPercepts(final String unitName, final String unitType,
			final Map<PerceptFilter, List<Percept>> globalPercepts) {
		Set<String> subscriptions = this.subscriptions.get(unitName);
		if (subscriptions == null || subscriptions.isEmpty()) {
			subscriptions = this.subscriptions.get(unitType); // fallback
		}
		if (subscriptions == null || subscriptions.isEmpty()) {
			return new HashMap<>(0);
		} else {
			final Map<PerceptFilter, List<Percept>> thisUnitPercepts = new HashMap<>();
			filterPercepts(thisUnitPercepts, subscriptions, globalPercepts);
			filterPercepts(thisUnitPercepts, subscriptions, this.constructionPercepts);
			filterPercepts(thisUnitPercepts, subscriptions, this.mapPercepts);
			filterPercepts(thisUnitPercepts, subscriptions, this.nukePercepts);
			filterPercepts(thisUnitPercepts, subscriptions, this.endGamePercepts);
			return thisUnitPercepts;
		}
	}

	private static void filterPercepts(final Map<PerceptFilter, List<Percept>> thisUnitPercepts,
			final Set<String> subscriptions, final Map<PerceptFilter, List<Percept>> perceptsToAdd) {
		if (perceptsToAdd != null) {
			for (final PerceptFilter toAdd : perceptsToAdd.keySet()) {
				if (subscriptions.contains(toAdd.getName())) {
					thisUnitPercepts.put(toAdd, perceptsToAdd.get(toAdd));
				}
			}
		}
	}

	private void processUninitializedUnits() {
		if (this.startedManagers && this.units.getUninitializedUnits() != null) {
			final List<Unit> toAdd = new LinkedList<>();
			Unit unit;
			while ((unit = this.units.getUninitializedUnits().poll()) != null) {
				final String unitName = BwapiUtility.getName(unit);
				if (unit.isCompleted() && isInitialized(unitName)) {
					this.env.addToEnvironment(unitName, BwapiUtility.getEisUnitType(unit));
				} else {
					toAdd.add(unit);
				}
			}
			this.units.getUninitializedUnits().addAll(toAdd);
		}
	}

	private PerceptUpdate translatePercepts(final String unitName, final Map<PerceptFilter, List<Percept>> map) {
		final List<Percept> addList = new LinkedList<>();
		final List<Percept> delList = new LinkedList<>();
		Map<String, List<Percept>> previousPercepts = this.previous.get(unitName);
		if (previousPercepts == null) {
			previousPercepts = new HashMap<>();
			this.previous.put(unitName, previousPercepts);
		}
		final Iterator<Entry<PerceptFilter, List<Percept>>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			final Entry<PerceptFilter, List<Percept>> entry = entries.next();
			final PerceptFilter key = entry.getKey();
			final List<Percept> values = entry.getValue();
			final List<Percept> previous = previousPercepts.get(key.getName());
			switch (key.getType()) {
			case ALWAYS:
				addList.addAll(values);
				if (previous != null) {
					addList.removeAll(previous);
					delList.addAll(previous);
					delList.removeAll(values);
				}
				break;
			case ONCE:
				if (previous == null) {
					addList.addAll(values);
				} else {
					delList.addAll(values);
					values.clear();
				}
				break;
			case ON_CHANGE:
				addList.addAll(values);
				if (previous != null) {
					addList.removeAll(previous);
					// this is not strictly the most efficient, but it works
					// without requiring advanced bookkeeping here:
					delList.addAll(previous);
				}
				break;
			default:
				Logger.getLogger("StarCraft Logger").warning("Unknown percept type " + key);
				break;
			}
			previousPercepts.put(key.getName(), values);
		}
		return new PerceptUpdate(addList, delList);
	}

	/**
	 * Update the global percepts (gameframe, resources, mineralfield,
	 * vespenegeyser, friendly, enemy, attacking, underConstruction)
	 *
	 * @param bwapi the JNIBWAPI
	 */
	private Map<PerceptFilter, List<Percept>> getGlobalPercepts(final JNIBWAPI bwapi) {
		final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		new UnitsPerceiver(bwapi, this.enemies).perceive(toReturn);
		return toReturn;
	}

	/**
	 * Update the map percepts (map, enemyPlayer, ownRace, base, chokepoint, region)
	 *
	 * @param bwapi the JNIBWAPI
	 */
	public void updateMap(final JNIBWAPI bwapi) {
		final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>();
		new MapPerceiver(bwapi).perceive(toReturn);
		this.mapPercepts = toReturn;
	}

	/**
	 * updates the constructionsites in the game.
	 *
	 * @param bwapi the JNIBWAPI
	 */
	public void updateConstructionSites(final JNIBWAPI bwapi) {
		final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>(1);
		new ConstructionSitePerceiver(bwapi).perceive(toReturn);
		this.constructionPercepts = toReturn;
	}

	/**
	 * Updates the nuke percept(s)
	 *
	 * @param bwapi the JNIBWAPI
	 * @param the   position of a nuke
	 */
	public void updateNukePerceiver(final JNIBWAPI bwapi, final Position pos) {
		if (pos == null) {
			this.nukePercepts = null;
		} else {
			final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>(1);
			final List<Percept> nukepercept = new ArrayList<>(1);
			final int region = BwapiUtility.getRegion(pos, bwapi.getMap());
			nukepercept.add(new NukePercept(pos.getBX(), pos.getBY(), region));
			toReturn.put(new PerceptFilter(Percepts.NUKE, Filter.Type.ON_CHANGE), nukepercept);
			if (this.nukePercepts == null) {
				this.nukePercepts = toReturn;
			} else {
				this.nukePercepts.values().iterator().next().addAll(toReturn.values().iterator().next());
			}
		}
	}

	/**
	 * Updates the winner percept.
	 *
	 * @param winner True iff we won
	 */
	public void updateEndGamePerceiver(final boolean winner) {
		final Map<PerceptFilter, List<Percept>> toReturn = new HashMap<>(1);
		final List<Percept> endgamepercept = new ArrayList<>(1);
		endgamepercept.add(new WinnerPercept(winner));
		toReturn.put(new PerceptFilter(Percepts.WINNER, Filter.Type.ALWAYS), endgamepercept);
		this.endGamePercepts = toReturn;
	}

	public void addDraw(final String draw, final IDraw idraw) {
		this.draws.put(draw, idraw);
	}

	public void removeDraw(final String draw) {
		this.draws.remove(draw);
	}

	public void toggleDraw(final String draw) {
		this.draws.get(draw).toggle();
	}

	/**
	 * Get the percepts of this unit.
	 *
	 * @param entity - the name of the unit
	 * @return the percepts
	 */
	public PerceptUpdate getPercepts(final String entity) {
		final Map<PerceptFilter, List<Percept>> percepts = this.percepts.get(entity);
		if (percepts == null) {
			return new PerceptUpdate();
		} else {
			return translatePercepts(entity, this.percepts.get(entity));
		}
	}

	/**
	 * get the constructionSites.
	 *
	 * @return the constructionsites.
	 */
	public List<Percept> getConstructionSites() {
		final List<Percept> perceptHolder = new LinkedList<>();
		for (final List<Percept> percept : this.constructionPercepts.values()) {
			perceptHolder.addAll(percept);
		}
		return perceptHolder;
	}

	/**
	 * Clean the game data.
	 */
	public void clean() {
		this.units.clean();
		for (int i = 1; i <= this.managers; ++i) {
			this.env.deleteFromEnvironment("manager" + i);
		}
		this.percepts = null;
		this.constructionPercepts = null;
		this.endGamePercepts = null;
		this.nukePercepts = null;
		this.mapPercepts = null;
		this.previous.clear();
		try {
			this.env.kill();
		} catch (final ManagementException ignore) {
		}
	}

	/**
	 * @param entity The evaluated entity
	 * @return boolean indicating whether the unit is initialized or not.
	 */
	public boolean isInitialized(final String entity) {
		return this.percepts != null && this.percepts.containsKey(entity);
	}
}
