package com.irr310.client.game;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.irr310.client.GameClient;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.world.Component;
import com.irr310.common.world.Ship;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.Duration;
import com.irr310.server.controller.CapacityController;
import com.irr310.server.controller.LinearEngineController;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class ClientGameEngine extends FramerateEngine {

    private List<CapacityController> capacityControllers;

    public ClientGameEngine() {
        capacityControllers = new ArrayList<CapacityController>();
        framerate = new Duration(15000000);
    }

    @Override
    protected void frame() {
        for (CapacityController controller : capacityControllers) {
            controller.update(framerate.getSeconds());
        }
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new GameEngineEventVisitor());
    }

    private final class GameEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping game engine");
            setRunning(false);
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            Ship ship = event.getShip();

            for (Component component : ship.getComponents()) {
                for (Capacity capacity : component.getCapacities()) {
                    if (capacity instanceof LinearEngineCapacity) {
                        addCapacityController(new LinearEngineController(component, (LinearEngineCapacity) capacity));
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

    }

    private void addCapacityController(CapacityController controller) {
        capacityControllers.add(controller);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }

}
