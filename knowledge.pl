% type(race,name)
%	> includes all units and buildings of each race
% costs(name,minerals,gas,supply,buildFrames,requiredUnitsOrTechList)
% 	> supply can be negative for e.g. an overlord or a pylon
% stats(name,maxHealth,maxShield,maxEnergy,speed,conditionsList)
%	> conditions can be [building,canMove,canDetect,flies,mechanical,organic,requiresCreep,requiresPsi,robotic]
% metrics(name,width,height,sightRange,spaceRequired)
%	> spaceRequired can be negative for e.g. a bunker or a shuttle
% combat(name,groundDPS,airDPS,cooldownFrames,range,splashRadius)
%	> only there for units that can attack; this does not take upgrades nor abilities into account

type(terran,'Terran Marine').
costs('Terran Marine',50,0,2,360,['Terran Barracks']).
stats('Terran Marine',40,0,0,40,['canMove','organic']).
metrics('Terran Marine',1,1,7,1).
combat('Terran Marine',6,6,15,4,0).

type(terran,'Terran Ghost').
costs('Terran Ghost',25,75,2,750,['Terran Academy','Terran Covert Ops','Terran Barracks']).
stats('Terran Ghost',45,0,200,40,['canMove','organic']).
metrics('Terran Ghost',1,1,9,1).
combat('Terran Ghost',10,10,22,7,0).

type(terran,'Terran Vulture').
costs('Terran Vulture',75,0,4,450,['Terran Factory']).
stats('Terran Vulture',80,0,0,64,['canMove','mechanical']).
metrics('Terran Vulture',1,1,8,2).
combat('Terran Vulture',20,0,30,5,0).

type(terran,'Terran Goliath').
costs('Terran Goliath',100,50,4,600,['Terran Factory','Terran Armory']).
stats('Terran Goliath',125,0,0,45,['canMove','mechanical']).
metrics('Terran Goliath',1,1,8,2).
combat('Terran Goliath',12,20,22,6,0).

type(terran,'Terran SCV').
costs('Terran SCV',50,0,2,300,['Terran Command Center']).
stats('Terran SCV',60,0,0,49,['canMove','mechanical','organic']).
metrics('Terran SCV',1,1,7,1).
combat('Terran SCV',5,0,15,0,0).

type(terran,'Terran Wraith').
costs('Terran Wraith',150,100,4,900,['Terran Starport']).
stats('Terran Wraith',120,0,200,66,['canMove','flies','mechanical']).
metrics('Terran Wraith',1,1,7,0).
combat('Terran Wraith',8,20,30,5,0).

type(terran,'Terran Science Vessel').
costs('Terran Science Vessel',100,225,4,1200,['Terran Starport','Terran Control Tower','Terran Science Facility']).
stats('Terran Science Vessel',200,0,200,50,['canMove','canDetect','flies','mechanical']).
metrics('Terran Science Vessel',2,2,10,0).

type(terran,'Terran Dropship').
costs('Terran Dropship',100,100,4,750,['Terran Starport','Terran Control Tower']).
stats('Terran Dropship',150,0,0,54,['canMove','flies','mechanical']).
metrics('Terran Dropship',2,2,8,-8).

type(terran,'Terran Battlecruiser').
costs('Terran Battlecruiser',400,300,12,2000,['Terran Starport','Terran Control Tower','Terran Physics Lab']).
stats('Terran Battlecruiser',500,0,200,25,['canMove','flies','mechanical']).
metrics('Terran Battlecruiser',2,2,11,0).
combat('Terran Battlecruiser',25,25,30,6,0).

type(terran,'Terran Vulture Spider Mine').
costs('Terran Vulture Spider Mine',1,0,0,1,[]).
stats('Terran Vulture Spider Mine',20,0,0,160,['canMove']).
metrics('Terran Vulture Spider Mine',1,1,3,0).
combat('Terran Vulture Spider Mine',125,0,22,0,2).

