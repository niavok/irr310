package com.irr310.server.game;

import java.util.List;

import com.irr310.common.Game;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.RocketDescriptor;
import com.irr310.common.world.Ship;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;

public class ShipFactory {

    public static Ship createSimpleShip() {

        World world = Game.getInstance().getWorld();

        Ship newShip = new Ship(GameServer.pickNewId());

        // Kernel
        Component kernel = ComponentFactory.createKernel("kernel");
        kernel.setShipPosition(new Vec3(0, 0, 0));
        world.addComponent(kernel);
        newShip.assign(kernel);

        // Camera
        Component camera = ComponentFactory.createCamera("camera");
        camera.setShipPosition(new Vec3(0, -1, 0));
        world.addComponent(camera);
        newShip.assign(camera);

        // Factory
        Component factory = ComponentFactory.createFactory("factory");
        factory.setShipPosition(new Vec3(0, 0, -2));
        world.addComponent(factory);
        newShip.assign(factory);

        // Tank
        Component tank = ComponentFactory.createTank("tank");
        tank.setShipPosition(new Vec3(0, 5.5, -2));
        world.addComponent(tank);
        newShip.assign(tank);

        // Harvester
        Component harvester = ComponentFactory.createHarvester("harvester");
        harvester.setShipPosition(new Vec3(0, 11, -2));
        world.addComponent(harvester);
        newShip.assign(harvester);

        // Refinery
        Component rafinery = ComponentFactory.createRefinery("rafinery");
        rafinery.setShipPosition(new Vec3(0, -4, -2));
        world.addComponent(rafinery);
        newShip.assign(rafinery);

        // Hangar
        Component hangar = ComponentFactory.createHangar("hangar");
        hangar.setShipPosition(new Vec3(0, -8, -2));
        world.addComponent(hangar);
        newShip.assign(hangar);

        // PVCells
        Component frontLeft = ComponentFactory.createPVCell("frontLeftPVCell");
        frontLeft.setShipPosition(new Vec3(-5, 0, -2));
        world.addComponent(frontLeft);
        newShip.assign(frontLeft);

        Component backLeft = ComponentFactory.createPVCell("backLeftPVCell");
        backLeft.setShipPosition(new Vec3(-5, -6, -2));
        world.addComponent(backLeft);
        newShip.assign(backLeft);

        Component frontRight = ComponentFactory.createPVCell("frontRightPVCell");
        frontRight.setShipPosition(new Vec3(5, 0, -2));
        world.addComponent(frontRight);
        newShip.assign(frontRight);

        Component backRight = ComponentFactory.createPVCell("backRightPVCell");
        backRight.setShipPosition(new Vec3(5, -6, -2));
        world.addComponent(backRight);
        newShip.assign(backRight);

        // Engines
        Component mainLeftPropeller = ComponentFactory.createBigPropeller("mainLeftPropeller");
        mainLeftPropeller.setShipPosition(new Vec3(-10, 0, -2));
        world.addComponent(mainLeftPropeller);
        newShip.assign(mainLeftPropeller);

        Component mainRightPropeller = ComponentFactory.createBigPropeller("mainRightPropeller");
        mainRightPropeller.setShipPosition(new Vec3(10, 0, -2));
        world.addComponent(mainRightPropeller);
        newShip.assign(mainRightPropeller);

        // Links
        newShip.link(kernel, camera, new Vec3(0., -0.5, 0.));
        newShip.link(kernel, factory, new Vec3(0, 0, -0.5));
        newShip.link(factory, tank, new Vec3(0, 2, -2));
        newShip.link(tank, harvester, new Vec3(0, 9, -2));
        newShip.link(factory, rafinery, new Vec3(0, -2, -2));
        newShip.link(rafinery, hangar, new Vec3(0, -6, -2));
        newShip.link(factory, frontLeft, new Vec3(-2, 0, -2));
        newShip.link(frontLeft, backLeft, new Vec3(-5, -3, -2));
        newShip.link(factory, frontRight, new Vec3(2, 0, -2));
        newShip.link(frontRight, backRight, new Vec3(5, -3, -2));
        newShip.link(frontLeft, mainLeftPropeller, new Vec3(-8, 0, -2));
        newShip.link(frontRight, mainRightPropeller, new Vec3(8, 0, -2));

        return newShip;
    }

