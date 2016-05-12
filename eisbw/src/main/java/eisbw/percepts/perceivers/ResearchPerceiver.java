package eisbw.percepts.perceivers;

import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.percepts.HasResearchedPercept;
import eisbw.percepts.ResearchPercept;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.TechType.TechTypes;

import java.util.ArrayList;
import java.util.List;

public class ResearchPerceiver extends UnitPerceiver {
  public ResearchPerceiver(JNIBWAPI api, Unit unit) {
    super(api, unit);
  }

  @Override
  public List<Percept> perceive() {
    ArrayList<Percept> percepts = new ArrayList<>();

    for (TechType tech : TechTypes.getAllTechTypes()) {
      if (api.getSelf().isResearched(tech)) {
        percepts.add(new HasResearchedPercept(tech.getName()));
      }
    }

    return percepts;
  }

  @Override
  public List<Parameter> getConditions() {
    return new ArrayList<>();
  }
}
