package com.irr310.client.graphics;

import org.lwjgl.opengl.Display;

import com.irr310.client.graphics.ether.activities.MainActivity;
import com.irr310.common.Game;
import com.irr310.common.engine.EventDispatcher;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.game.GameEvent;
import com.irr310.common.event.game.GameEventVisitor;
import com.irr310.common.event.game.KeyPressedEvent;
import com.irr310.common.event.game.KeyReleasedEvent;
import com.irr310.common.event.game.MouseEvent;
import com.irr310.common.event.game.QuitGameEvent;
import com.irr310.common.tools.Vec2;
import com.irr310.i3d.Color;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.I3dContext.ContextListener;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Surface;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;

public class UiEngine extends FramerateEngine<GameEvent> {

    private I3dContext context;
    private GraphicEngineEventVisitor eventVisitor;
    private Surface mainSurface;
    private Surface statusSurface;
    private final EventDispatcher<GameEvent> dispatcher;

    public UiEngine(EventDispatcher<GameEvent> dispatcher) {
        this.dispatcher = dispatcher;
        framerate = new Duration(16666666);
    }

    @Override
    protected void onInit() {
        context = I3dContext.getInstance();
        context.initCanvas("IRR310", 1664, 936);
        context.setContextListener(new UiContextListener());
        
        context.start();
        
        
        
        //canvas = new V3DCanvas(context, 1664, 936);
        //canvas.setEnabled(true);
        
        statusSurface = SurfaceFactory.buildAbsoluteHeightRelativeWidthSurface(context, 0, 0, 100, 25);
        
        
        
        statusSurface.setBackgroundColor(Color.black);
        mainSurface = SurfaceFactory.buildFullscreenSurface(context, 25,0,0,0);
        mainSurface.setBackgroundColor(Color.white);
        context.addSurface(mainSurface);
        context.addSurface(statusSurface);
        
        
        eventVisitor = new GraphicEngineEventVisitor();
        //fpsIndicator = new GuiFpsIndicator(this);
        //changeRenderer(new BlankGraphicRenderer(this));

        /*canvas.addResizeListener(new ResizeListener() {
            @Override
            public void resized(V3DCanvas canvas) {
                //Game.getInstance().sendToAll(new ReloadUiEvent());
            }
        });*/
        
        context.update(Time.now(false), Time.now(true));
    }
    
    @Override
    protected void onStart() {
        pause(false);
        mainSurface.startActivity(new Intent(MainActivity.class));
        context.show();
    }


    @Override
    protected void onEnd() {
        Display.destroy();
    }

    @Override
    protected void frame() {
        context.update(Time.now(false), Time.now(true));
        //currentActivity.frame(Time.now(false), Time.now(true));
        //renderer.frame();
        //canvas.frame();
        //fpsIndicator.update();
    }

    @Override
    protected void processEvent(GameEvent e) {
        e.accept(eventVisitor);
    }

    private final class UiContextListener implements ContextListener {
        @Override
        public void onQuit() {
            dispatcher.sendToAll(new QuitGameEvent());
        }
    }

