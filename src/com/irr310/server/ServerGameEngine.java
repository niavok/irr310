package com.irr310.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.Game;
import com.irr310.common.GameTime;
import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.RayResultDescriptor;
import com.irr310.common.event.AddShipEvent;
import com.irr310.common.event.AddWorldObjectEvent;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.BuyUpgradeRequestEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent.Reason;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.DamageEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.GameOverEvent;
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
import com.irr310.common.world.DamageType;
import com.irr310.common.world.Loot;
import com.irr310.common.world.Monolith;
import com.irr310.common.world.Part;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.controller.CapacityController;
import com.irr310.common.world.capacity.controller.GunController;
import com.irr310.common.world.capacity.controller.LinearEngineController;
import com.irr310.common.world.item.ItemSlot;
import com.irr310.common.world.item.ShipSchema;
import com.irr310.common.world.upgrade.UpgradeOwnership;
import com.irr310.server.game.CelestialObjectFactory;
import com.irr310.server.game.ShipFactory;
import com.irr310.server.upgrade.UpgradeFactory;

public class ServerGameEngine extends FramerateEngine {

    private static final double WORLD_SIZE = 1000;
    private List<CapacityController> capacityControllers;

    private int reputation;
    private Wave currentWave;
    private Queue<Wave> waveQueue = new LinkedBlockingQueue<Wave>();
    private Time nextWaveTime;
    boolean stillPlaying = true;
    private Time beginWaveTime;
    private Time lastInterrestTime;
    private Duration interrestInterval;

    public ServerGameEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
        reputation = 0;
        currentWave = null;
        interrestInterval = new Duration(10f);
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new GameEngineEventVisitor());
    }

    @Override
    protected void frame() {
        Time currentTime = GameTime.getGameTime();

        for (CapacityController controller : capacityControllers) {
            controller.update(framerate.getSeconds());
        }
        
        // Interrest
        if(lastInterrestTime.durationTo(currentTime).longer(interrestInterval)) {
            for (Player player : Game.getInstance().getWorld().getPlayers()) {
                // 1% per minute
                player.giveInterrest(player.getMoney() * 0.01/6);
            }
            lastInterrestTime = currentTime;
        }

        // Check world leave
        for (Part part : Game.getInstance().getWorld().getParts()) {
            if (part.getTransform().getTranslation().length() > WORLD_SIZE) {

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
                    if (object.getFirstPart().getTransform().getTranslation().distanceTo(monolith.getFirstPart().getTransform().getTranslation()) < 80) {
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
                                             .multiply(1);

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
        public void visit(AddShipEvent event) {
            Ship ship = null;

            switch (event.getType()) {
                case SIMPLE:
                    ship = ShipFactory.createSimpleShip();
                    ship.setOwner(event.getOwner());
                    break;
                case SIMPLE_FIGHTER:
                    ship = ShipFactory.createSimpleFighter();
                    ship.setOwner(event.getOwner());
                    ShipSchema shipShema = new ShipSchema();
                    //Center slot
                    shipShema.addItemSlot(new ItemSlot(new Vec3(0, 0.5, 0)));
                    //Engine slot                    
                    shipShema.addItemSlot(new ItemSlot(new Vec3(5.5, -3.5, 0)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-5.5, -3.5, 0)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(0, -3.5, 5.5)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(0, -3.5, -5.5)));
                    //Wings slot
                    shipShema.addItemSlot(new ItemSlot(new Vec3(2., -3.5, 1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(2., -3.5, -1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(3., -3.5, 1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(3., -3.5, -1)));
                    
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-2., -3.5, 1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-2., -3.5, -1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-3., -3.5, 1)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-3., -3.5, -1)));
                    
                    shipShema.addItemSlot(new ItemSlot(new Vec3(1, -3.5, 2.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-1, -3.5, 2.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(1, -3.5, 3.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-1, -3.5, 3.)));
                    
                    shipShema.addItemSlot(new ItemSlot(new Vec3(1, -3.5, -2.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-1, -3.5, -2.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(1, -3.5, -3.)));
                    shipShema.addItemSlot(new ItemSlot(new Vec3(-1, -3.5, -3.)));
                    
                    
                    
                    event.getOwner().setShipShema(shipShema);
                    break;
            }

            Game.getInstance().getWorld().addShip(ship, event.getPosition());
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            Ship ship = event.getShip();

            for (Component component : ship.getComponents()) {
                for (Capacity capacity : component.getCapacities()) {
                    if (capacity instanceof LinearEngineCapacity) {
                        addCapacityController(new LinearEngineController(component, (LinearEngineCapacity) capacity));
                    }
                    if (capacity instanceof GunCapacity) {
                        addCapacityController(new GunController(component, (GunCapacity) capacity));
                    }
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

            RayResultDescriptor rayTest = Game.getInstance().getPhysicEngine().rayTest(event.getFrom(), event.getTo());
            if (rayTest != null && rayTest.getPart() != event.getSource())
            {
                // damage = (1-rangePercent^3)
                double damage = event.getDamage() * (1 - Math.pow(rayTest.getHitFraction(), 3));
                applyDamage(rayTest.getPart(), damage, event.getDamageType());
            }
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
            UpgradeFactory.apply(playerUpgrade);
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

            event.getPlayer().giveMoney(event.getUpgrade().getPrices().get(currentRank-1));
            playerUpgrade.setRank(currentRank - 1);
            Game.getInstance().sendToAll(new UpgradeStateChanged(playerUpgrade, event.getPlayer()));
            UpgradeFactory.apply(playerUpgrade);
        }
        
        @Override
        public void visit(GameOverEvent event) {
            Game.getInstance().gameOver();
        }
    }

    private void processCollision(Part part, float impulse) {
        applyDamage(part, impulse, DamageType.PHYSICAL);
    }

    private void applyDamage(Part target, double damage, DamageType damageType) {
        WorldObject parentObject = target.getParentObject();

        double effectiveDamage = damage * (1.0 - parentObject.getPhysicalResistance());

        if (effectiveDamage == 0) {
            return;
        }

        double newDurablility = parentObject.getDurability();
        newDurablility -= effectiveDamage;
        if (newDurablility < 0) {
            newDurablility = 0;
        }

        parentObject.setDurability(newDurablility);

        // TODO: extras damage transmission

        Game.getInstance().sendToAll(new DamageEvent(target, effectiveDamage, damageType));

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
        initWorld();
        lastInterrestTime = GameTime.getGameTime();
    }

    void createWaves() {
        // Create waves
        WaveFactory.createWaves(waveQueue);
        nextWaveTime = GameTime.getGameTime();

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
        nextWaveTime = GameTime.getGameTime().add(currentWave.getDuration());
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
