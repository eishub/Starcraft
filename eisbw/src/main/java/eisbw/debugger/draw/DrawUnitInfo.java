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
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
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
		drawUnitInformation(api, 440, 6);
		drawAgentCount(api);
	}

	/**
	 * Draws remaining research/upgrade times; unit building/training is already
	 * covered by the health drawing
	 */
	private void drawTimerInfo(JNIBWAPI api) {
		int y = 45;
		for (final Unit unit : api.getMyUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			int total = 0;
			int done = 0;
			String txt = "";
			boolean bar = false;
			if (unit.getRemainingResearchTime() > 0) {
				total = unit.getTech().getResearchTime();
				done = total - unit.getRemainingResearchTime();
				txt = unit.getTech().getName();
				bar = true;
			}
			if (unit.getRemainingUpgradeTime() > 0) {
				total = unit.getUpgrade().getUpgradeTimeBase();
				done = total - unit.getRemainingUpgradeTime();
				txt = unit.getUpgrade().getName();
				bar = true;
			}
			if (unit.getRemainingBuildTimer() > 0) {
				UnitType type = unit.getType();
				total = type.getBuildTime();
				done = total - unit.getRemainingBuildTimer();
				txt = (type.getID() == UnitTypes.Zerg_Egg.getID()) ? unit.getBuildType().getName()
						: BwapiUtility.getName(type);
			}
			if (total > 0) {
				if (bar) {
					int width = unit.getType().getTileWidth() * 32;
					Position start = new Position(unit.getPosition().getPX() - width / 2,
							unit.getPosition().getPY() - 20);
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
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			UnitType type = unit.getType();
			int health = unit.getHitPoints();
			int max = type.getMaxHitPoints();
			if (type.isMineralField()) {
				health = unit.getResources();
				max = 1500;
			}
			if (type.isRefinery() || type.getID() == UnitTypes.Resource_Vespene_Geyser.getID()) {
				health = unit.getResources();
				max = 5000;
			}
			if (health > 0 && max > 0) {
				int x = unit.getPosition().getPX();
				int y = unit.getPosition().getPY();
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
				boolean self = (unit.getPlayer().getID() == api.getSelf().getID());
				api.drawBox(new Position(x - l, y - t - 5), new Position(x + r, y - t),
						self ? BWColor.White : BWColor.Red, false, false);
				api.drawBox(new Position(x - l, y - t), new Position(x + r, y + b), self ? BWColor.White : BWColor.Red,
						false, false);
				api.drawText(new Position(x - l, y - t), unit.getType().getName(), false);
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
			boolean self = (unit.getPlayer().getID() == api.getSelf().getID());
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
			if (!BwapiUtility.isValid(unit) || !unit.isCompleted()) {
				continue;
			}
			this.alive.add(unit);
			int type = unit.getType().getID();
			if (type == UnitTypes.Terran_Siege_Tank_Siege_Mode.getID()) {
				type = UnitTypes.Terran_Siege_Tank_Tank_Mode.getID();
			}
			if (count.containsKey(type)) {
				count.put(type, count.get(type).intValue() + 1);
			} else {
				count.put(type, 1);
			}
		}
		previous.removeAll(this.alive);
		for (final Unit unit : previous) {
			if (unit.isMorphing()) {
				continue;
			}
			int type = unit.getType().getID();
			if (type == UnitTypes.Terran_Siege_Tank_Siege_Mode.getID()) {
				type = UnitTypes.Terran_Siege_Tank_Tank_Mode.getID();
			}
			if (this.dead.containsKey(type)) {
				this.dead.put(type, this.dead.get(type).intValue() + 1);
			} else {
				this.dead.put(type, 1);
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

	private void drawAgentCount(JNIBWAPI api) {
		api.drawText(new Position(10, 15, PosType.PIXEL), "Agentcount: " + this.game.getAgentCount(), true);
	}
}