type(terran,'Terran Firebat').
costs('Terran Firebat',50,25,2,360,['Terran Academy','Terran Barracks']).
stats('Terran Firebat',50,0,0,40,['canMove','organic']).
metrics('Terran Firebat',1,1,7,1).
combat('Terran Firebat',8,0,22,1,0).

type(terran,'Terran Medic').
costs('Terran Medic',50,25,2,450,['Terran Academy','Terran Barracks']).
stats('Terran Medic',60,0,200,40,['canMove','organic']).
metrics('Terran Medic',1,1,9,1).

type(zerg,'Zerg Larva').
costs('Zerg Larva',1,1,0,1,['Zerg Hatchery']).
stats('Zerg Larva',25,0,0,0,['organic']).
metrics('Zerg Larva',1,1,4,0).

type(zerg,'Zerg Egg').
costs('Zerg Egg',1,1,0,1,['Zerg Larva']).
stats('Zerg Egg',200,0,0,0,['organic']).
metrics('Zerg Egg',1,1,4,0).

type(zerg,'Zerg Zergling').
costs('Zerg Zergling',50,0,1,420,['Zerg Larva','Zerg Spawning Pool']).
stats('Zerg Zergling',35,0,0,54,['canMove','organic']).
metrics('Zerg Zergling',1,1,5,1).
combat('Zerg Zergling',5,0,8,0,0).

type(zerg,'Zerg Hydralisk').
costs('Zerg Hydralisk',75,25,2,420,['Zerg Larva','Zerg Hydralisk Den']).
stats('Zerg Hydralisk',80,0,0,36,['canMove','organic']).
metrics('Zerg Hydralisk',1,1,6,2).
combat('Zerg Hydralisk',10,10,15,4,0).

type(zerg,'Zerg Ultralisk').
costs('Zerg Ultralisk',200,200,8,900,['Zerg Larva','Zerg Ultralisk Cavern']).
stats('Zerg Ultralisk',400,0,0,51,['canMove','organic']).
metrics('Zerg Ultralisk',2,2,7,4).
combat('Zerg Ultralisk',20,0,15,0,0).

type(zerg,'Zerg Drone').
costs('Zerg Drone',50,0,2,300,['Zerg Larva']).
stats('Zerg Drone',40,0,0,49,['canMove','organic']).
metrics('Zerg Drone',1,1,7,1).
combat('Zerg Drone',5,0,22,1,0).

type(zerg,'Zerg Overlord').
costs('Zerg Overlord',100,0,-16,600,['Zerg Larva']).
stats('Zerg Overlord',200,0,0,8,['canMove','canDetect','flies','organic']).
metrics('Zerg Overlord',2,2,9,-8).

type(zerg,'Zerg Mutalisk').
costs('Zerg Mutalisk',100,100,4,600,['Zerg Larva','Zerg Spire']).
stats('Zerg Mutalisk',120,0,0,66,['canMove','flies','organic']).
metrics('Zerg Mutalisk',2,2,7,0).
combat('Zerg Mutalisk',9,9,30,3,0).

type(zerg,'Zerg Guardian').
costs('Zerg Guardian',50,100,4,600,['Zerg Greater Spire','Zerg Mutalisk']).
stats('Zerg Guardian',150,0,0,25,['canMove','flies','organic']).
metrics('Zerg Guardian',2,2,11,0).
combat('Zerg Guardian',20,0,30,8,0).

type(zerg,'Zerg Queen').
costs('Zerg Queen',100,100,4,750,['Zerg Larva','Zerg Queens Nest']).
stats('Zerg Queen',120,0,200,66,['canMove','flies','organic']).
metrics('Zerg Queen',2,2,10,0).

type(zerg,'Zerg Defiler').
costs('Zerg Defiler',50,150,4,750,['Zerg Larva','Zerg Defiler Mound']).
stats('Zerg Defiler',80,0,200,40,['canMove','organic']).
metrics('Zerg Defiler',1,1,10,2).

type(zerg,'Zerg Scourge').
costs('Zerg Scourge',25,75,1,450,['Zerg Larva','Zerg Spire']).
stats('Zerg Scourge',25,0,0,66,['canMove','flies','organic']).
metrics('Zerg Scourge',1,1,5,0).
combat('Zerg Scourge',0,110,1,0,0).