    public static Ship createSimpleFighter() {

        World world = Game.getInstance().getWorld();

        Ship newShip = new Ship(GameServer.pickNewId());

        // // Kernel
        // Component kernel = ComponentFactory.createKernel("kernel");
        // kernel.setShipPosition(new Vect3(-1, 0, 0));
        // world.addComponent(kernel);
        // newShip.assign(kernel);
        //
        // // Camera
        // Component camera = ComponentFactory.createCamera("camera");
        // camera.setShipPosition(new Vect3(1, 0, 0));
        // world.addComponent(camera);
        // newShip.assign(camera);

        // Gun
        // Component gun = ComponentFactory.createGun("weapon.gun");
        // gun.setShipPosition(new Vec3(0, 0.5, 0));
        // world.addComponent(gun);
        // newShip.assign(gun);

        // CompactThrusterBlock Front
        Component thrusterBlockFront = ComponentFactory.createThrusterBlock("trusterBlock1");
        thrusterBlockFront.setShipPosition(new Vec3(0, -1, 0));
        world.addComponent(thrusterBlockFront);
        newShip.assign(thrusterBlockFront);

        // LightHull
        Component hull = ComponentFactory.createLightHull("kernel");
        hull.setShipPosition(new Vec3(0, -3, 0));
        world.addComponent(hull);
        newShip.assign(hull);

        // CompactThrusterBlock Back
        Component thrusterBlockBack = ComponentFactory.createThrusterBlock("trusterBlock2");
        thrusterBlockBack.setShipPosition(new Vec3(0, -5, 0));
        world.addComponent(thrusterBlockBack);
        newShip.assign(thrusterBlockBack);

        // Wings
        Component wingTop = ComponentFactory.createWing("wingTop");
        wingTop.setShipPosition(new Vec3(0, -3, 2.5));
        wingTop.setShipRotation(new Vec3(0, 90, 0));
        world.addComponent(wingTop);
        newShip.assign(wingTop);

        Component wingBottom = ComponentFactory.createWing("wingBottom");
        wingBottom.setShipPosition(new Vec3(0, -3, -2.5));
        wingBottom.setShipRotation(new Vec3(0, -90, 0));
        world.addComponent(wingBottom);
        newShip.assign(wingBottom);

        Component wingLeft = ComponentFactory.createWing("wingLeft");
        wingLeft.setShipPosition(new Vec3(-2.5, -3, 0));
        world.addComponent(wingLeft);
        newShip.assign(wingLeft);

        Component wingRight = ComponentFactory.createWing("wingRight");
        wingRight.setShipPosition(new Vec3(2.5, -3, 0));
        wingRight.setShipRotation(new Vec3(0, 180, 0));
        world.addComponent(wingRight);
        newShip.assign(wingRight);

        // Engines
        Component topReactor = ComponentFactory.createReactor("topReactor");
        topReactor.setShipPosition(new Vec3(0, -3.5, 4.5));
        world.addComponent(topReactor);
        newShip.assign(topReactor);

        Component bottomReactor = ComponentFactory.createReactor("bottomReactor");
        bottomReactor.setShipPosition(new Vec3(0, -3.5, -4.5));
        world.addComponent(bottomReactor);
        newShip.assign(bottomReactor);

        Component leftReactor = ComponentFactory.createReactor("leftReactor");
        leftReactor.setShipPosition(new Vec3(-4.5, -3.5, 0));
        world.addComponent(leftReactor);
        newShip.assign(leftReactor);

        Component rightReactor = ComponentFactory.createReactor("rightReactor");
        rightReactor.setShipPosition(new Vec3(4.5, -3.5, 0));
        world.addComponent(rightReactor);
        newShip.assign(rightReactor);

        // Links

        // newShip.link(gun, camera, new Vect3(0.5, 0, 0.));
        // newShip.link(gun, kernel, new Vect3(-0.5, 0, 0));
        // newShip.link(gun, thrusterBlockFront, new Vec3(0, -0.5, 0));
        newShip.link(thrusterBlockFront, hull, new Vec3(0, -1.5, 0));
        newShip.link(hull, thrusterBlockBack, new Vec3(0, -4.5, 0));
        newShip.link(hull, wingRight, new Vec3(1, -3, 0));
        newShip.link(hull, wingTop, new Vec3(0, -3, 1));
        newShip.link(hull, wingBottom, new Vec3(0, -3, -1));
        newShip.link(hull, wingLeft, new Vec3(-1, -3, 0));
        newShip.link(wingRight, rightReactor, new Vec3(4, -3.5, 0));
        newShip.link(wingLeft, leftReactor, new Vec3(-4, -3.5, 0));
        newShip.link(wingTop, topReactor, new Vec3(0, -3.5, 4));
        newShip.link(wingBottom, bottomReactor, new Vec3(0, -3.5, -4));

        return newShip;

    }

    public static Ship createOre() {
        Ship ship = new Ship(GameServer.pickNewId());
        return ship;
    }

    public static Ship createRocket(RocketDescriptor rocket, Vec3 initialSpeed, Ship sourceShip) {
        World world = Game.getInstance().getWorld();
        Ship newShip = new Ship(GameServer.pickNewId());
        newShip.setOwner(sourceShip.getOwner());
        newShip.setDestructible(true);
        Component rocketHull = ComponentFactory.createRocket("kernel", rocket, sourceShip);
        rocketHull.setShipPosition(new Vec3(0, 0, 0));
        rocketHull.getFirstPart().getLinearSpeed().set(initialSpeed);
        newShip.assign(rocketHull);

        return newShip;
    }

}
