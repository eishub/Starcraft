package eisbw.percepts.perceivers;

import eis.iilang.Percept;
import eisbw.percepts.WorkerActivityPercept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.ArrayList;
import java.util.List;

public class WorkerActivityPerceiver extends Perceiver {
  
  public WorkerActivityPerceiver(JNIBWAPI api) {
    super(api);
  }

  @Override
  public List<Percept> perceive() {
    List<Percept> percepts = new ArrayList<>();

    for (Unit unit : this.api.getMyUnits()) {
      if (unit.getType().isWorker()) {
        if (unit.isGatheringGas()) {
          percepts.add(new WorkerActivityPercept(unit.getID(), "gatheringGas"));
        } else if (unit.isGatheringMinerals()) {
          percepts.add(new WorkerActivityPercept(unit.getID(), "gatheringMinerals"));
        } else if (unit.isConstructing()) {
          percepts.add(new WorkerActivityPercept(unit.getID(), "constructing"));
        } else {
          percepts.add(new WorkerActivityPercept(unit.getID(), "idling"));
        }
      }
    }

    return percepts;
  }
}
