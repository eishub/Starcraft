package eisbw.debugger.draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.MapDrawer;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.type.Color;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.Building;
import org.openbw.bwapi4j.unit.GasMiningFacility;
import org.openbw.bwapi4j.unit.MineralPatch;
import org.openbw.bwapi4j.unit.MobileUnit;
import org.openbw.bwapi4j.unit.PlayerUnit;
import org.openbw.bwapi4j.unit.Unit;
import org.openbw.bwapi4j.unit.VespeneGeyser;

import eisbw.BwapiUtility;
import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class DrawUnitInfo extends IDraw {
	private final static int barHeight = 18;
	private final static Color barColor = Color.BLUE;
	private final BW bwapi;
	private final List<PlayerUnit> alive = new LinkedList<>();
	private final Map<Integer, Integer> dead = new HashMap<>();

	/**
	 * Draw unit information (health, movement, counts).
	 *
	 * @param game
	 *            The current game.
	 */
	public DrawUnitInfo(Game game, BW bwapi) {
		super(game);
		this.bwapi = bwapi;
	}

	@Override
	protected void doDraw(MapDrawer api) {
		drawTimerInfo(api);
		drawHealth(api);
		drawTargets(api);
		drawIDs(api);
		drawUnitInformation(api, 440, 5);
	}

	/**
	 * Draws remaining research/upgrade times; unit building/training is already
	 * covered by the health drawing
	 */
	private void drawTimerInfo(MapDrawer api) {
		int y = 45;
		for (final PlayerUnit punit : this.bwapi.getUnits(this.bwapi.getInteractionHandler().self())) {
			Building unit = (punit instanceof Building) ? (Building) punit : null;
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int total = 0;
			int done = 0;
			String txt = "";
			boolean bar = false;
			// if (unit.getRemainingResearchTime() > 0) { FIXME cannot access Researcher
			// TechType ttype = unit.getTech();
			// total = ttype.researchTime();
			// done = total - unit.getRemainingResearchTime();
			// txt = BwapiUtility.getName(ttype);
			// bar = true;
			// }
			// if (unit.getRemainingUpgradeTime() > 0) { FIXME cannot access Researcher
			// UpgradeType utype = unit.getUpgrade();
			// total = utype.upgradeTime(0); // ???
			// done = total - unit.getRemainingUpgradeTime();
			// txt = BwapiUtility.getName(utype);
			// bar = true;
			// }
			if (unit.getRemainingBuildTime() > 0) {
				total = type.buildTime();
				done = total - unit.getRemainingBuildTime();
				txt = BwapiUtility.getName(type); // TODO: zerg eggs not supported now
			}
			if (total > 0) {
				if (bar) {
					int width = type.tileWidth() * 32;
					Position start = new Position(unit.getX() - width / 2, unit.getY() - 20);
					api.drawBoxMap(start.getX(), start.getY(), start.getX() + width, start.getY() + barHeight, barColor,
							false);
					int progress = (int) ((double) done / (double) total * width);
					api.drawBoxMap(start.getX(), start.getY(), start.getX() + progress, start.getY() + barHeight,
							barColor, true);
					api.drawTextMap(new Position(start.getX() + 5, start.getY() + 2), txt);
				}
				api.drawTextScreen(10, y, (total - done) + " " + txt);
				y += 10;
			}
		}
	}

	/**
	 * Draws health boxes for units (ported from JNIBWAPI native code); added a
	 * max>0 check to prevent crashes on spell units (with health 255)
	 */
	private void drawHealth(MapDrawer api) {
		for (final Unit unit : this.bwapi.getAllUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int health = 0;
			int max = type.maxHitPoints();
			if (unit instanceof PlayerUnit) {
				health = ((PlayerUnit) unit).getHitPoints();
			} else if (unit instanceof MineralPatch) {
				health = ((MineralPatch) unit).getResources();
				max = 1500;
			} else if (unit instanceof VespeneGeyser) {
				health = ((VespeneGeyser) unit).getResources();
				max = 5000;
			} else if (unit instanceof GasMiningFacility) {
				health = ((GasMiningFacility) unit).getResources();
				max = 5000;
			}

			if (health > 0 && max > 0) {
				int x = unit.getX();
				int y = unit.getY();
				int l = type.dimensionLeft();
				int t = type.dimensionUp();
				int r = type.dimensionRight();
				int b = type.dimensionDown();
				int width = ((r + l) * health) / max;
				if (health * 3 < max) {
					api.drawBoxMap(x - l, y - t - 5, x - l + width, y - t, Color.RED, true);
				} else if (health * 3 < 2 * max) {
					api.drawBoxMap(x - l, y - t - 5, x - l + width, y - t, Color.YELLOW, true);
				} else {
					api.drawBoxMap(x - l, y - t - 5, x - l + width, y - t, Color.GREEN, true);
				}
				boolean self = (unit instanceof PlayerUnit
						&& BwapiUtility.getPlayer((PlayerUnit) unit) == this.bwapi.getInteractionHandler().self());
				api.drawBoxMap(x - l, y - t - 5, x + r, y - t, self ? Color.WHITE : Color.RED, false);
				api.drawBoxMap(x - l, y - t, x + r, y + b, self ? Color.WHITE : Color.RED, false);
				api.drawTextMap(x - l, y - t, BwapiUtility.getName(type));
			}
		}
	}

	/**
	 * Draws the targets of each unit. (ported from JNIBWAPI native code)
	 */
	private void drawTargets(MapDrawer api) {
		for (final Unit u : this.bwapi.getAllUnits()) {
			MobileUnit unit = (u instanceof MobileUnit) ? (MobileUnit) u : null;
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			boolean self = (BwapiUtility.getPlayer(unit) == this.bwapi.getInteractionHandler().self());
			Unit target = unit.getTargetUnit(); // FIXME: orderTarget not supported in lib atm.
			if (target != null) {
				api.drawLineMap(unit.getPosition(), target.getPosition(), self ? Color.YELLOW : Color.PURPLE);
			}
			Position position = unit.getTargetPosition();
			if (position != null) {
				api.drawLineMap(unit.getPosition(), position, self ? Color.YELLOW : Color.PURPLE);
			}
		}
	}

	/**
	 * Draws the IDs of each unit. (ported from JNIBWAPI native code)
	 */
	private void drawIDs(MapDrawer api) {
		for (final Unit u : this.bwapi.getAllUnits()) {
			PlayerUnit unit = (u instanceof PlayerUnit) ? (PlayerUnit) u : null;
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			api.drawTextMap(unit.getPosition(), Integer.toString(unit.getId()));
		}
	}

	/**
	 * Draws a list of all unit types, counting how many are still alive and how
	 * many have died (ported from native code of the tournament manager)
	 */
	private void drawUnitInformation(MapDrawer api, int x, int y) {
		api.drawTextScreen(x, y + 20, this.bwapi.getInteractionHandler().self().getName() + "'s Units");
		api.drawTextScreen(x + 160, y + 20, "#");
		api.drawTextScreen(x + 180, y + 20, "X");

		Map<Integer, Integer> count = new HashMap<>();
		List<PlayerUnit> previous = new ArrayList<>(this.alive);
		this.alive.clear();
		for (final PlayerUnit unit : this.bwapi.getUnits(this.bwapi.getInteractionHandler().self())) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null || !unit.isCompleted()) {
				continue;
			}
			this.alive.add(unit);
			if (type == UnitType.Terran_Siege_Tank_Siege_Mode) {
				type = UnitType.Terran_Siege_Tank_Tank_Mode;
			}
			int t = type.getId();
			if (count.containsKey(t)) {
				count.put(t, count.get(t).intValue() + 1);
			} else {
				count.put(t, 1);
			}
		}
		previous.removeAll(this.alive);
		for (final PlayerUnit unit : previous) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null /* || unit.isMorphing() FIXME: not supported in lib atm. */) {
				continue;
			}
			if (type == UnitType.Terran_Siege_Tank_Siege_Mode) {
				type = UnitType.Terran_Siege_Tank_Tank_Mode;
			}
			int t = type.getId();
			if (this.dead.containsKey(t)) {
				this.dead.put(t, this.dead.get(t).intValue() + 1);
			} else {
				this.dead.put(t, 1);
			}
		}

		int yspace = 0;
		for (final UnitType type : UnitType.values()) {
			int t = type.getId();
			int livecount = count.containsKey(t) ? count.get(t).intValue() : 0;
			int deadcount = this.dead.containsKey(t) ? this.dead.get(t).intValue() : 0;
			if (livecount > 0 || deadcount > 0) {
				api.drawTextScreen(x, y + 40 + ((yspace) * 10), BwapiUtility.getName(type));
				api.drawTextScreen(x + 160, y + 40 + ((yspace) * 10), Integer.toString(livecount));
				api.drawTextScreen(x + 180, y + 40 + ((yspace++) * 10), Integer.toString(deadcount));
			}
		}
	}
}
