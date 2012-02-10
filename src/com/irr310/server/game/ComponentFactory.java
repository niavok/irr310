package com.irr310.server.game;

import com.irr310.common.Game;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.GameServer;

public class ComponentFactory {

    public static Component createBigPropeller(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);
        component.setSkin("big_propeller");
        
        part.setShape(new Vect3(4, 2, 4));

        generateRectangeSlots(component, part);

        LinearEngineCapacity engineCapacity = new LinearEngineCapacity(GameServer.pickNewId());
        engineCapacity.theoricalMaxThrust = 10;
        engineCapacity.theoricalMinThrust = -4;
        engineCapacity.theoricalVariationSpeed = 5;
        engineCapacity.currentThrust = 0;
        //engineCapacity.setTargetThrust(4);
        component.addCapacity(engineCapacity);
        
        return component;
    }

    public static Component createCamera(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("camera");
        Part part = component.getFirstPart();
        part.setMass(1d);

        part.setShape(new Vect3(1, 1, 1));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createFactory(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createPVCell(String name) {
        Component component = createSimpleComponent(name);
        component.setSkin("pvcell");
        Part part = component.getFirstPart();
        part.setMass(5d);

        part.setShape(new Vect3(6, 6, 1));

        generateRectangeSlots(component, part);

        return component;
    }

    public static Component createHangar(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createRefinery(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createTank(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(20d);

        part.setShape(new Vect3(4, 7, 4));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createHarvester(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static Component createKernel(String name) {
        Component component = createSimpleComponent(name);
        Part part = component.getFirstPart();
        part.setMass(1d);

        part.setShape(new Vect3(1, 1, 1));

        generateBoxSlots(component, part);

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
        
        Part part = new Part(GameServer.pickNewId());
        component.addPart(part);
        Game.getInstance().getWorld().addPart(part);
        return component;
    }

    private static void generateBoxSlots(Component component, Part part) {

        Vect3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vect3(shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(-shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, -shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, 0, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, 0, -shape.z / 2));

    }

    private static void generateRectangeSlots(Component component, Part part) {

        Vect3 shape = part.getShape();

        component.addSlot(GameServer.pickNewId(), part, new Vect3(shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(-shape.x / 2, 0, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, shape.y / 2, 0));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, -shape.y / 2, 0));

    }
}
