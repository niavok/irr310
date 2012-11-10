package com.irr310.server;

import com.irr310.common.engine.EngineManager;
import com.irr310.common.engine.EventDispatcher;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.game.GameEventVisitor;
import com.irr310.common.event.system.SystemEvent;
import com.irr310.common.event.system.SystemEventVisitor;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.WorldEvent;
import com.irr310.common.world.World;

public class SystemEngine extends FramerateEngine<WorldEvent> implements EventDispatcher<SystemEventVisitor, SystemEvent> {

    private World world;
    private EngineManager<SystemEventVisitor, SystemEvent> engineManager;

    public SystemEngine(WorldEngine worldEngine) {
        world = worldEngine.getWorld();
        engineManager = new EngineManager<SystemEventVisitor, SystemEvent>();
    }

    @Override
    protected void processEvent(WorldEvent e) {
        e.accept(new SystemEngineEventVisitor());
    }

    @Override
    protected void frame() {
    }

    @Override
    protected void onStart() {
        pause(false);
    }
    
    @Override
    protected void onInit() {
    }

    @Override
    protected void onEnd() {
        
    }

    private final class SystemEngineEventVisitor extends DefaultWorldEventVisitor {

//        @Override
//        public void visit(QuitGameEvent event) {
//            java.lang.System.out.println("stopping system engine");
//            setRunning(false);
//        }
//        
//        @Override
//        public void visit(StartEngineEvent event) {
//            pause(false);
//        }
//
//        @Override
//        public void visit(PauseEngineEvent event) {
//            pause(true);
//        }
       
    }

    
    public World getWorld() {
        return world;
    }

    public PhysicEngine getPhysicEngine() {
        return null;
    }

    @Override
    public void sendToAll(SystemEvent event) {
        engineManager.sendToAll(event);
    }
    
    public void registerEventVisitor(SystemEventVisitor visitor) {
        engineManager.registerEventVisitor(visitor);
    }

    public void unregisterEventVisitor(SystemEventVisitor visitor) {
        engineManager.unregisterEventVisitor(visitor);
    }

}
