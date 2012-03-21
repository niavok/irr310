package com.irr310.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.Game;
import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.RayResultDescriptor;
import com.irr310.common.event.AddWorldObjectEvent;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.BuyUpgradeRequestEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent.Reason;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.ComponentAddedEvent;
import com.irr310.common.event.ComponentRemovedEvent;
import com.irr310.common.event.DamageEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.GameOverEvent;
import com.irr310.common.event.InventoryChangedEvent;
import com.irr310.common.event.NextWaveEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.SellUpgradeRequestEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.UpgradeStateChanged;
import com.irr310.common.event.WorldReadyEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Asteroid;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;
import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Loot;
import com.irr310.common.world.Monolith;
import com.irr310.common.world.Part;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.WeaponCapacity;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.common.world.capacity.controller.GunController;
import com.irr310.common.world.capacity.controller.LinearEngineController;
import com.irr310.common.world.capacity.controller.ShotgunController;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.server.game.CelestialObjectFactory;
import com.irr310.server.upgrade.UpgradeFactory;

public class ServerGameEngine extends FramerateEngine {

    private List<CapacityController> capacityControllers;

    private int reputation;
    private Wave currentWave;
    private Queue<Wave> waveQueue = new LinkedBlockingQueue<Wave>();
    private Time nextWaveTime;
    boolean stillPlaying = true;
    private Time beginWaveTime;
    private Time lastInterrestTime;
    private Duration interrestInterval;
    private boolean inited;