    private final class GraphicEngineEventVisitor implements GameEventVisitor {

//        EngineEventVisitor rendererVisitor = new DefaultEngineEventVisitor();
//
//        public void setRendererVisitor(EngineEventVisitor rendererVisitor) {
//            this.rendererVisitor = rendererVisitor;
//        }
//
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping graphic engine");
            context.destroy();
            setRunning(false);
        }
//
//        @Override
//        public void visit(StartEngineEvent event) {
//            pause(false);
//            mainSurface.startActivity(new Intent(MainActivity.class));
//            context.show();
//            //changeRenderer(new MenuGraphicRenderer(UiEngine.this));
//        }
//
//        @Override
//        public void visit(LoadingGameEvent event) {
//            LoadingGraphicRenderer loadingRenderer = new LoadingGraphicRenderer(UiEngine.this);
////            changeRenderer(loadingRenderer);
////            loadingRenderer.setMessage(event.getMessage());
//        }
//
//        @Override
//        public void visit(WorldReadyEvent event) {
//            changeRenderer(new WorldRenderer(UiEngine.this));
//        }
//
        @Override
        public void visit(MouseEvent event) {
            context.onMouseEvent(event.getMouseEvent());
        }
//
//        @Override
//        public void visit(PauseEngineEvent event) {
//            pause(true);
//        }
//
//        @Override
//        public void visit(MinimizeWindowEvent event) {
//            /*canvas.hide();*/
//        }
//
        @Override
        public void visit(KeyPressedEvent event) {
            /*if (event.getKeyCode() == Keyboard.KEY_F11 && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                System.out.println("Reload shaders");
                context.reloadShader();
                return;
            } else {
                rendererVisitor.visit(event);
            }*/
        }
//
//        @Override
//        public void visit(CelestialObjectAddedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(CelestialObjectRemovedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(ComponentRemovedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(WorldShipAddedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(CollisionEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(BulletFiredEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(GameOverEvent event) {
//            changeRenderer(new MenuGraphicRenderer(UiEngine.this, event.getReason()));
//        }
//
//        @Override
//        public void visit(AddGuiComponentEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(RemoveGuiComponentEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(NextWaveEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(DamageEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(MoneyChangedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(UpgradeStateChanged event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(InventoryChangedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(ComponentAddedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(InitEngineEvent event) {
//        }
//
//        @Override
//        public void visit(UseScriptEvent event) {
//        }
//
//        @Override
//        public void visit(AddWorldObjectEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(NetworkEvent event) {
//        }
//
//        @Override
//        public void visit(PlayerAddedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
        @Override
        public void visit(KeyReleasedEvent event) {
//            rendererVisitor.visit(event);
        }
//
//        @Override
//        public void visit(PlayerLoggedEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(ReloadUiEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(BuyUpgradeRequestEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(SellUpgradeRequestEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(RocketFiredEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(WorldShipRemovedEvent event) {
//            rendererVisitor.visit(event);
//        }
//        
//        @Override
//        public void visit(ExplosionFiredEvent event) {
//            rendererVisitor.visit(event);
//        }
//        
//        @Override
//        public void visit(BindAIEvent event) {
//            rendererVisitor.visit(event);
//        }
//
//        @Override
//        public void visit(FactionAddedEvent event) {
//            // TODO Auto-generated method stub
//            
//        }
        
    }

    public V3DContext getV3DContext() {
        //return context;
        return null;
    }

    public Vec2 getViewportSize() {
        //return new Vec2(canvas.getWidth(), canvas.getHeight());
        return null;
    }

    public float getFps() {
        //return fpsIndicator.getLastFps();
        return 0;
    }

//    public GraphicRenderer getRenderer() {
//        //return renderer;
//        return null;
//    }

//    private void changeRenderer(GraphicRenderer renderer) {
//        /*this.renderer = renderer;
//        renderer.init();
//        eventVisitor.setRendererVisitor(renderer.getEventVisitor());
//
//        canvas.addCamera(renderer.getCameraBinding());
//
//        if (cameraBinding != null) {
//            canvas.removeCamera(cameraBinding);
//        }
//        cameraBinding = renderer.getCameraBinding();*/
//
//    }

    public void addPopup(V3DContainer popup, V3DContainer parent) {
        /*renderer.getPopupLayer().removeAll();
        int parentAbsoluteX = parent.getAbsoluteX();
        int parentAbsoluteY = parent.getAbsoluteY();
        Dimension parentSize = parent.getSize();

        int defaultX = parentAbsoluteX + parentSize.getWidth();
        int defaultY = parentAbsoluteY;

        // TODO: implement limit cases

        popup.setPosition(defaultX + 15, canvas.getHeight() - defaultY + 15);
        renderer.getPopupLayer().add(popup);*/
    }

    public void removePopup(V3DContainer popup) {
        /*renderer.getPopupLayer().remove(popup);*/
    }
}