type(terran,'Terran Valkyrie').
costs('Terran Valkyrie',250,125,6,750,['Terran Starport','Terran Control Tower','Terran Armory']).
stats('Terran Valkyrie',200,0,0,66,['canMove','flies','mechanical']).
metrics('Terran Valkyrie',2,2,8,0).
combat('Terran Valkyrie',0,12,64,6,1).

type(zerg,'Zerg Cocoon').
costs('Zerg Cocoon',1,1,0,1,['Zerg Greater Spire','Zerg Mutalisk']).
stats('Zerg Cocoon',200,0,0,0,['flies','organic']).
metrics('Zerg Cocoon',1,1,4,0).

type(protoss,'Protoss Corsair').
costs('Protoss Corsair',150,100,4,600,['Protoss Stargate']).
stats('Protoss Corsair',100,80,200,66,['canMove','flies','mechanical']).
metrics('Protoss Corsair',1,1,9,0).
combat('Protoss Corsair',0,5,8,5,1).

type(protoss,'Protoss Dark Templar').
costs('Protoss Dark Templar',125,100,4,750,['Protoss Gateway','Protoss Templar Archives']).
stats('Protoss Dark Templar',80,40,0,49,['canMove','organic']).
metrics('Protoss Dark Templar',1,1,7,2).
combat('Protoss Dark Templar',40,0,30,0,0).

type(zerg,'Zerg Devourer').
costs('Zerg Devourer',150,50,4,600,['Zerg Greater Spire','Zerg Mutalisk']).
stats('Zerg Devourer',250,0,0,50,['canMove','flies','organic']).
metrics('Zerg Devourer',2,2,10,0).
combat('Zerg Devourer',0,25,100,6,0).

type(protoss,'Protoss Dark Archon').
costs('Protoss Dark Archon',0,0,8,300,['Protoss Dark Templar','Protoss Dark Templar']).
stats('Protoss Dark Archon',25,200,200,49,['canMove']).
metrics('Protoss Dark Archon',1,1,10,4).

type(protoss,'Protoss Probe').
costs('Protoss Probe',50,0,2,300,['Protoss Nexus']).
stats('Protoss Probe',20,20,0,49,['canMove','mechanical','robotic']).
metrics('Protoss Probe',1,1,8,1).
combat('Protoss Probe',5,0,22,1,0).

type(protoss,'Protoss Zealot').
costs('Protoss Zealot',100,0,4,600,['Protoss Gateway']).
stats('Protoss Zealot',100,60,0,40,['canMove','organic']).
metrics('Protoss Zealot',1,1,7,2).
combat('Protoss Zealot',8,0,22,0,0).

type(protoss,'Protoss Dragoon').
costs('Protoss Dragoon',125,50,4,750,['Protoss Gateway','Protoss Cybernetics Core']).
stats('Protoss Dragoon',100,80,0,50,['canMove','mechanical']).
metrics('Protoss Dragoon',1,1,8,4).
combat('Protoss Dragoon',20,20,30,4,0).

type(protoss,'Protoss High Templar').
costs('Protoss High Templar',50,150,4,750,['Protoss Gateway','Protoss Templar Archives']).
stats('Protoss High Templar',40,40,200,32,['canMove','organic']).
metrics('Protoss High Templar',1,1,7,2).

type(protoss,'Protoss Archon').
costs('Protoss Archon',0,0,8,300,['Protoss High Templar','Protoss High Templar']).
stats('Protoss Archon',10,350,0,49,['canMove']).
metrics('Protoss Archon',1,1,8,4).
combat('Protoss Archon',30,30,20,2,0).

type(protoss,'Protoss Shuttle').
costs('Protoss Shuttle',200,0,4,900,['Protoss Robotics Facility']).
stats('Protoss Shuttle',80,60,0,44,['canMove','flies','mechanical','robotic']).
metrics('Protoss Shuttle',2,1,8,-8).

