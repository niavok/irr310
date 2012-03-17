package com.irr310.server.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.GunCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.WingCapacity;
import com.irr310.common.world.item.Item;
import com.irr310.server.GameServer;

public class ComponentFactory {

    public static Component createBigPropeller(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);
        component.setSkin("big_propeller");

        part.setShape(new Vec3(4, 2, 4));

        generateRectangeSlots(component, part);

        LinearEngineCapacity engineCapacity = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacity.theoricalMaxThrust = 10;
        engineCapacity.theoricalMinThrust = -4;
        engineCapacity.theoricalVariationSpeed = 5;
        engineCapacity.currentThrust = 0;
        // engineCapacity.setTargetThrust(4);
        component.addCapacity(engineCapacity);

        return component;
    }

    public static Component createCamera(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("camera");
        Part part = component.getFirstPart();
        part.setMass(1d);

        part.setShape(new Vec3(1, 1, 1));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createFactory(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("factory");
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vec3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createPVCell(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("pvcell");
        Part part = component.getFirstPart();
        part.setMass(5d);

        part.setShape(new Vec3(6, 6, 1));

        generateRectangeSlots(component, part);

        return component;
    }

    public static Component createHangar(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("hangar");
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vec3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createRefinery(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("refinery");
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vec3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createTank(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("tank");
        Part part = component.getFirstPart();
        part.setMass(20d);

        part.setShape(new Vec3(4, 7, 4));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createHarvester(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("harvester");
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vec3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createKernel(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("kernel");
        Part part = component.getFirstPart();
        part.setMass(1d);

        part.setShape(new Vec3(1, 1, 1));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createGun(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("gun");
        Part part = component.getFirstPart();
        part.setMass(2d);
        part.setShape(new Vec3(1, 2, 1));
        Vec3 shape = part.getShape();
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 2, -shape.y / 4, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 2, -shape.y / 4, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 4, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 4, -shape.z / 2));

        GunCapacity gunCapacity = new GunCapacity(GameServer.pickNewId());
        component.addCapacity(gunCapacity);

        return component;
    }

    public static Component createThrusterBlock(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("thrusterBlock");
        Part part = component.getFirstPart();
        part.setMass(4d);

        part.setShape(new Vec3(2, 1, 2));

        Vec3 shape = part.getShape();
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 2, 0));

        LinearEngineCapacity engineCapacityTop = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacityTop.theoricalMaxThrust = 2;
        engineCapacityTop.theoricalMinThrust = -2;
        engineCapacityTop.theoricalVariationSpeed = 10;
        engineCapacityTop.currentThrust = 0;
        component.addCapacity(engineCapacityTop);

        LinearEngineCapacity engineCapacityBottom = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacityBottom.theoricalMaxThrust = 2;
        engineCapacityBottom.theoricalMinThrust = -2;
        engineCapacityBottom.theoricalVariationSpeed = 10;
        engineCapacityBottom.currentThrust = 0;
        component.addCapacity(engineCapacityBottom);

        LinearEngineCapacity engineCapacityLeft = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacityLeft.theoricalMaxThrust = 2;
        engineCapacityLeft.theoricalMinThrust = -2;
        engineCapacityLeft.theoricalVariationSpeed = 10;
        engineCapacityLeft.currentThrust = 0;
        component.addCapacity(engineCapacityLeft);

        LinearEngineCapacity engineCapacityRight = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacityRight.theoricalMaxThrust = 2;
        engineCapacityRight.theoricalMinThrust = -2;
        engineCapacityRight.theoricalVariationSpeed = 10;
        engineCapacityRight.currentThrust = 0;
        component.addCapacity(engineCapacityRight);
        return component;
    }

    public static Component createLightHull(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("hull");
        Part part = component.getFirstPart();
        part.setMass(12d);

        part.setShape(new Vec3(2, 3, 2));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createWing(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("wing");
        Part part = component.getFirstPart();
        part.setMass(5d);

        part.setShape(new Vec3(3, 3, 0.5));

        Vec3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 2, -shape.y / 6, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 2, 0, 0));

        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 6, -0.25, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 6, -0.25, -shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 6, 0.25, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 6, 0.25, -shape.z / 2));

        WingCapacity wingCapacity = new WingCapacity(GameServer.pickNewId());
        component.addCapacity(wingCapacity);

        return component;
    }

    public static Component createReactor(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(4d);
        component.setSkin("reactor");

        part.setShape(new Vec3(1, 2, 1));

        generateVerticalRectangeSlots(component, part);

        LinearEngineCapacity engineCapacity = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacity.theoricalMaxThrust = 10;
        engineCapacity.theoricalMinThrust = -4;
        engineCapacity.theoricalVariationSpeed = 80;
        engineCapacity.currentThrust = 0;
        component.addCapacity(engineCapacity);

        return component;
    }

    public static Component createGate(String name) {
        Component component = createSimpleComponent(name);
        return component;
    }

    public static Component createBeam(String name) {
        Component component = createSimpleComponent(name);
        return component;
    }

    public static Component createPiston(String name) {
        Component component = createSimpleComponent(name);
        return component;
    }

    public static Component createTorqueEngine(String name) {
        Component component = createSimpleComponent(name);
        return component;
    }

    // Tools

    private static Component createSimpleComponent(String name) {
        Component component = new Component(GameServer.pickNewId(), name);

        Part part = new Part(GameServer.pickNewId(), component);
        component.addPart(part);
        return component;
    }

    private static void generateBoxSlots(Component component, Part part) {

        Vec3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, 0, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, 0, -shape.z / 2));

    }

    private static void generateRectangeSlots(Component component, Part part) {

        Vec3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, -shape.y / 2, 0));

    }

    private static void generateVerticalRectangeSlots(Component component, Part part) {

        Vec3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vec3(shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(-shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, 0, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vec3(0, 0, -shape.z / 2));

    }

    public static Component createByItem(Item content) {
        if (content.getName().equals("weapon.gun")) {
            return createGun("weapon.gun");
        }
        throw new RuntimeException("No component found to " + content.getName());
    }

}
