package eisbw.units;

import java.util.ArrayList;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.types.UnitType.UnitTypes;
import eisbw.BWApiUtility;
import eisbw.UnitTypesEx;
import eisbw.percepts.perceivers.AddonPerceiver;
import eisbw.percepts.perceivers.AttackingUnitsPerceiver;
import eisbw.percepts.perceivers.AvailableResourcesPerceiver;
import eisbw.percepts.perceivers.BuildUnitPerceiver;
import eisbw.percepts.perceivers.BuilderUnitPerceiver;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;
import eisbw.percepts.perceivers.EnemyPerceiver;
import eisbw.percepts.perceivers.GathererUnitPerceiver;
import eisbw.percepts.perceivers.GenericUnitPerceiver;
import eisbw.percepts.perceivers.IPerceiver;
import eisbw.percepts.perceivers.IdleWorkersPerceiver;
import eisbw.percepts.perceivers.IsLoadedUnitPerceiver;
import eisbw.percepts.perceivers.IsMovingPerceiver;
import eisbw.percepts.perceivers.LiftUnitPerceiver;
import eisbw.percepts.perceivers.MapPerceiver;
import eisbw.percepts.perceivers.PlayerUnitsPerceiver;
import eisbw.percepts.perceivers.QueueSizePerceiver;
import eisbw.percepts.perceivers.RallyPerceiver;
import eisbw.percepts.perceivers.RepairPerceiver;
import eisbw.percepts.perceivers.ResearchPerceiver;
import eisbw.percepts.perceivers.SiegeUnitPerceiver;
import eisbw.percepts.perceivers.StimUnitPerceiver;
import eisbw.percepts.perceivers.TransporterPerceiver;
import eisbw.percepts.perceivers.UpgradePerceiver;
import eisbw.percepts.perceivers.WorkerActivityPerceiver;

public class StarcraftUnitFactory {

    private final JNIBWAPI api;
    private final BWApiUtility util;

    public StarcraftUnitFactory(JNIBWAPI api, BWApiUtility util) {
        this.api = api;
        this.util = util;
    }

    
    // These perceptgenerators are only added on init, so a building that can fly 
    // can not move when built, so please take caution and think when adding percepts
    public StarcraftUnit Create(Unit unit) {
        ArrayList<IPerceiver> perceptGenerators = new ArrayList<>();
        perceptGenerators.add(new GenericUnitPerceiver(api, unit));
        perceptGenerators.add(new MapPerceiver(api));
        perceptGenerators.add(new EnemyPerceiver(api));
        perceptGenerators.add(new PlayerUnitsPerceiver(api, util));
        perceptGenerators.add(new ResearchPerceiver(this.api, unit));
        
        if (unit.getType().isCanMove()) {
        	perceptGenerators.add(new IsLoadedUnitPerceiver(api,unit));
        	perceptGenerators.add(new IsMovingPerceiver(api, unit));
        }
        
        if (unit.getType().isBuilding()) {
            perceptGenerators.add(new AvailableResourcesPerceiver(api));
            perceptGenerators.add(new QueueSizePerceiver(this.api, unit));
            perceptGenerators.add(new BuildUnitPerceiver(this.api, unit));	
            perceptGenerators.add(new UpgradePerceiver(this.api, unit));
            perceptGenerators.add(new RallyPerceiver(api, unit));
            if(unit.getType().getRaceID() == 1){
            	perceptGenerators.add(new LiftUnitPerceiver(api, unit));
                perceptGenerators.add(new AddonPerceiver(api, unit));
            }
        }
        if (UnitTypesEx.isRefinery(unit.getType())) {
            perceptGenerators.add(new WorkerActivityPerceiver(api,util));
        }
        if (unit.getType().isAttackCapable()) {
            perceptGenerators.add(new AttackingUnitsPerceiver(api));			
        }
        if (unit.getType().isWorker()) {
            perceptGenerators.add(new AvailableResourcesPerceiver(api));
            perceptGenerators.add(new BuilderUnitPerceiver(api, unit));
            perceptGenerators.add(new GathererUnitPerceiver(api, unit));
            perceptGenerators.add(new ConstructionSitePerceiver(api, unit));	
            perceptGenerators.add(new WorkerActivityPerceiver(api,util));
			
			if (unit.getType().getRaceID() == RaceTypes.Terran.getID()) {
				perceptGenerators.add(new RepairPerceiver(api));
			}
        }
		if (unit.getType().getSpaceProvided() > 0) {
			perceptGenerators.add(new TransporterPerceiver(api, util, unit));
		}
		
        String un = unit.getType().getName();
        if (UnitTypesEx.isCommandCenter(unit.getType())) {
            perceptGenerators.add(new IdleWorkersPerceiver(api, util));
             //For some reason the refinerys can't be matched in GOAL right now. Just using command center for now
            perceptGenerators.add(new WorkerActivityPerceiver(api,util));
        } else if (un.equals(UnitTypes.Terran_Marine.getName()) || un.equals(UnitTypes.Terran_Firebat.getName())) {
            perceptGenerators.add(new StimUnitPerceiver(api, unit));
        } else if (un.equals(UnitTypes.Terran_Siege_Tank_Tank_Mode.getName())) {
            perceptGenerators.add(new SiegeUnitPerceiver(api, unit));
        }
		
		return new StarcraftUnit(api, unit, perceptGenerators);
    }
}
