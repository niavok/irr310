package com.irr310.server.game;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.SimpleComponent;
import com.irr310.server.GameServer;

public class ComponentFactory {

    public static SimpleComponent createBigPropeller() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 2, 4));

        generateRectangeSlots(component, part);

        return component;
    }

    public static SimpleComponent createCamera() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(1d);

        part.setShape(new Vect3(1, 1, 1));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createFactory() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createPVCell() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(5d);

        part.setShape(new Vect3(6, 6, 1));

        generateRectangeSlots(component, part);

        return component;
    }

    public static SimpleComponent createHangar() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createRefinery() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createTank() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(20d);

        part.setShape(new Vect3(4, 7, 4));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createHarvester() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(48d);

        part.setShape(new Vect3(4, 4, 3));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createKernel() {
        SimpleComponent component = createSimpleComponent();
        Part part = component.getPart();
        part.setMass(1d);

        part.setShape(new Vect3(1, 1, 1));

        generateBoxSlots(component, part);

        return component;
    }

    public static SimpleComponent createGate() {
        SimpleComponent component = createSimpleComponent();
        return component;
    }

    public static SimpleComponent createBeam() {
        SimpleComponent component = createSimpleComponent();
        return component;
    }

    public static SimpleComponent createPiston() {
        SimpleComponent component = createSimpleComponent();
        return component;
    }

    public static SimpleComponent createTorqueEngine() {
        SimpleComponent component = createSimpleComponent();
        return component;
    }

    // Tools

    private static SimpleComponent createSimpleComponent() {
        return new SimpleComponent(GameServer.pickNewId(), GameServer.pickNewId());
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
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, 0, shape.z / 2));
        component.addSlot(GameServer.pickNewId(), part, new Vect3(0, 0, -shape.z / 2));

    }
}
