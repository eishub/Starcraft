package eisbw.debugger.draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eisbw.BwapiUtility;
import eisbw.Game;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.types.UpgradeType;
import jnibwapi.util.BWColor;

/**
 * @author Harm & Danny.
 *
 */
public class DrawUnitInfo extends IDraw {
	private final static int barHeight = 18;
	private final static BWColor barColor = BWColor.Blue;
	private final List<Unit> alive = new LinkedList<>();
	private final Map<Integer, Integer> dead = new HashMap<>();

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
	protected void doDraw(JNIBWAPI api) {
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
	private void drawTimerInfo(JNIBWAPI api) {
		int y = 45;
		for (final Unit unit : api.getMyUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int total = 0;
			int done = 0;
			String txt = "";
			boolean bar = false;
			if (unit.getRemainingResearchTime() > 0) {
				TechType ttype = unit.getTech();
				total = ttype.getResearchTime();
				done = total - unit.getRemainingResearchTime();
				txt = ttype.getName();
				bar = true;
			}
			if (unit.getRemainingUpgradeTime() > 0) {
				UpgradeType utype = unit.getUpgrade();
				total = utype.getUpgradeTimeBase();
				done = total - unit.getRemainingUpgradeTime();
				txt = utype.getName();
				bar = true;
			}
			if (unit.getRemainingBuildTimer() > 0) {
				total = type.getBuildTime();
				done = total - unit.getRemainingBuildTimer();
				txt = (type == UnitTypes.Zerg_Egg) ? unit.getBuildType().getName() : BwapiUtility.getName(type);
			}
			if (total > 0) {
				if (bar) {
					int width = type.getTileWidth() * 32;
					Position start = new Position(unit.getX() - width / 2, unit.getY() - 20);
					api.drawBox(start, new Position(start.getPX() + width, start.getPY() + barHeight), barColor, false,
							false);
					int progress = (int) ((double) done / (double) total * width);
					api.drawBox(start, new Position(start.getPX() + progress, start.getPY() + barHeight), barColor,
							true, false);
					api.drawText(new Position(start.getPX() + 5, start.getPY() + 2), txt, false);
				}
				api.drawText(new Position(10, y, PosType.PIXEL), (total - done) + " " + txt, true);
				y += 10;
			}
		}
	}

	/**
	 * Draws health boxes for units (ported from JNIBWAPI native code); added a
	 * max>0 check to prevent crashes on spell units (with health 255)
	 */
	private void drawHealth(JNIBWAPI api) {
		for (final Unit unit : api.getAllUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			int health = unit.getHitPoints();
			int max = type.getMaxHitPoints();
			if (type.isMineralField()) {
				health = unit.getResources();
				max = 1500;
			}
			if (type.isRefinery() || type == UnitTypes.Resource_Vespene_Geyser) {
				health = unit.getResources();
				max = 5000;
			}
			if (health > 0 && max > 0) {
				int x = unit.getX();
				int y = unit.getY();
				int l = type.getDimensionLeft();
				int t = type.getDimensionUp();
				int r = type.getDimensionRight();
				int b = type.getDimensionDown();
				int width = ((r + l) * health) / max;
				if (health * 3 < max) {
					api.drawBox(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), BWColor.Red, true,
							false);
				} else if (health * 3 < 2 * max) {
					api.drawBox(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), BWColor.Yellow,
							true, false);
				} else {
					api.drawBox(new Position(x - l, y - t - 5), new Position(x - l + width, y - t), BWColor.Green, true,
							false);
				}
				boolean self = (BwapiUtility.getPlayer(unit) == api.getSelf());
				api.drawBox(new Position(x - l, y - t - 5), new Position(x + r, y - t),
						self ? BWColor.White : BWColor.Red, false, false);
				api.drawBox(new Position(x - l, y - t), new Position(x + r, y + b), self ? BWColor.White : BWColor.Red,
						false, false);
				api.drawText(new Position(x - l, y - t), type.getName(), false);
			}
		}
	}

	/**
	 * Draws the targets of each unit. (ported from JNIBWAPI native code)
	 */
	private void drawTargets(JNIBWAPI api) {
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			boolean self = (BwapiUtility.getPlayer(unit) == api.getSelf());
			Unit target = (unit.getTarget() == null) ? unit.getOrderTarget() : unit.getTarget();
			if (target != null) {
				api.drawLine(unit.getPosition(), target.getPosition(), self ? BWColor.Yellow : BWColor.Purple, false);
			}
			Position position = unit.getTargetPosition();
			if (position != null) {
				api.drawLine(unit.getPosition(), position, self ? BWColor.Yellow : BWColor.Purple, false);
			}
		}
	}

	/**
	 * Draws the IDs of each unit. (ported from JNIBWAPI native code)
	 */
	private void drawIDs(JNIBWAPI api) {
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			api.drawText(unit.getPosition(), Integer.toString(unit.getID()), false);
		}
	}

	/**
	 * Draws a list of all unit types, counting how many are still alive and how
	 * many have died (ported from native code of the tournament manager)
	 */
	private void drawUnitInformation(JNIBWAPI api, int x, int y) {
		api.drawText(new Position(x, y + 20), api.getSelf().getName() + "'s Units", true);
		api.drawText(new Position(x + 160, y + 20), "#", true);
		api.drawText(new Position(x + 180, y + 20), "X", true);

		Map<Integer, Integer> count = new HashMap<>();
		List<Unit> previous = new ArrayList<>(this.alive);
		this.alive.clear();
		for (final Unit unit : api.getMyUnits()) {
			UnitType type = BwapiUtility.getType(unit);
			if (type == null) {
				continue;
			}
			this.alive.add(unit);
			if (type == UnitTypes.Terran_Siege_Tank_Siege_Mode) {
				type = UnitTypes.Terran_Siege_Tank_Tank_Mode;
			}
			int t = type.getID();
			if (count.containsKey(t)) {
				count.put(t, count.get(t).intValue() + 1);
			} else {
				count.put(t, 1);
			}
		}
		previous.removeAll(this.alive);
		for (final Unit unit : previous) {
			UnitType type = unit.getType(); // manual because unit is dead
			if (type == UnitTypes.Terran_Siege_Tank_Siege_Mode) {
				type = UnitTypes.Terran_Siege_Tank_Tank_Mode;
			}
			int t = type.getID();
			if (this.dead.containsKey(t)) {
				this.dead.put(t, this.dead.get(t).intValue() + 1);
			} else {
				this.dead.put(t, 1);
			}
		}

		int yspace = 0;
		for (final UnitType type : UnitTypes.getAllUnitTypes()) {
			int t = type.getID();
			int livecount = count.containsKey(t) ? count.get(t).intValue() : 0;
			int deadcount = this.dead.containsKey(t) ? this.dead.get(t).intValue() : 0;
			if (livecount > 0 || deadcount > 0) {
				api.drawText(new Position(x, y + 40 + ((yspace) * 10)), BwapiUtility.getName(type), true);
				api.drawText(new Position(x + 160, y + 40 + ((yspace) * 10)), Integer.toString(livecount), true);
				api.drawText(new Position(x + 180, y + 40 + ((yspace++) * 10)), Integer.toString(deadcount), true);
			}
		}
	}
}
