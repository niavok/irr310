package com.irr310.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.Game;
import com.irr310.common.GameTime;
import com.irr310.common.engine.CollisionDescriptor;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.RayResultDescriptor;
import com.irr310.common.event.AddShipEvent;
import com.irr310.common.event.AddWorldObjectEvent;
import com.irr310.common.event.BulletFiredEvent;
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
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldReadyEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.TransformMatrix;
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
import com.irr310.server.game.CelestialObjectFactory;
import com.irr310.server.game.ShipFactory;

public class ServerGameEngine extends FramerateEngine {

    private static final double WORLD_SIZE = 1000;
    private List<CapacityController> capacityControllers;

    private int reputation;
    private Wave currentWave;
    private Queue<Wave> waveQueue = new LinkedBlockingQueue<Wave>();
    private Time nextWaveTime;
    boolean stillPlaying = true;
    private Time beginWaveTime;

    public ServerGameEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
        reputation = 0;
        currentWave = null;
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

                        loot(loot, ship.getOwner());

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

                    int embeddedMoney = ship.getOwner().getEmbeddedMoney();

                    if (embeddedMoney > 0) {
                        ship.getOwner().retireMoney(distachRevenue(embeddedMoney), true);
                    }

                }

            }
        }

        if (stillPlaying) {
            // Next Wave
            if (currentTime.after(nextWaveTime)) {
                nextWave();
                beginWaveTime = currentTime;

                // Interrest (10%);
                for (Player player : Game.getInstance().getWorld().getPlayers()) {
                    player.giveMoney((int) (player.getMoney() * 0.1), false);
                }

            }
        }

        if (stillPlaying) {
            currentWave.update(beginWaveTime.durationTo(currentTime));
        }

    }

    private int distachRevenue(int amount) {
        List<Player> players = Game.getInstance().getWorld().getPlayers();

        int amountPerPlayer = amount / players.size();
        for (Player player : players) {
            player.giveMoney(amountPerPlayer, false);

        }
        return amountPerPlayer * players.size();
    }

    private void loot(Loot loot, Player player) {
        Game.getInstance().getWorld().removeCelestialObject(loot, Reason.LOOTED);
        player.giveMoney(loot.getValue(), true);
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
            if (rayTest != null) {
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
                Loot loot = CelestialObjectFactory.createLoot(50);
                loot.getFirstPart().getLinearSpeed().set(event.getObject().getFirstPart().getLinearSpeed());
                loot.getFirstPart().getTransform().setTranslation(event.getObject().getFirstPart().getTransform().getTranslation());
                Game.getInstance().getWorld().addCelestialObject(loot);
            }
        }

        @Override
        public void visit(WorldReadyEvent event) {
            createWaves();
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
        initWorld();
    }

    void createWaves() {
        // Create waves
        Wave wave1 = new Wave(1);
        wave1.setDuration(new Duration(40f));
        wave1.setActiveDuration(new Duration(2f));
        wave1.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                Random random = new Random();

                double size = 8;

                float angularSpeed = 1;

                Vec3 position = new Vec3(950, 0, 0);
                Vec3 linearSpeed = new Vec3(-10, 0, 0);

                TransformMatrix rotation = TransformMatrix.identity();
                rotation.rotateX(random.nextDouble() * 360);
                rotation.rotateY(random.nextDouble() * 360);
                rotation.rotateZ(random.nextDouble() * 360);

                position = position.rotate(rotation);
                linearSpeed = linearSpeed.rotate(rotation);

                Asteroid asteroid = CelestialObjectFactory.createAsteroid(size);
                asteroid.getFirstPart().getTransform().translate(position);
                asteroid.getFirstPart().getLinearSpeed().set(linearSpeed);
                asteroid.getFirstPart()
                        .getRotationSpeed()
                        .set(random.nextFloat() * angularSpeed - angularSpeed / 2f,
                             random.nextFloat() * angularSpeed - angularSpeed / 2f,
                             random.nextFloat() * angularSpeed - angularSpeed / 2f);

                Game.getInstance().getWorld().addCelestialObject(asteroid);
            }

        });
        waveQueue.add(wave1);

        Wave wave2 = new Wave(2);
        wave2.setDuration(new Duration(100f));
        wave2.setActiveDuration(new Duration(4f));
        wave2.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                for (int i = 0; i < 5; i++) {

                    Random random = new Random();

                    double size = 8;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(950, 0, 0);
                    Vec3 linearSpeed = new Vec3(-10, 0, 0);

                    TransformMatrix rotation = TransformMatrix.identity();
                    rotation.rotateX(random.nextDouble() * 360);
                    rotation.rotateY(random.nextDouble() * 360);
                    rotation.rotateZ(random.nextDouble() * 360);

                    position = position.rotate(rotation);
                    linearSpeed = linearSpeed.rotate(rotation);

                    Asteroid asteroid = CelestialObjectFactory.createAsteroid(size);
                    asteroid.getFirstPart().getTransform().translate(position);
                    asteroid.getFirstPart().getLinearSpeed().set(linearSpeed);
                    asteroid.getFirstPart()
                            .getRotationSpeed()
                            .set(random.nextFloat() * angularSpeed - angularSpeed / 2f,
                                 random.nextFloat() * angularSpeed - angularSpeed / 2f,
                                 random.nextFloat() * angularSpeed - angularSpeed / 2f);

                    Game.getInstance().getWorld().addCelestialObject(asteroid);
                }
            }

        });
        waveQueue.add(wave2);

        Wave wave3 = new Wave(3);
        wave3.setDuration(new Duration(140f));
        wave3.setActiveDuration(new Duration(10f));
        wave3.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                for (int i = 0; i < 10; i++) {

                    Random random = new Random();

                    double size = 8;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(950, 0, 0);
                    Vec3 linearSpeed = new Vec3(-10, 0, 0);

                    TransformMatrix rotation = TransformMatrix.identity();
                    rotation.rotateX(random.nextDouble() * 360);
                    rotation.rotateY(random.nextDouble() * 360);
                    rotation.rotateZ(random.nextDouble() * 360);

                    position = position.rotate(rotation);
                    linearSpeed = linearSpeed.rotate(rotation);

                    Asteroid asteroid = CelestialObjectFactory.createAsteroid(size);
                    asteroid.getFirstPart().getTransform().translate(position);
                    asteroid.getFirstPart().getLinearSpeed().set(linearSpeed);
                    asteroid.getFirstPart()
                            .getRotationSpeed()
                            .set(random.nextFloat() * angularSpeed - angularSpeed / 2f,
                                 random.nextFloat() * angularSpeed - angularSpeed / 2f,
                                 random.nextFloat() * angularSpeed - angularSpeed / 2f);

                    Game.getInstance().getWorld().addCelestialObject(asteroid);
                }
            }

        });
        waveQueue.add(wave3);

        
        for(int i = 4; i < 50; i++) {
            Wave wave = new Wave(i);
            wave.setDuration(new Duration(60f));
            wave.setActiveDuration(new Duration(10f));
            waveQueue.add(wave);
        }
        
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
