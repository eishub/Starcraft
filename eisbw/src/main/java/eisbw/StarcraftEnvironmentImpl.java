package eisbw;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import eis.EIDefaultImpl;
import eis.PerceptUpdate;
import eis.eis2java.translation.Translator;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;
import eisbw.configuration.Configuration;
import eisbw.translators.BooleanStringTranslator;
import eisbw.translators.ParamEnumTranslator;
import eisbw.translators.RaceStringTranslator;

/**
 * @author Danny & Harm - The environment class which handles most environment
 *         logics.
 */
public class StarcraftEnvironmentImpl extends EIDefaultImpl {
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger("StarCraft Logger");

	protected BwapiListener listener;
	private Configuration config;
	private Game game;
	private final Set<String> entities = new HashSet<>();

	/**
	 * Constructor of the environment.
	 */
	public StarcraftEnvironmentImpl() {
		super();
		final Translator translatorfactory = Translator.getInstance();
		translatorfactory.registerParameter2JavaTranslator(new ParamEnumTranslator());
		translatorfactory.registerParameter2JavaTranslator(new BooleanStringTranslator());
		translatorfactory.registerParameter2JavaTranslator(new RaceStringTranslator());
	}

	@Override
	public void init(final Map<String, Parameter> parameters) throws ManagementException {
		super.init(parameters);
		setState(EnvironmentState.PAUSED);
		try {
			this.config = new Configuration(parameters);
			this.game = new Game(this, this.config.getManagers(), this.config.getPercepts());
			if (!"test".equals(this.config.getOwnRace())) {
				WindowsTools.loadRequirements(this.config.getScDir());
				this.listener = new BwapiListener(this.game, this.config.getScDir(), this.config.getDebug(),
						this.config.getDrawMapInfo(), this.config.getDrawUnitInfo(), this.config.getInvulnerable(),
						this.config.getSpeed());
				if (!"OFF".equals(this.config.getAutoMenu())) {
					WindowsTools.startChaoslauncher(this.config.getOwnRace(), this.config.getMap(),
							this.config.getScDir(), this.config.getAutoMenu(), this.config.getGameType(),
							this.config.getEnemyRace(), this.config.getSeed());
				}
			}
			setState(EnvironmentState.RUNNING);
		} catch (final Exception ex) {
			this.logger.log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void kill() throws ManagementException {
		if ("SINGLE_PLAYER".equals(this.config.getAutoMenu())) {
			WindowsTools.Client_KillStarcraft();
		}
		super.kill();
	}

	@Override
	protected PerceptUpdate getPerceptsForEntity(final String entity) throws NoEnvironmentException {
		return this.game.getPercepts(entity);
	}

	@Override
	protected boolean isSupportedByEnvironment(final Action action) {
		return this.listener.isSupportedByEnvironment(action);
	}

	@Override
	protected boolean isSupportedByType(final Action action, final String type) {
		return isSupportedByEnvironment(action);
	}

	@Override
	protected boolean isSupportedByEntity(final Action action, final String entity) {
		return this.listener.isSupportedByEntity(action, entity);
	}

	@Override
	protected void performEntityAction(final Action action, final String entity) throws ActException {
		this.listener.performEntityAction(action, entity);
	}

	/**
	 * Adds a unit to the environment.
	 *
	 * @param unitName    - the name of the unit
	 * @param eisUnitType - the type of the unit
	 */
	public void addToEnvironment(final String unitName, final String eisUnitType) {
		try {
			if (this.entities.add(unitName)) {
				addEntity(unitName, eisUnitType);
			}
		} catch (final EntityException exception) {
			this.logger.log(Level.WARNING, "Could not add " + unitName + " to the environment", exception);
		}
	}

	/**
	 * Deletes a unit from the environment.
	 *
	 * @param unitName - the name of the unit
	 */
	public void deleteFromEnvironment(final String unitName) {
		try {
			if (this.entities.remove(unitName)) {
				final Set<String> agents = getAssociatedAgents(unitName);
				deleteEntity(unitName);
				for (final String agent : agents) {
					unregisterAgent(agent);
				}
			}
		} catch (final EntityException exception) {
			this.logger.log(Level.WARNING, "Could not delete " + unitName + " from the environment", exception);
		} catch (final RelationException exception) {
			this.logger.log(Level.WARNING, "Exception when deleting entity from the environment", exception);
		} catch (final AgentException exception) {
			this.logger.log(Level.WARNING, "Exception when deleting agent from the environment", exception);
		}
	}

	public int getFPS() {
		return this.listener.getFPS();
	}
}
