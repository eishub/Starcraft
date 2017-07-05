package eisbw.debugger.draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import eisbw.BwapiUtility;
import eisbw.Game;

/**
 * @author Harm & Danny.
 *
 */
public class DrawUnitInfo extends IDraw {
	private final static int barHeight = 18;
	private final static Color barColor = Color.Blue;
	private final List<Unit> alive = new LinkedList<>();
	private final Map<UnitType, Integer> dead = new HashMap<>();

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
		drawUnitInformation(api, 440, 6);
		drawAgentCount(api);
	}

	/**
	 * Draws remaining research/upgrade times; unit building/training is already
	 * covered by the health drawing
	 */
	private void drawTimerInfo(bwapi.Game api) {
		for (final Unit unit : api.self().getUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			int total = 0;
			int done = 0;
			String txt = "";
			if (unit.getRemainingResearchTime() > 0) {
				total = unit.getTech().researchTime();
				done = total - unit.getRemainingResearchTime();
				txt = unit.getTech().toString();
			}
			if (unit.getRemainingUpgradeTime() > 0) {
				total = unit.getUpgrade().upgradeTime();
				done = total - unit.getRemainingUpgradeTime();
				txt = unit.getUpgrade().toString();
			}
			if (total > 0) {
				int width = unit.getType().tileWidth() * 32;
				Position start = new Position(unit.getPosition().getX() - width / 2, unit.getPosition().getY() - 30);
				api.drawBoxMap(start, new Position(start.getX() + width, start.getY() + barHeight), barColor, false);
				int progress = (int) ((double) done / (double) total * width);
				api.drawBoxMap(start, new Position(start.getX() + progress, start.getY() + barHeight), barColor, true);
				api.drawTextMap(new Position(start.getX() + 5, start.getY() + 2), txt);
			}
		}
	}

	/**
	 * Draws health boxes for units (ported from bwapi.Game native code); added a
	 * max>0 check to prevent crashes on spell units (with health 255)
	 */
	private void drawHealth(bwapi.Game api) {
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			int health = unit.getHitPoints();
			int max = unit.getType().maxHitPoints();
			if (health > 0 && max > 0) {
				int x = unit.getPosition().getX();
				int y = unit.getPosition().getY();
				int l = unit.getType().dimensionLeft();
				int t = unit.getType().dimensionUp();
				int r = unit.getType().dimensionRight();
				int b = unit.getType().dimensionDown();
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
				boolean self = unit.getPlayer() != null && unit.getPlayer().equals(api.self());
				api.drawBoxMap(new Position(x - l, y - t - 5), new Position(x + r, y - t),
						self ? Color.White : Color.Red, false);
				api.drawBoxMap(new Position(x - l, y - t), new Position(x + r, y + b), self ? Color.White : Color.Red,
						false);
				api.drawTextMap(new Position(x - l, y - t), unit.getType().toString());
			}
		}
	}

	/**
	 * Draws the targets of each unit. (ported from bwapi.Game native code)
	 */
	private void drawTargets(bwapi.Game api) {
		for (final Unit unit : api.getAllUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			boolean self = unit.getPlayer() != null && unit.getPlayer().equals(api.self());
			Unit target = (unit.getTarget() == null) ? unit.getOrderTarget() : unit.getTarget();
			if (target != null) {
				api.drawLineMap(unit.getPosition(), target.getPosition(), self ? Color.Yellow : Color.Purple);
			}
			Position position = unit.getTargetPosition();
			if (position != null) {
				api.drawLineMap(unit.getPosition(), position, self ? Color.Yellow : Color.Purple);
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
		api.drawTextScreen(new Position(x, y + 20), api.self().getName() + "'s Units");
		api.drawTextScreen(new Position(x + 160, y + 20), "#");
		api.drawTextScreen(new Position(x + 180, y + 20), "X");

		Map<UnitType, Integer> count = new HashMap<>();
		List<Unit> previous = new ArrayList<>(this.alive);
		this.alive.clear();
		for (final Unit unit : api.self().getUnits()) {
			if (!BwapiUtility.isValid(unit)) {
				continue;
			}
			this.alive.add(unit);
			UnitType type = unit.getType();
			if (type == UnitType.Terran_Siege_Tank_Siege_Mode) {
				type = UnitType.Terran_Siege_Tank_Tank_Mode;
			}
			if (count.containsKey(type)) {
				count.put(type, count.get(type).intValue() + 1);
			} else {
				count.put(type, 1);
			}
		}
		previous.removeAll(this.alive);
		for (final Unit unit : previous) {
			UnitType type = unit.getType();
			if (type == UnitType.Terran_Siege_Tank_Siege_Mode) {
				type = UnitType.Terran_Siege_Tank_Tank_Mode;
			}
			if (this.dead.containsKey(type)) {
				this.dead.put(type, this.dead.get(type).intValue() + 1);
			} else {
				this.dead.put(type, 1);
			}
		}

		int yspace = 0;
		for (final UnitType t : count.keySet()) {
			int livecount = count.get(t).intValue();
			int deadcount = this.dead.containsKey(t) ? this.dead.get(t).intValue() : 0;
			api.drawTextScreen(new Position(x, y + 40 + ((yspace) * 10)), BwapiUtility.getName(t));
			api.drawTextScreen(new Position(x + 160, y + 40 + ((yspace) * 10)), Integer.toString(livecount));
			api.drawTextScreen(new Position(x + 180, y + 40 + ((yspace++) * 10)), Integer.toString(deadcount));
		}
	}

	private void drawAgentCount(bwapi.Game api) {
		api.drawTextScreen(new Position(10, 10), "Agentcount: " + this.game.getAgentCount());
	}
}
