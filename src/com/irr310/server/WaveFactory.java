package com.irr310.server;

import java.util.Queue;
import java.util.Random;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Asteroid;
import com.irr310.server.game.CelestialObjectFactory;

public class WaveFactory {

    public static void createWaves(Queue<Wave> waveQueue) {
        // Create waves
        Wave wave1 = new Wave(1);
        wave1.setDuration(new Duration(20f));
        wave1.setActiveDuration(new Duration(2f));
        wave1.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                Random random = new Random();

                double size = 8 + random.nextDouble() * 3;

                float angularSpeed = 1;

                Vec3 position = new Vec3(950, 0, 0);
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
            }

        });
        waveQueue.add(wave1);

        Wave wave2 = new Wave(2);
        wave2.setDuration(new Duration(40f));
        wave2.setActiveDuration(new Duration(4f));
        wave2.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                for (int i = 0; i < 5; i++) {

                    Random random = new Random();

                    double size = 8 + random.nextDouble() * 3;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(950, 0, 0);
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
                }
            }

        });
        waveQueue.add(wave2);

        Wave wave3 = new Wave(3);
        wave3.setDuration(new Duration(60f));
        wave3.setActiveDuration(new Duration(10f));
        wave3.addWaveEvent(new WaveEvent(new Duration(2f)) {

            public void action() {

                for (int i = 0; i < 10; i++) {

                    Random random = new Random();

                    double size = 8 + random.nextDouble() * 3;

                    float angularSpeed = 1;

                    Vec3 position = new Vec3(950, 0, 0);
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
                }
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
                    for (int j = 0; j < 10 + i; j++) {

                        Random random = new Random();

                        double size = 8 + random.nextDouble() * 3;

                        float angularSpeed = 1;

                        Vec3 position = new Vec3(950, 0, 0);
                        Vec3 linearSpeed = new Vec3(-10 - i / 20 + random.nextDouble() * 1, 0, 0);

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

        }

    }
}
