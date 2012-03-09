Specification of the minigame
=============================


The goal of the game is to defend the Monolith

The world is a 3D sphere with in its center the Monolith

Objects will come into the sphere and target the monolith. If the Monolith is hit, it will loose hit points. If the Monolith is destroyed, the game is lost.

The game restart at the last waypoint. The ability of reduce the difficulty is provide


The player control a space ship. The space ship can be destoyed, partially or totally. The player can decide to repair his ship. It is teleported to the Monolith. The operation take 5s. Then the ship is repair by the monotith control zone.

Several waves of ennemy will come.

The next wave will arrive after a few time, or if the player ask for it with a button.


Upgrades
========

All upgrades cost money. All upgrades can be sell.

The player can buy or sell upgrade if in the controller radius of the monolith. The purchases are installed immediatly.


Weapon upgrade
--------------


### Weapon damage
 - Rank 1 : increase the damage by 50%      **500 $**
 - Rank 2 : increase the damage by 100%     **2000 $**
 - Rank 3 : increase the damage by 200%     **8000 $**

### Increase cooling
 - Rank 1 : increase the cooling by 50%      **500 $**
 - Rank 2 : increase the cooling by 100%     **2000 $**
 - Rank 3 : increase the cooling by 200%     **8000 $**

### Increase firerate
 - Rank 1 : increase the firerate by 50%      **500 $**
 - Rank 2 : increase the firerate by 100%     **2000 $**
 - Rank 3 : increase the firerate by 200%     **8000 $**


### Weapons
You can have/buy as many weapon as you Weapon slot level. You can buy multiple time the same weapons.
 - Gun. 1 by default                        **200 $**
    Light weigth. Medium damage, medium firerate. 4 Barrel with fast overheat. Medium range. Small impulse
 - Cannon                                   **800 $**
    Heavy weigth. Big damage, slow firerate. Long range. Big impulse.
 - Shotgun                                  **800 $**
    Medium weigth. Big damage in cone. Small range. Big impulse.
 - Laser                                    **800 $**
    Light weigth. Light damage. Long range. No impulse. Use energy but not ammo
 - Rocket
 - Pulse : big plasma ball, that push all enemies. Use energy but not ammo
 - Missile
 - Rear gun
 - Bomb
 - Mines (how avoid spam ?)


Weapon levels ?

### Ammo limit ???

The ammo are gain very quicky in the monolith control zone. Each weapon has is own ammo amount (? really interresting instead of a common amount poll ? )
 - Rank 1 : increase the ammo limit by 50%      **500 $**
 - Rank 2 : increase the ammo limit by 100%     **2000 $**
 - Rank 3 : increase the ammo limit by 200%     **8000 $**
 

Ship upgrade
------------

### Engine
Increase engine thrust so ship speed and acceleration


### Flats
Control damping

### Wings
    - Increase wings efficiency
    - Wing efficiency controler
    
