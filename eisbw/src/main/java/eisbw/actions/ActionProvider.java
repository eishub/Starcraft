package eisbw.actions;

import java.util.HashMap;
import java.util.Map;
import jnibwapi.JNIBWAPI;

public class ActionProvider {

    private final Map<String, StarcraftAction> actions;

    public ActionProvider() {
        this.actions = new HashMap<>();
    }

    public StarcraftAction getAction(String actionName) {
        return actions.get(actionName);
    }

    public void loadActions(JNIBWAPI api) {
        actions.put("attack/1", new Attack(api));
        actions.put("attack/2", new AttackMove(api));
        actions.put("upgrade/1", new Upgrade(api));
        actions.put("build/3", new Build(api));
        actions.put("gather/1", new Gather(api));
        actions.put("move/2", new Move(api));
        actions.put("train/1", new Train(api));
        actions.put("stop/0", new Stop(api));
        actions.put("use/1", new Use(api));
        actions.put("use/2", new UseOnTarget(api));
        actions.put("use/3", new UseOnPosition(api));
        actions.put("research/1", new Research(api));
        actions.put("setRallyPoint/2", new SetRallyPoint(api));
        actions.put("setRallyPoint/1", new SetRallyUnit(api));
        actions.put("lift/0", new Lift(api));
        actions.put("land/2", new Land(api));        
        actions.put("siege/0", new Siege(api));
        actions.put("unsiege/0", new UnSiege(api));
        actions.put("buildAddon/1", new BuildAddon(api));
    }
}