type(protoss,'Protoss Scout').
costs('Protoss Scout',275,125,6,1200,['Protoss Stargate']).
stats('Protoss Scout',150,100,0,50,['canMove','flies','mechanical']).
metrics('Protoss Scout',2,1,8,0).
combat('Protoss Scout',8,28,30,4,0).

type(protoss,'Protoss Arbiter').
costs('Protoss Arbiter',100,350,8,2400,['Protoss Stargate','Protoss Arbiter Tribunal']).
stats('Protoss Arbiter',200,150,200,50,['canMove','flies','mechanical']).
metrics('Protoss Arbiter',2,2,9,0).
combat('Protoss Arbiter',10,10,45,5,0).

type(protoss,'Protoss Carrier').
costs('Protoss Carrier',350,250,12,2100,['Protoss Stargate','Protoss Fleet Beacon']).
stats('Protoss Carrier',300,150,0,33,['canMove','flies','mechanical']).
metrics('Protoss Carrier',2,2,11,0).
combat('Protoss Carrier',0,0,0,0,0).

type(protoss,'Protoss Interceptor').
costs('Protoss Interceptor',25,0,0,300,['Protoss Carrier']).
stats('Protoss Interceptor',40,40,0,133,['canMove','flies','mechanical']).
metrics('Protoss Interceptor',1,1,6,0).
combat('Protoss Interceptor',6,6,1,4,0).

type(protoss,'Protoss Reaver').
costs('Protoss Reaver',200,100,8,1050,['Protoss Robotics Facility','Protoss Robotics Support Bay']).
stats('Protoss Reaver',100,80,0,17,['canMove','mechanical','robotic']).
metrics('Protoss Reaver',1,1,10,4).
combat('Protoss Reaver',0,0,0,0,0).

type(protoss,'Protoss Observer').
costs('Protoss Observer',25,75,2,600,['Protoss Robotics Facility','Protoss Observatory']).
stats('Protoss Observer',40,20,0,33,['canMove','canDetect','flies','mechanical','robotic']).
metrics('Protoss Observer',1,1,9,0).

type(protoss,'Protoss Scarab').
costs('Protoss Scarab',15,0,0,105,['Protoss Reaver']).
stats('Protoss Scarab',20,10,0,160,['canMove','mechanical']).
metrics('Protoss Scarab',1,1,5,0).
combat('Protoss Scarab',100,0,1,4,1).

type(zerg,'Zerg Lurker Egg').
costs('Zerg Lurker Egg',1,1,0,1,['Lurker Aspect','Zerg Hydralisk']).
stats('Zerg Lurker Egg',200,0,0,0,['organic']).
metrics('Zerg Lurker Egg',1,1,4,0).

type(zerg,'Zerg Lurker').
costs('Zerg Lurker',50,100,4,600,['Lurker Aspect','Zerg Hydralisk']).
stats('Zerg Lurker',125,0,0,58,['canMove','organic']).
metrics('Zerg Lurker',1,1,8,4).
combat('Zerg Lurker',20,0,37,6,0).

type(terran,'Terran Command Center').
costs('Terran Command Center',400,0,-20,1800,['Terran SCV']).
stats('Terran Command Center',1500,0,0,10,['building','mechanical']).
metrics('Terran Command Center',4,3,10,0).

type(terran,'Terran Comsat Station').
costs('Terran Comsat Station',50,50,0,600,['Terran Academy','Terran Command Center']).
stats('Terran Comsat Station',500,0,200,0,['building','mechanical']).
metrics('Terran Comsat Station',2,2,10,0).

type(terran,'Terran Nuclear Silo').
costs('Terran Nuclear Silo',100,100,0,1200,['Terran Science Facility','Terran Covert Ops','Terran Command Center']).
stats('Terran Nuclear Silo',600,0,0,0,['building','mechanical']).
metrics('Terran Nuclear Silo',2,2,8,0).

type(terran,'Terran Supply Depot').
costs('Terran Supply Depot',100,0,-16,600,['Terran SCV']).
stats('Terran Supply Depot',500,0,0,0,['building','mechanical']).
metrics('Terran Supply Depot',3,2,8,0).