### Teleport    
- teleport   (
    - distance : instantaneous jump
    - speed : instantaneous modify speed
    - angle instantaneous modify angle (harder to implement)

### Thruster
Enable multidirectionnal thruster to stabilise the ship
    - stabilise
    - translation
    - rotation helper

### Booster
Use ammo to boost speed during very short duration. In hardpoint.


### Big engine
Add a big engine 


### Generator
 - Rank 1 : 1 kW                          **500 $**
 - Rank 2 : 2 kW                          **2000 $**
 - Rank 3 : 3 kW                          **8000 $**

### Battery
 - Rank 1 : 10 kJ (10s for 1kW)            **500 $**
 - Rank 2 : 20 kJ                          **2000 $**
 - Rank 3 : 30 kJ                          **8000 $**



### Hardpoint slot
Point to attach weapon or other equipements.

 - Rank 1 : 1 slots (nose slot)           **200 $**
 - Rank 2 : 2 slots                       **4000 $**
 - Rank 3 : 3 slots                       **6000 $**
 - Rank 4 :  ... 21 slots : 4 on each wing, 1 on nose, 1 on each engine

## Revesable hardpoints
Allow to attach stuff on hardpoint (not nose) turned to rear. Usefull to weapons.

Defense upgrade
---------------


### Armor
Renforce each part of the ship.
 - Rank 1 : Armor + 50%                   **500 $**
 - Rank 2 : Armor + 100%                  **2000 $**
 - Rank 3 : Armor + 200%                  **8000 $**

### Shield
Absorb global damages. Use energy
 - Rank 1 : 1000 damages                  **500 $**
 - Rank 2 : 2000 damages                  **2000 $**
 - Rank 3 : 4000 damages                  **8000 $**

 
### Embedded repair 
Repair the ship slowly outside the monolith control zone.
 - Rank 1 : 10 damages/s                  **500 $**
 - Rank 2 : 20 damages/s                  **2000 $**
 - Rank 3 : 40 damages/s                  **8000 $**

### Nose Ram
Hight resistance block on the nose of the ship. Take the nose slot !
 - Heavy, robust
 - Very heavy, very robust
 - Very very heavy, very very robust

### Pike
Hight resistance pike on the wings or the engine slot. Not available for nose.
 - Heavy, robust
 - Very heavy, very robust
 - Very very heavy, very very robust


### Countermeasure system
 - Rank1 : Alert following projectile
 - Rank2 : Cone attack with big cooling time and ammo use
 
System upgrade
--------------


### Radar
Use energy
 - Rank 1: display arrows (default)
 - Rank 2: display a global minimap
 - Rank 3: ?

### Rear Camera
Use energy

### Target analysize
Use energy
 - Rank1 : Get the hit points

### Follow target
Use energy
 - Rank1 : let the engine getting the same speed as the target
 - Rank2 : let the engine getting the same speed and trajectory as the target 


### Directionnal nose weapon
Use energy
The nose weapon can turn to target enemies

### Energy monitor
Use energy
 - Rank1 : Show the current energy consumtion on the ship
 - Rank1 : Allow to choose the alimentation priority


### Damage monitor
Use energy
 - Rank1 : Show the current damage on the ship
 - Rank1 : Allow to choose the repair priority


Monolith upgrade
----------------

### Monolith repair
 - Rank 1: Increase monolith repair rate
 - ...

### Monolith control zone
- Increase control zone radius
 
### Monolith ship repair
    - Increase ship repair rate

### Harvester
An harvester is a small autonomus ship wicht gather loot on map.
    - Buy a harvester / Increase harvester speed/capacity/resistance/ autorepair speed

### Turret
Get turret to deploy on the map
    - 1 turret
    - 2 turret

### Defence wall
Create defence wall around the monolith (destroyable but can be replaced after a cooldown)


Reputation
=================

Reputation are a mean to increase or reduce the game difficulity dynamicaly.
By default, some Chooses are enabled, others are disabled. Some chooses increase the difficulty, others decrease it.

The player has a reputation bar. Each wave, he can change his responsability. If the responsability is high, it gain a lot of reputation. If low, il lose some reputation.

the reputation increase the interest rate.


Charity (+)
-------
Your consciousness tell you to give some money for the poor orphan of the war.

-50 % interrest

Pity (+)
--------
Capture enemies is more honorable than to kill them. You don't take money from dead people

No money on destroyed enemies


Signal Jamming (default)
------------------------
The ennemy are less efficient

* IA reduce

Free ship (default)
-------------------
You can get a new ship for free if you die, and not loose the game

* loose if destroy

Provocation
-----------
You send provocation signal.

* More enemies come

Ecology
-------

The monolith burn too much power. Reduce the consumtion to avoid the global warming

* no regeneration
* no ship regeneration


Fast waves
----------

The wave last less


Perfect
-------
No hit on monolith allowed

Waves (need a lot of work :( )
=====

Wave 1
------

- One medium slow asteroid


Wave 2
------

- 5 medium slow asteroid


Wave 3
------

- 3 medium slow asteroid
- 2 small fast asteroid



Wave 4
------

- 1 big average speed asteroid
- 3 medium slow asteroid
- 2 small fast asteroid


Wave 4
------

- 2 medium fast asteroid by 2 side


Wave 5
------

Motorized asteroid


Wave 6
------

rocket


Wave 7
------

gunner asteroid

Wave 8+
------



bomb
fighter
bomber
anti figter missile
Huge asteroid
shielded asteroid (resistant at the front)
avanced fighter
repair ship
minifighter
battleship
ram ship
shield ship
boss ?
kamicaze
...

Different AI




















