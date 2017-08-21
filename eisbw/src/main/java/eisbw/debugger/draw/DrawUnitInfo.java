package eisbw.debugger.draw;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import bwapi.Color;
import bwapi.Player;
import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import eisbw.BwapiUtility;
import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class DrawUnitInfo extends IDraw {
	private final static int barHeight = 18;
	private final static Color barColor = Color.Blue;

	/**
	 * Draw unit information (health, movement, counts).
	 *
	 * @param game
	 *            The current game.
	 */
	public DrawUnitInfo(Game game) {
		super(game);
	}

	@Override
	protected void doDraw(bwapi.Game api) {
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
	private void drawTimerInfo(bwapi.Game api) {
		int y = 45;
		for (final Unit unit : BwapiUtility.getSelf(api).getUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int total = 0;
			int done = 0;
			String txt = "";
			boolean bar = false;
			int remaining = unit.getRemainingResearchTime();
			if (remaining > 0) {
				TechType tech = unit.getTech();
				total = tech.researchTime();
				done = total - remaining;
				txt = BwapiUtility.getName(tech);
				bar = true;
			}
			remaining = unit.getRemainingUpgradeTime();
			if (unit.getRemainingUpgradeTime() > 0) {
				UpgradeType upgrade = unit.getUpgrade();
				total = upgrade.upgradeTime();
				done = total - remaining;
				txt = BwapiUtility.getName(upgrade);
				bar = true;
			}
			remaining = unit.getRemainingBuildTime();
			if (remaining > 0) {
				total = type.buildTime();
				done = total - remaining;
				txt = (type == UnitType.Zerg_Egg) ? BwapiUtility.getName(unit.getBuildType())
						: BwapiUtility.getName(type);
			}
			if (total > 0) {
				if (bar) {
					Position pos = unit.getPosition();
					int width = type.tileWidth() * 32;
					Position start = new Position(pos.getX() - width / 2, pos.getY() - 30);
					api.drawBoxMap(start, new Position(start.getX() + width, start.getY() + barHeight), barColor,
							false);
					int progress = (int) ((double) done / (double) total * width);
					api.drawBoxMap(start, new Position(start.getX() + progress, start.getY() + barHeight), barColor,
							true);
					api.drawTextMap(new Position(start.getX() + 5, start.getY() + 2), txt);
				}
				api.drawTextScreen(new Position(10, y), (total - done) + " " + txt);
				y += 10;
			}
		}
	}

	/**
	 * Draws health boxes for units (ported from bwapi.Game native code); added a
	 * max>0 check to prevent crashes on spell units (with health 255)
	 */
	private void drawHealth(bwapi.Game api) {
		Player self = BwapiUtility.getSelf(api);
		for (final Unit unit : api.getAllUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int health = unit.getHitPoints();
			int max = type.maxHitPoints();
			if (type.isMineralField()) {
				health = unit.getResources();
				max = 1500;
			}
			if (type.isRefinery() || type == UnitType.Resource_Vespene_Geyser) {
				health = unit.getResources();
				max = 5000;
			}
			if (health > 0 && max > 0) {
				Position pos = unit.getPosition();
				int x = pos.getX();
				int y = pos.getY();
				int l = type.dimensionLeft();
				int t = type.dimensionUp();
				int r = type.dimensionRight();
				int b = type.dimensionDown();
				int width = ((r + l) * health) / max;
				if (health * 3 < max) {
					api.drawBoxMap(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), Color.Red, true);
				} else if (health * 3 < 2 * max) {
					api.drawBoxMap(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), Color.Yellow,
							true);
				} else {
					api.drawBoxMap(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), Color.Green,
							true);
				}
				boolean isSelf = (BwapiUtility.getPlayer(unit) == self);
				api.drawBoxMap(new Position(x - l, y - t - 5), new Position(x + r, y - t),
						isSelf ? Color.White : Color.Red, false);
				api.drawBoxMap(new Position(x - l, y - t), new Position(x + r, y + b), isSelf ? Color.White : Color.Red,
						false);
				api.drawTextMap(new Position(x - l, y - t), BwapiUtility.getName(type));
			}
		}
	}

	/**
	 * Draws the targets of each unit. (ported from bwapi.Game native code)
	 */
	private void drawTargets(bwapi.Game api) {
		Player self = BwapiUtility.getSelf(api);
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			boolean isSelf = (BwapiUtility.getPlayer(unit) == self);
			Position pos = unit.getPosition();
			Unit target = (unit.getTarget() == null) ? unit.getOrderTarget() : unit.getTarget();
			if (target != null) {
				api.drawLineMap(pos, target.getPosition(), isSelf ? Color.Yellow : Color.Purple);
			}
			Position targetpos = (unit.getTargetPosition() == null) ? unit.getOrderTargetPosition()
					: unit.getTargetPosition();
			if (targetpos != null) {
				api.drawLineMap(pos, targetpos, isSelf ? Color.Yellow : Color.Purple);
			}
		}
	}

	/**
	 * Draws the IDs of each unit. (ported from bwapi.Game native code)
	 */
	private void drawIDs(bwapi.Game api) {
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			api.drawTextMap(unit.getPosition(), Integer.toString(unit.getID()));
		}
	}

	/**
	 * Draws a list of all unit types, counting how many are still alive and how
	 * many have died (ported from native code of the tournament manager)
	 */

	private void drawUnitInformation(bwapi.Game api, int x, int y) {
		Player self = BwapiUtility.getSelf(api);
		api.drawTextScreen(new Position(x, y + 20), self.getName() + "'s Units");
		api.drawTextScreen(new Position(x + 160, y + 20), "#");
		api.drawTextScreen(new Position(x + 180, y + 20), "X");

		int yspace = 0;
		for (Field field : UnitType.class.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
				try {
					UnitType type = (UnitType) field.get(null);
					int livecount = self.allUnitCount(type);
					int deadcount = self.deadUnitCount(type);
					if (livecount > 0 || deadcount > 0) {
						api.drawTextScreen(new Position(x, y + 40 + ((yspace) * 10)), BwapiUtility.getName(type));
						api.drawTextScreen(new Position(x + 160, y + 40 + ((yspace) * 10)),
								Integer.toString(livecount));
						api.drawTextScreen(new Position(x + 180, y + 40 + ((yspace++) * 10)),
								Integer.toString(deadcount));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