type(terran,'Terran Refinery').
costs('Terran Refinery',100,0,0,600,['Terran SCV']).
stats('Terran Refinery',750,0,0,0,['building','mechanical']).
metrics('Terran Refinery',4,2,8,0).

type(terran,'Terran Barracks').
costs('Terran Barracks',150,0,0,1200,['Terran SCV','Terran Command Center']).
stats('Terran Barracks',1000,0,0,10,['building','mechanical']).
metrics('Terran Barracks',4,3,8,0).

type(terran,'Terran Academy').
costs('Terran Academy',150,0,0,1200,['Terran SCV','Terran Barracks']).
stats('Terran Academy',600,0,0,0,['building','mechanical']).
metrics('Terran Academy',3,2,8,0).

type(terran,'Terran Factory').
costs('Terran Factory',200,100,0,1200,['Terran SCV','Terran Barracks']).
stats('Terran Factory',1250,0,0,10,['building','mechanical']).
metrics('Terran Factory',4,3,8,0).

type(terran,'Terran Starport').
costs('Terran Starport',150,100,0,1050,['Terran Factory','Terran SCV']).
stats('Terran Starport',1300,0,0,10,['building','mechanical']).
metrics('Terran Starport',4,3,10,0).

type(terran,'Terran Control Tower').
costs('Terran Control Tower',50,50,0,600,['Terran Starport']).
stats('Terran Control Tower',500,0,0,0,['building','mechanical']).
metrics('Terran Control Tower',2,2,8,0).

type(terran,'Terran Science Facility').
costs('Terran Science Facility',100,150,0,900,['Terran Starport','Terran SCV']).
stats('Terran Science Facility',850,0,0,10,['building','mechanical']).
metrics('Terran Science Facility',4,3,10,0).

type(terran,'Terran Covert Ops').
costs('Terran Covert Ops',50,50,0,600,['Terran Science Facility']).
stats('Terran Covert Ops',750,0,0,0,['building','mechanical']).
metrics('Terran Covert Ops',2,2,8,0).

type(terran,'Terran Physics Lab').
costs('Terran Physics Lab',50,50,0,600,['Terran Science Facility']).
stats('Terran Physics Lab',600,0,0,0,['building','mechanical']).
metrics('Terran Physics Lab',2,2,8,0).

type(terran,'Terran Machine Shop').
costs('Terran Machine Shop',50,50,0,600,['Terran Factory']).
stats('Terran Machine Shop',750,0,0,0,['building','mechanical']).
metrics('Terran Machine Shop',2,2,8,0).

type(terran,'Terran Engineering Bay').
costs('Terran Engineering Bay',125,0,0,900,['Terran SCV','Terran Command Center']).
stats('Terran Engineering Bay',850,0,0,10,['building','mechanical']).
metrics('Terran Engineering Bay',4,3,8,0).

type(terran,'Terran Armory').
costs('Terran Armory',100,50,0,1200,['Terran Factory','Terran SCV']).
stats('Terran Armory',750,0,0,0,['building','mechanical']).
metrics('Terran Armory',3,2,8,0).

type(terran,'Terran Missile Turret').
costs('Terran Missile Turret',75,0,0,450,['Terran SCV','Terran Engineering Bay']).
stats('Terran Missile Turret',200,0,0,0,['building','canDetect','mechanical']).
metrics('Terran Missile Turret',2,2,11,0).
combat('Terran Missile Turret',0,20,15,7,0).

type(terran,'Terran Bunker').
costs('Terran Bunker',100,0,0,450,['Terran SCV','Terran Barracks']).
stats('Terran Bunker',350,0,0,0,['building','mechanical']).
metrics('Terran Bunker',3,2,10,-4).

type(zerg,'Zerg Hatchery').
costs('Zerg Hatchery',300,0,-2,1800,['Zerg Drone']).
stats('Zerg Hatchery',1250,0,0,0,['building','organic']).
metrics('Zerg Hatchery',4,3,9,0).

type(zerg,'Zerg Lair').
costs('Zerg Lair',150,100,-2,1500,['Zerg Hatchery','Zerg Spawning Pool']).
stats('Zerg Lair',1800,0,0,0,['building','organic']).
metrics('Zerg Lair',4,3,10,0).

