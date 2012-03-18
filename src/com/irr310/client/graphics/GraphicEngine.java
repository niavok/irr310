package com.irr310.client.graphics;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.irr310.client.graphics.gui.GuiFpsIndicator;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.AddGuiComponentEvent;
import com.irr310.common.event.AddWorldObjectEvent;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.BuyUpgradeRequestEvent;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.ComponentAddedEvent;
import com.irr310.common.event.ComponentRemovedEvent;
import com.irr310.common.event.DamageEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.GameOverEvent;
import com.irr310.common.event.InitEngineEvent;
import com.irr310.common.event.InventoryChangedEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.KeyReleasedEvent;
import com.irr310.common.event.LoadingGameEvent;
import com.irr310.common.event.MinimizeWindowEvent;
import com.irr310.common.event.MoneyChangedEvent;
import com.irr310.common.event.MouseEvent;
import com.irr310.common.event.NetworkEvent;
import com.irr310.common.event.NextWaveEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.PlayerLoggedEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.ReloadUiEvent;
import com.irr310.common.event.RemoveGuiComponentEvent;
import com.irr310.common.event.SellUpgradeRequestEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.UpgradeStateChanged;
import com.irr310.common.event.UseScriptEvent;
import com.irr310.common.event.WorldReadyEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.event.WorldSizeChangedEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.ResizeListener;
import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;

public class GraphicEngine extends FramerateEngine {

    final V3DContext context = new V3DContext();

    V3DCanvas canvas;
    private GuiFpsIndicator fpsIndicator;
    GraphicRenderer renderer;

    private GraphicEngineEventVisitor eventVisitor;

    private V3DCameraBinding cameraBinding;
    public GraphicEngine() {
        framerate = new Duration(16666666);
    }

    @Override
    protected void init() {
        
        // canvas = new V3DCanvas(context, 1920, 1200);
        eventVisitor = new GraphicEngineEventVisitor();
        canvas = new V3DCanvas(context, 1280, 1024);
        canvas.setEnabled(true);
        fpsIndicator = new GuiFpsIndicator(this);
        changeRenderer(new BlankGraphicRenderer(this));
        
        canvas.addResizeListener(new ResizeListener() {
            
            @Override
            public void resized(V3DCanvas canvas) {
                Game.getInstance().sendToAll(new ReloadUiEvent());
            }
        });
        
        
        frame();
    }

    @Override
    protected void end() {
        Display.destroy();
    }

    @Override
    protected void frame() {
        renderer.frame();
        canvas.frame();
        fpsIndicator.update();
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(eventVisitor); 
    }

    private final class GraphicEngineEventVisitor implements EngineEventVisitor {

        EngineEventVisitor rendererVisitor = new DefaultEngineEventVisitor();

        public void setRendererVisitor(EngineEventVisitor rendererVisitor) {
            this.rendererVisitor = rendererVisitor;
        }

        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping graphic engine");
            canvas.destroy();
            setRunning(false);
        }

        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
            changeRenderer(new MenuGraphicRenderer(GraphicEngine.this));
        }
        
        @Override
        public void visit(LoadingGameEvent event) {
            LoadingGraphicRenderer loadingRenderer = new LoadingGraphicRenderer(GraphicEngine.this);
            changeRenderer(loadingRenderer);
            loadingRenderer.setMessage(event.getMessage());
        }

        @Override
        public void visit(WorldReadyEvent event) {
            changeRenderer(new WorldRenderer(GraphicEngine.this));
        }
        
        @Override
        public void visit(MouseEvent event) {
            canvas.onMouseEvent(event.getMouseEvent());
        }
        

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }

        @Override
        public void visit(MinimizeWindowEvent event) {
            canvas.hide();
        }

        @Override
        public void visit(KeyPressedEvent event) {
            if (event.getKeyCode() == Keyboard.KEY_F11 && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                System.out.println("Reload shaders");
                context.reloadShader();
                return;
            } else {
                rendererVisitor.visit(event);
            }
        }

        @Override
        public void visit(CelestialObjectAddedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(CelestialObjectRemovedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(ComponentRemovedEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(WorldShipAddedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(CollisionEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(BulletFiredEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(GameOverEvent event) {
            changeRenderer(new MenuGraphicRenderer(GraphicEngine.this, event.getReason()));
        }
        
        @Override
        public void visit(AddGuiComponentEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(RemoveGuiComponentEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(NextWaveEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(DamageEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(MoneyChangedEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(UpgradeStateChanged event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(InventoryChangedEvent event) {
            rendererVisitor.visit(event);
        }
        
        @Override
        public void visit(ComponentAddedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(InitEngineEvent event) {
        }

        @Override
        public void visit(UseScriptEvent event) {
        }

        @Override
        public void visit(AddWorldObjectEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(NetworkEvent event) {
        }

        @Override
        public void visit(PlayerAddedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(KeyReleasedEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(PlayerLoggedEvent event) {
            rendererVisitor.visit(event);            
        }

        @Override
        public void visit(ReloadUiEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(BuyUpgradeRequestEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(SellUpgradeRequestEvent event) {
            rendererVisitor.visit(event);
        }

        @Override
        public void visit(WorldSizeChangedEvent event) {
            rendererVisitor.visit(event);            
        }
        
    }

    public V3DContext getV3DContext() {
        return context;
    }

    public Vec2 getViewportSize() {
        return new Vec2(canvas.getWidth(), canvas.getHeight());
    }

    public float getFps() {
        return fpsIndicator.getLastFps();
    }

    public GraphicRenderer getRenderer() {
        return renderer;
    }
    
    private void changeRenderer(GraphicRenderer renderer) {
        this.renderer = renderer;
        renderer.init();
        eventVisitor.setRendererVisitor(renderer.getEventVisitor());
        
        canvas.addCamera(renderer.getCameraBinding());
        
        if(cameraBinding != null) {
        canvas.removeCamera(cameraBinding);
        }
        cameraBinding = renderer.getCameraBinding();
        
        
        
    }

}