    public ServerGameEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
        reputation = 0;
        currentWave = null;
        interrestInterval = new Duration(10f);
        inited = false;
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new GameEngineEventVisitor());
    }

    @Override
    protected void frame() {
        if (!inited) {
            return;
        }

        Time currentTime = Time.now(true);

        for (CapacityController controller : capacityControllers) {
            controller.update(framerate.getSeconds());
        }

        // Interrest
        if (lastInterrestTime.durationTo(currentTime).longer(interrestInterval)) {
            for (Player player : Game.getInstance().getWorld().getPlayers()) {
                // 1% per minute
                player.giveInterrest(player.getMoney() * 0.01 / 6);
            }
            lastInterrestTime = currentTime;
        }

        // Check world leave
        double worldSize = Game.getInstance().getWorld().getWorldSize();
        for (Part part : Game.getInstance().getWorld().getParts()) {
            if (part.getTransform().getTranslation().length() > worldSize) {

                if (part.getParentObject() instanceof CelestialObject) {
                    CelestialObject object = (CelestialObject) part.getParentObject();
                    Game.getInstance().getWorld().removeCelestialObject(object, Reason.LEAVE_OUT_WORLD);
                }
            }
        }

        // Check loot gain
        for (CelestialObject object : Game.getInstance().getWorld().getCelestialsObjects()) {
            if (object instanceof Loot) {
                for (Ship ship : Game.getInstance().getWorld().getShips()) {
                    if (ship.getComponentByName("kernel")
                            .getFirstPart()
                            .getTransform()
                            .getTranslation()
                            .distanceTo(object.getFirstPart().getTransform().getTranslation()) < 25) {
                        Loot loot = (Loot) object;

                        Game.getInstance().getWorld().removeCelestialObject(loot, Reason.LOOTED);
                        distachRevenue(loot.getValue());
                    }
                }
            }
        }

        // Check player distance to monolith
        Monolith monolith = null;
        for (CelestialObject object : Game.getInstance().getWorld().getCelestialsObjects()) {
            if (object instanceof Monolith) {
                monolith = (Monolith) object;
                break;
            }
        }

        if (monolith != null) {
            for (CelestialObject object : Game.getInstance().getWorld().getCelestialsObjects()) {
                if (object instanceof Loot) {
                    double distanceTo = object.getFirstPart()
                                              .getTransform()
                                              .getTranslation()
                                              .distanceTo(monolith.getFirstPart().getTransform().getTranslation());
                    if (distanceTo < 25) {
                        Loot loot = (Loot) object;
                        Game.getInstance().getWorld().removeCelestialObject(loot, Reason.LOOTED);
                        distachRevenue(loot.getValue());
                    }

                    // Send loot to monolith
                    Game.getInstance().getWorld().lock();
                    Vec3 lootSpeed = monolith.getFirstPart()
                                             .getTransform()
                                             .getTranslation()
                                             .minus(object.getFirstPart().getTransform().getTranslation())
                                             .normalize()
                                             .multiply(1 + Math.log10(distanceTo) * 1);

                    object.getFirstPart().getLinearSpeed().set(lootSpeed);
                    Game.getInstance().getPhysicEngine().reloadStates(object.getFirstPart());
                    Game.getInstance().getWorld().unlock();
                }
            }

            for (Ship ship : Game.getInstance().getWorld().getShips()) {
                if (ship.getComponentByName("kernel")
                        .getFirstPart()
                        .getTransform()
                        .getTranslation()
                        .distanceTo(monolith.getFirstPart().getTransform().getTranslation()) < 80) {

                    // Can build
                }

            }
        }

        if (stillPlaying) {
            // Next Wave
            if (currentTime.after(nextWaveTime)) {
                nextWave();
                beginWaveTime = currentTime;

            }
        }

        if (stillPlaying) {
            currentWave.update(beginWaveTime.durationTo(currentTime));
        }

    }

    private int distachRevenue(int amount) {
        List<Player> players = Game.getInstance().getWorld().getPlayers();

        // Be generious, round to ceil
        int amountPerPlayer = (int) Math.ceil((float) amount / (float) players.size());
        for (Player player : players) {
            player.giveMoney(amountPerPlayer);

        }
        return amountPerPlayer * players.size();
    }

    private final class GameEngineEventVisitor extends DefaultEngineEventVisitor {

        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping game engine");
            setRunning(false);
        }

        @Override
        public void visit(AddWorldObjectEvent event) {
            Component o = null;

            switch (event.getType()) {
                case CAMERA:
                    o = new Component(GameServer.pickNewId(), "camera");
                    break;
                case LINEAR_ENGINE:
                    o = new Component(GameServer.pickNewId(), "camera");
                    break;
            }

            if (event.getPosition() != null) {
                o.changeTranslation(event.getPosition());
            }

            /*
             * if(event.getRotation() != null) {
             * o.getRotation().set(event.getRotation()); }
             */

            if (event.getLinearSpeed() != null) {
                o.changeLinearSpeed(event.getLinearSpeed());
            }

            if (event.getRotationSpeed() != null) {
                o.changeRotationSpeed(event.getRotationSpeed());
            }

            o.setName(event.getName());

            // Game.getInstance().getWorld().addObject(o);
        }

        @Override
        public void visit(WorldShipAddedEvent event) {

        }

        @Override
        public void visit(ComponentAddedEvent event) {
            Component component = event.getComponent();
            for (Capacity capacity : component.getCapacities()) {
                if (capacity instanceof LinearEngineCapacity) {
                    addCapacityController(new LinearEngineController(component, (LinearEngineCapacity) capacity));
                }
                if (capacity instanceof WeaponCapacity) {
                    if(capacity.getName().equals("gun")) {
                        addCapacityController(new GunController(component, (WeaponCapacity) capacity));
                    } else if(capacity.getName().equals("shotgun")) { 
                        addCapacityController(new ShotgunController(component, (WeaponCapacity) capacity));
                    }
                }
            }
        }

        @Override
        public void visit(ComponentRemovedEvent event) {
            for (Iterator<CapacityController> iterator = capacityControllers.iterator(); iterator.hasNext();) {
                CapacityController capacityController = iterator.next();
                if (capacityController.getComponent() == event.getComponent()) {
                    iterator.remove();
                }
            }
        }

        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }

        @Override
        public void visit(CollisionEvent event) {
            CollisionDescriptor collisionDescriptor = event.getCollisionDescriptor();
            processCollision(collisionDescriptor.getPartA(), collisionDescriptor.getImpulse());
            processCollision(collisionDescriptor.getPartB(), collisionDescriptor.getImpulse());
        }

        @Override
        public void visit(BulletFiredEvent event) {

            List<RayResultDescriptor> rayTestResults = Game.getInstance().getPhysicEngine().rayTest(event.getFrom(), event.getTo());
            for (RayResultDescriptor rayTest : rayTestResults) {

                // Ignore it on the ship
                if (event.getSource().getParentObject() instanceof Component) {
                    Component component = (Component) event.getSource().getParentObject();
                    if (component.getShip().getComponents().contains(rayTest.getPart().getParentObject())) {
                        continue;
                    }
                }

                // damage = (1-rangePercent^3)
                double attenuedDamage = event.getDamage().getWeaponBaseDamage() * (1 - Math.pow(rayTest.getHitFraction(), 3));
                event.getDamage().setBaseDamage(attenuedDamage);
                applyDamage(rayTest.getPart(), event.getDamage());
                impulse(rayTest.getPart(), attenuedDamage, rayTest.getLocalPosition(), event.getTo().minus(event.getFrom()).normalize());

                break;
            }
        }

        private void impulse(Part part, double energy, Vec3 localPosition, Vec3 axis) {
            Game.getInstance().getPhysicEngine().impulse(part, energy, localPosition, axis);
        }

        @Override
        public void visit(CelestialObjectRemovedEvent event) {
            if (event.getObject() instanceof Monolith) {
                Game.getInstance().sendToAll(new GameOverEvent("The monolith is destroyed"));
            } else if (event.getObject() instanceof Asteroid) {
                Loot loot = CelestialObjectFactory.createLoot(5);
                loot.getFirstPart().getLinearSpeed().set(event.getObject().getFirstPart().getLinearSpeed());
                loot.getFirstPart().getTransform().setTranslation(event.getObject().getFirstPart().getTransform().getTranslation());
                Game.getInstance().getWorld().addCelestialObject(loot);
            }
        }

        @Override
        public void visit(WorldReadyEvent event) {
            createWaves();
        }

        @Override
        public void visit(BuyUpgradeRequestEvent event) {

            int currentRank = 0;

            UpgradeOwnership playerUpgrade = event.getPlayer().getUpgradeState(event.getUpgrade());
            currentRank = playerUpgrade.getRank();

            // Check max rank
            if (currentRank >= event.getUpgrade().getMaxRank()) {
                return;
            }

            // Check cost
            if (event.getPlayer().getMoney() < event.getUpgrade().getPrices().get(currentRank)) {
                return;
            }

            event.getPlayer().retireMoney(event.getUpgrade().getPrices().get(currentRank));
            playerUpgrade.setRank(currentRank + 1);
            Game.getInstance().sendToAll(new UpgradeStateChanged(playerUpgrade, event.getPlayer()));
            UpgradeFactory.refresh(event.getPlayer());
        }

        @Override
        public void visit(SellUpgradeRequestEvent event) {

            int currentRank = 0;

            UpgradeOwnership playerUpgrade = event.getPlayer().getUpgradeState(event.getUpgrade());
            currentRank = playerUpgrade.getRank();

            // Check min rank
            if (currentRank <= 0) {
                return;
            }

            event.getPlayer().giveMoney(event.getUpgrade().getPrices().get(currentRank - 1));
            playerUpgrade.setRank(currentRank - 1);
            Game.getInstance().sendToAll(new UpgradeStateChanged(playerUpgrade, event.getPlayer()));
            UpgradeFactory.refresh(event.getPlayer());
        }
        
        @Override
        public void visit(InventoryChangedEvent event) {
            UpgradeFactory.refresh(event.getPlayer());
        }
        @Override
        public void visit(GameOverEvent event) {
            Game.getInstance().gameOver();
        }
    }

    private void processCollision(Part part, double impulse) {
        DamageDescriptor damage = new DamageDescriptor( DamageDescriptor.DamageType.PHYSICAL, 0);
        damage.setBaseDamage(impulse*0.5);
        applyDamage(part, damage);
    }

    private void applyDamage(Part target, DamageDescriptor damage) {
        WorldObject parentObject = target.getParentObject();

        double effectiveDamage = damage.getBaseDamage() * (1.0 - parentObject.getPhysicalResistance()*(1 - damage.armorPenetration));

        if (effectiveDamage == 0) {
            return;
        }

        double newDurablility = parentObject.getDurability();
        newDurablility -= effectiveDamage;
        if (newDurablility < 0) {
            newDurablility = 0;
        }

        parentObject.setDurability(newDurablility);

        damage.setEffectiveDamage(effectiveDamage);
        // TODO: extras damage transmission

        Game.getInstance().sendToAll(new DamageEvent(target, damage));

        if (newDurablility == 0) {
            if (parentObject instanceof CelestialObject) {
                Game.getInstance().getWorld().removeCelestialObject((CelestialObject) parentObject, Reason.DESTROYED);
            }
        }

    }

    private void addCapacityController(CapacityController controller) {
        capacityControllers.add(controller);
    }

    @Override
    protected void init() {
        // World
        UpgradeFactory.initUpgrades();
        UpgradeFactory.refresh();
        initWorld();
        lastInterrestTime = Time.now(true);

        inited = true;
    }

    void createWaves() {
        // Create waves
        WaveFactory.createWaves(waveQueue);
        nextWaveTime = Time.now(true);

        nextWave();
    }

    private void nextWave() {
        currentWave = waveQueue.poll();
        if (currentWave == null) {
            stillPlaying = false;
            Game.getInstance().sendToAll(new GameOverEvent("You win !"));
            return;
        }
        Game.getInstance().sendToAll(new NextWaveEvent(currentWave.getId(), currentWave.getDuration(), currentWave.getActiveDuration()));
        beginWaveTime = nextWaveTime;
        nextWaveTime = Time.now(true).add(currentWave.getDuration());
    }

    private void initWorld() {
        Monolith monolith = new Monolith(GameServer.pickNewId(), "monolith");
        Game.getInstance().getWorld().addCelestialObject(monolith);

        /*
         * for (int i = 0; i < 100; i++) { Random random = new Random(); double
         * sizeBase = random.nextDouble(); double size =
         * (sizeBase*sizeBase)*50+1; float angularSpeed = 1; float position =
         * 1000; float linearSpeed = 5; Asteroid asteroid =
         * CelestialObjectFactory.createAsteroid(size); asteroid.getFirstPart()
         * .getTransform() .translate(random.nextFloat() * position -
         * position/2, random.nextFloat() * position - position/2,
         * random.nextFloat() * position - position/2);
         * asteroid.getFirstPart().getLinearSpeed().set(random.nextFloat() *
         * linearSpeed - linearSpeed/2, random.nextFloat() * linearSpeed -
         * linearSpeed/2, random.nextFloat() * linearSpeed - linearSpeed/2);
         * asteroid.getFirstPart().getRotationSpeed().set(random.nextFloat() *
         * angularSpeed - angularSpeed/2f, random.nextFloat() * angularSpeed -
         * angularSpeed/2f, random.nextFloat() * angularSpeed -
         * angularSpeed/2f); getWorld().addCelestialObject(asteroid); }
         */

    }

    @Override
    protected void end() {
    }

}
