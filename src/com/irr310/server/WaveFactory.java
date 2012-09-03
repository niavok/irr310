package com.irr310.server;

import java.util.Queue;
import java.util.Random;

import com.irr310.common.Game;
import com.irr310.common.event.BindAIEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Player;
import com.irr310.common.world.zone.Asteroid;
import com.irr310.common.world.zone.Ship;
import com.irr310.server.game.CelestialObjectFactory;
import com.irr310.server.game.ShipFactory;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class WaveFactory {

    private Player enemies;
    private Random random;

    public WaveFactory() {

    }

    public void createWaves(Queue<Wave> waveQueue) {
        enemies = new Player(GameServer.pickNewId(), "enemies");
        enemies.setColor(new V3DColor(50, 60, 60));
        Game.getInstance().getWorld().addPlayer(enemies);
        random = new Random();

        // Create waves
        Wave wave1 = new Wave(1);
        wave1.setDuration(new Duration(20f));
        wave1.setActiveDuration(new Duration(2f));
        wave1.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {
                Game.getInstance().getWorld().setWorldSize(500);

                double size = 8 + random.nextDouble() * 3;

                float angularSpeed = 1;

                Vec3 position = new Vec3(Game.getInstance().getWorld().getWorldSize() - 50, 0, 0);
                Vec3 linearSpeed = new Vec3(-10 + random.nextDouble() * 1, 0, 0);

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

                //createFighter();
            }

        });
        waveQueue.add(wave1);

        Wave wave2 = new Wave(2);
        wave2.setDuration(new Duration(40f));
        wave2.setActiveDuration(new Duration(4f));
        wave2.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {
                Game.getInstance().getWorld().setWorldSize(800);
                double baseRotationX = random.nextDouble() * 360;
                double baseRotationY = random.nextDouble() * 360;
                double baseRotationZ = random.nextDouble() * 360;

                for (int i = 0; i < 3; i++) {

                    double size = 8 + random.nextDouble() * 3;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(Game.getInstance().getWorld().getWorldSize() - 50, 0, 0);
                    Vec3 linearSpeed = new Vec3(-10 + random.nextDouble() * 1, 0, 0);

                    TransformMatrix rotation = TransformMatrix.identity();
                    rotation.rotateX(Math.toRadians(baseRotationX + random.nextDouble() * 10));
                    rotation.rotateY(Math.toRadians(baseRotationY + random.nextDouble() * 10));
                    rotation.rotateZ(Math.toRadians(baseRotationZ + random.nextDouble() * 10));

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

                createFighter();
                
                //createFighter();
            }

        });
        waveQueue.add(wave2);

        Wave wave3 = new Wave(3);
        wave3.setDuration(new Duration(60f));
        wave3.setActiveDuration(new Duration(10f));
        wave3.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {
                Game.getInstance().getWorld().setWorldSize(1000);
                double baseRotationX = random.nextDouble() * 360;
                double baseRotationY = random.nextDouble() * 360;
                double baseRotationZ = random.nextDouble() * 360;

                for (int i = 0; i < 5; i++) {

                    double size = 8 + random.nextDouble() * 3;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(Game.getInstance().getWorld().getWorldSize() - 50, 0, 0);
                    Vec3 linearSpeed = new Vec3(-10 + random.nextDouble() * 1, 0, 0);

                    TransformMatrix rotation = TransformMatrix.identity();
                    rotation.rotateX(Math.toRadians(baseRotationX + random.nextDouble() * 10));
                    rotation.rotateY(Math.toRadians(baseRotationY + random.nextDouble() * 10));
                    rotation.rotateZ(Math.toRadians(baseRotationZ + random.nextDouble() * 10));

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

                createFighter();
                
                //createFighter();
                //createFighter();
            }

        });
        waveQueue.add(wave3);

        for (int h = 4; h < 50; h++) {
            final int i = h;
            Wave wave = new Wave(i);
            wave.setDuration(new Duration(60f + 5 * i));
            wave.setActiveDuration(new Duration(10f));
            waveQueue.add(wave);
            wave.addWaveEvent(new WaveEvent(new Duration(2f)) {
                public void action() {
                    Game.getInstance().getWorld().setWorldSize(1200 + i * 100);

                    Random random = new Random();
                    double baseRotationX = random.nextDouble() * 360;
                    double baseRotationY = random.nextDouble() * 360;
                    double baseRotationZ = random.nextDouble() * 360;

                    for (int j = 0; j < 6 + i/2; j++) {

                        double size = 8 + random.nextDouble() * 3;

                        float angularSpeed = 1;

                        Vec3 position = new Vec3(Game.getInstance().getWorld().getWorldSize() - 50, 0, 0);
                        Vec3 linearSpeed = new Vec3(-10 - i / 20 + random.nextDouble() * 1, 0, 0);

                        TransformMatrix rotation = TransformMatrix.identity();
                        rotation.rotateX(Math.toRadians(baseRotationX + random.nextDouble() * 10));
                        rotation.rotateY(Math.toRadians(baseRotationY + random.nextDouble() * 10));
                        rotation.rotateZ(Math.toRadians(baseRotationZ + random.nextDouble() * 10));

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

                    createFighter();
                    createFighter();
                    
                    //createFighter();
                    //createFighter();
                    //createFighter();
                }
            });

        }
    }

    private void createFighter() {

        //for(int i = 0; i < 20; i++) {
            Log.trace("createSimpleFighter");
            Ship ship = ShipFactory.createSimpleFighter();
            ship.setOwner(enemies);
    
            Vec3 shipPosition = new Vec3(Game.getInstance().getWorld().getWorldSize() - 50, 0, 0);
            //Vec3 shipPosition = new Vec3(100, 500, 0);
            TransformMatrix shipTransform = TransformMatrix.identity();
            shipTransform.rotateX(random.nextDouble() * 360);
            
            shipTransform.rotateZ(random.nextDouble() * 360);
            shipTransform.rotateY(random.nextDouble() * 360);
            
            TransformMatrix shipRotation = TransformMatrix.identity();
            shipRotation.rotateX(random.nextDouble() * 360);
            shipRotation.rotateY(random.nextDouble() * 360);
            shipRotation.rotateZ(random.nextDouble() * 360);
    
            shipPosition = shipPosition.rotate(shipRotation);
    
            
            
            shipTransform.translate(shipPosition);
            Log.trace("add ship");
            Game.getInstance().getWorld().addShip(ship, shipTransform);
            Log.trace("bindai");
            Game.getInstance().sendToAll(new BindAIEvent(ship));
        //}
    }

}
