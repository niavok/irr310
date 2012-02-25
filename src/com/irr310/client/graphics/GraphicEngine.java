package com.irr310.client.graphics;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.irr310.client.graphics.gui.GuiFpsIndicator;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.LoadingGameEvent;
import com.irr310.common.event.MinimizeWindowEvent;
import com.irr310.common.event.MouseEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vect2;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
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
        changeRenderer(new BlankGraphicRenderer(this));
        
        canvas.setEnabled(true);
        
        
        fpsIndicator = new GuiFpsIndicator(this);
        canvas.frame();
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

    private final class GraphicEngineEventVisitor extends DefaultEngineEventVisitor {

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
    }

    public V3DContext getV3DContext() {
        return context;
    }

    public Vect2 getViewportSize() {
        return new Vect2(canvas.getWidth(), canvas.getHeight());
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
