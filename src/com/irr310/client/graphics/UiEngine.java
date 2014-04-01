package com.irr310.client.graphics;

import org.lwjgl.input.Keyboard;

import com.irr310.client.GameClient;
import com.irr310.client.graphics.ether.activities.MainActivity;
import com.irr310.client.graphics.ether.activities.StatusActivity;
import com.irr310.client.input.InputEngineObserver;
import com.irr310.common.engine.Engine;
import com.irr310.common.engine.Observable;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Color;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.I3dContext.ContextListener;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Surface;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;

public class UiEngine implements Engine {

    private I3dContext context;
    private Surface mMainSurface;
    private Surface statusSurface;
    private Time nextFpsTime;
    private int frameCount;
    private Duration totalDuration;
    private Duration usedDuration;
    private Timestamp mLastTime;
    private InputEngineObserver mInputEngineObserver;
    
    
    public UiEngine() {
    }

    @Override
    public void init() {
        context = I3dContext.getInstance();
        context.initCanvas("IRR310", 1280, 768, "graphics/icons/logo.png");
        context.setContextListener(new UiContextListener());
        
        context.start();
        
        
        
        //canvas = new V3DCanvas(context, 1664, 936);
        //canvas.setEnabled(true);
        
        statusSurface = SurfaceFactory.buildAbsoluteHeightRelativeWidthSurface(context, 0, 0, 100, 25);
        
        
        
        statusSurface.setBackgroundColor(Color.black);
        mMainSurface = SurfaceFactory.buildFullscreenSurface(context, 25,0,0,0);
        mMainSurface.setBackgroundColor(Color.white);
        context.addSurface(mMainSurface);
        context.addSurface(statusSurface);
        
        
        //fpsIndicator = new GuiFpsIndicator(this);
        //changeRenderer(new BlankGraphicRenderer(this));

        /*canvas.addResizeListener(new ResizeListener() {
            @Override
            public void resized(V3DCanvas canvas) {
                //Game.getInstance().sendToAll(new ReloadUiEvent());
            }
        });*/
        
        Timestamp time = Time.getTimestamp();
        
        context.update(time);
        
        mLastTime = time;
        nextFpsTime = time.getTime().add(new Duration(5.0f));
        frameCount = 0;
        usedDuration = new Duration(0);
        totalDuration = new Duration(0);
        
        mInputEngineObserver = new UiInputEngineObserver();
        GameClient.getInstance().getInputEngine().getInputEnginObservable().register(this, mInputEngineObserver);
        
    }
    
    private final class UiContextListener implements ContextListener {
        @Override
        public void onQuit() {
            notifyQuitEvent();
        }
    }

    @Override
    public void start() {
        
        
        
        
        Bundle bundle = new Bundle(new MainActivity.MainActivityListener() {

            @Override
            public void onDone() {
                Bundle statusBundle = new Bundle(new StatusActivity.StatusActivityController() {

                    @Override
                    public Surface getControlledSurface() {
                        return mMainSurface;
                    }
                    
                    @Override
                    public void onQuit() {
                        notifyQuitEvent();
                    }
                    
                });
                
                statusSurface.startActivity(new Intent(StatusActivity.class, statusBundle));                
            }
            
        });
        
      
        
        // The main activity will start the StatusActivity
        mMainSurface.startActivity(new Intent(MainActivity.class, bundle));
        context.show();
    }


    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
        context.destroy();
    }
    
    @Override
    public void tick(Timestamp time) {
        Time beginTime = Time.now(false);
        context.update(time);
        //currentActivity.frame(Time.now(false), Time.now(true));
        //renderer.frame();
        //canvas.frame();
        //fpsIndicator.update();
        Duration durationToNow = beginTime.getDurationToNow(false);
        
        usedDuration = usedDuration.add(durationToNow);
        totalDuration = totalDuration.add(mLastTime.getTime().durationTo(time.getTime()));
        frameCount++;
        
        if(beginTime.after(nextFpsTime)) {
            float fps = (float) frameCount / 5.0f;
            float load = 100* usedDuration.getSeconds() / totalDuration.getSeconds();
            
//            Log.console("fps="+fps+" | load="+load+"%");
            
            usedDuration  = new Duration(0);
            totalDuration  = new Duration(0);
            frameCount = 0;
            nextFpsTime = beginTime.add(new Duration(5.0f));
        }
        
        mLastTime =  time;
    }

    private class UiInputEngineObserver implements InputEngineObserver {
        @Override
        public void onMouseEvent(V3DMouseEvent event) {
            context.onMouseEvent(event);
        }

        @Override
        public void onKeyEvent(V3DKeyEvent event) {
            if (event.getKeyCode() == Keyboard.KEY_F11 && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                Log.console("Reload ui");
                context.clearCaches();
                context.reloadUi();
                //System.out.println("Reload shaders");
                //context.reloadShader();
                return;
            } else {
                context.onKeyEvent(event);
            }            
        }
        
        @Override
        public void onControllerEvent(V3DControllerEvent event) {
            context.onControllerEvent(event);
        }

        @Override
        public void onQuitEvent() {
        }
    }
    
//    private final class GraphicEngineEventVisitor extends DefaultGameEventVisitor {

//        EngineEventVisitor rendererVisitor = new DefaultEngineEventVisitor();
//
//        public void setRendererVisitor(EngineEventVisitor rendererVisitor) {
//            this.rendererVisitor = rendererVisitor;
//        }
//
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
        
//    }

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
    
    
    // Observers
    private Observable<UiEngineObserver> mUiEngineObservable = new Observable<UiEngineObserver>();
    
    public Observable<UiEngineObserver> getUiEnginObservable() {
        return mUiEngineObservable;
    }
    
    private void notifyQuitEvent() {
        for(UiEngineObserver observer : mUiEngineObservable.getObservers()) {
            observer.onQuitEvent();
        }
    }
}