type(zerg,'Zerg Hive').
costs('Zerg Hive',200,150,-2,1800,['Zerg Lair','Zerg Queens Nest']).
stats('Zerg Hive',2500,0,0,0,['building','organic']).
metrics('Zerg Hive',4,3,11,0).

type(zerg,'Zerg Nydus Canal').
costs('Zerg Nydus Canal',150,0,0,600,['Zerg Hive','Zerg Drone']).
stats('Zerg Nydus Canal',250,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Nydus Canal',2,2,8,0).

type(zerg,'Zerg Hydralisk Den').
costs('Zerg Hydralisk Den',100,50,0,600,['Zerg Drone','Zerg Spawning Pool']).
stats('Zerg Hydralisk Den',850,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Hydralisk Den',3,2,8,0).

type(zerg,'Zerg Defiler Mound').
costs('Zerg Defiler Mound',100,100,0,900,['Zerg Hive','Zerg Drone']).
stats('Zerg Defiler Mound',850,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Defiler Mound',4,2,8,0).

type(zerg,'Zerg Greater Spire').
costs('Zerg Greater Spire',100,150,0,1800,['Zerg Spire']).
stats('Zerg Greater Spire',1000,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Greater Spire',2,2,8,0).

type(zerg,'Zerg Queens Nest').
costs('Zerg Queens Nest',150,100,0,900,['Zerg Lair','Zerg Drone']).
stats('Zerg Queens Nest',850,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Queens Nest',3,2,8,0).

type(zerg,'Zerg Evolution Chamber').
costs('Zerg Evolution Chamber',75,0,0,600,['Zerg Hatchery','Zerg Drone']).
stats('Zerg Evolution Chamber',750,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Evolution Chamber',3,2,8,0).

type(zerg,'Zerg Ultralisk Cavern').
costs('Zerg Ultralisk Cavern',150,200,0,1200,['Zerg Hive','Zerg Drone']).
stats('Zerg Ultralisk Cavern',600,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Ultralisk Cavern',3,2,8,0).

type(zerg,'Zerg Spire').
costs('Zerg Spire',200,150,0,1800,['Zerg Lair','Zerg Drone']).
stats('Zerg Spire',600,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Spire',2,2,8,0).

type(zerg,'Zerg Spawning Pool').
costs('Zerg Spawning Pool',200,0,0,1200,['Zerg Hatchery','Zerg Drone']).
stats('Zerg Spawning Pool',750,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Spawning Pool',3,2,8,0).

type(zerg,'Zerg Creep Colony').
costs('Zerg Creep Colony',75,0,0,300,['Zerg Drone']).
stats('Zerg Creep Colony',400,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Creep Colony',2,2,10,0).

type(zerg,'Zerg Spore Colony').
costs('Zerg Spore Colony',50,0,0,300,['Zerg Evolution Chamber','Zerg Creep Colony']).
stats('Zerg Spore Colony',400,0,0,0,['building','canDetect','organic','requiresCreep']).
metrics('Zerg Spore Colony',2,2,10,0).
combat('Zerg Spore Colony',0,15,15,7,0).

type(zerg,'Zerg Sunken Colony').
costs('Zerg Sunken Colony',50,0,0,300,['Zerg Spawning Pool','Zerg Creep Colony']).
stats('Zerg Sunken Colony',300,0,0,0,['building','organic','requiresCreep']).
metrics('Zerg Sunken Colony',2,2,10,0).
combat('Zerg Sunken Colony',40,0,32,7,0).

type(zerg,'Zerg Extractor').
costs('Zerg Extractor',50,0,0,600,['Zerg Drone']).
stats('Zerg Extractor',750,0,0,0,['building','organic']).
metrics('Zerg Extractor',4,2,7,0).

type(protoss,'Protoss Nexus').
costs('Protoss Nexus',400,0,-18,1800,['Protoss Probe']).
stats('Protoss Nexus',750,750,0,0,['building','mechanical']).
metrics('Protoss Nexus',4,3,11,0).

type(protoss,'Protoss Robotics Facility').
costs('Protoss Robotics Facility',200,200,0,1200,['Protoss Probe','Protoss Cybernetics Core']).
stats('Protoss Robotics Facility',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Robotics Facility',3,2,10,0).

type(protoss,'Protoss Pylon').
costs('Protoss Pylon',100,0,-16,450,['Protoss Probe']).
stats('Protoss Pylon',300,300,0,0,['building','mechanical']).
metrics('Protoss Pylon',2,2,8,0).

type(protoss,'Protoss Assimilator').
costs('Protoss Assimilator',100,0,0,600,['Protoss Probe']).
stats('Protoss Assimilator',450,450,0,0,['building','mechanical']).
metrics('Protoss Assimilator',4,2,10,0).

type(protoss,'Protoss Observatory').
costs('Protoss Observatory',50,100,0,450,['Protoss Probe','Protoss Robotics Facility']).
stats('Protoss Observatory',250,250,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Observatory',3,2,10,0).

type(protoss,'Protoss Gateway').
costs('Protoss Gateway',150,0,0,900,['Protoss Probe','Protoss Nexus']).
stats('Protoss Gateway',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Gateway',4,3,10,0).

type(protoss,'Protoss Photon Cannon').
costs('Protoss Photon Cannon',150,0,0,750,['Protoss Probe','Protoss Forge']).
stats('Protoss Photon Cannon',100,100,0,0,['building','canDetect','mechanical','requiresPsi']).
metrics('Protoss Photon Cannon',2,2,11,0).
combat('Protoss Photon Cannon',20,20,22,7,0).

type(protoss,'Protoss Citadel of Adun').
costs('Protoss Citadel of Adun',150,100,0,900,['Protoss Probe','Protoss Cybernetics Core']).
stats('Protoss Citadel of Adun',450,450,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Citadel of Adun',3,2,10,0).

type(protoss,'Protoss Cybernetics Core').
costs('Protoss Cybernetics Core',200,0,0,900,['Protoss Probe','Protoss Gateway']).
stats('Protoss Cybernetics Core',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Cybernetics Core',3,2,10,0).

type(protoss,'Protoss Templar Archives').
costs('Protoss Templar Archives',150,200,0,900,['Protoss Probe','Protoss Citadel of Adun']).
stats('Protoss Templar Archives',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Templar Archives',3,2,10,0).

type(protoss,'Protoss Forge').
costs('Protoss Forge',150,0,0,600,['Protoss Probe','Protoss Nexus']).
stats('Protoss Forge',550,550,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Forge',3,2,10,0).

type(protoss,'Protoss Stargate').
costs('Protoss Stargate',150,150,0,1050,['Protoss Probe','Protoss Cybernetics Core']).
stats('Protoss Stargate',600,600,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Stargate',4,3,10,0).

type(protoss,'Protoss Fleet Beacon').
costs('Protoss Fleet Beacon',300,200,0,900,['Protoss Probe','Protoss Stargate']).
stats('Protoss Fleet Beacon',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Fleet Beacon',3,2,10,0).

type(protoss,'Protoss Arbiter Tribunal').
costs('Protoss Arbiter Tribunal',200,150,0,900,['Protoss Probe','Protoss Templar Archives']).
stats('Protoss Arbiter Tribunal',500,500,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Arbiter Tribunal',3,2,10,0).

type(protoss,'Protoss Robotics Support Bay').
costs('Protoss Robotics Support Bay',150,100,0,450,['Protoss Probe','Protoss Robotics Facility']).
stats('Protoss Robotics Support Bay',450,450,0,0,['building','mechanical','requiresPsi']).
metrics('Protoss Robotics Support Bay',3,2,10,0).

type(protoss,'Protoss Shield Battery').
costs('Protoss Shield Battery',100,0,0,450,['Protoss Probe','Protoss Gateway']).
stats('Protoss Shield Battery',200,200,200,0,['building','mechanical','requiresPsi']).
metrics('Protoss Shield Battery',3,2,10,0).

