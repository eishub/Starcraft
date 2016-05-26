package eisbw;

import eisbw.units.Units;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

public class UpdateThread extends Thread {

  private Game game;
  private JNIBWAPI bwapi;
  private boolean running;
  private Units units;

  /**
   * Constructs a thread.
   * 
   * @param game
   *          - the game data class.
   * @param bwapi
   *          - the API.
   */
  public UpdateThread(Game game, JNIBWAPI bwapi) {
    this.bwapi = bwapi;
    this.game = game;
    this.units = game.getUnits();
    running = true;
  }

  /**
   * Terminate the thread.
   */
  public void terminate() {
    running = false;
  }

  @Override
  public void run() {
    Thread.currentThread().setName("Update thread");
    while (running) {
      game.update(bwapi);
      while (!units.getUninitializedUnits().isEmpty()) {
        Unit unit = units.getUninitializedUnits().poll();
        String unitName = BwapiUtility.getUnitName(unit);
        game.getEnvironment().addToEnvironment(unitName, BwapiUtility.getEisUnitType(unit));
      }
      try {
        Thread.sleep(5);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
