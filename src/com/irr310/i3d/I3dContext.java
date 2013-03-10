package com.irr310.i3d;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.irr310.common.Game;
import com.irr310.common.event.game.QuitGameEvent;
import com.irr310.i3d.fonts.Font;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class I3dContext {

    private static final I3dContext instance = new I3dContext();
    private I3dCanvas canvas;
    private Graphics graphics;
    private List<Surface> surfaceList = new ArrayList<Surface>();
    private Font defaultFont;
    private TextureManager textureManager;
    private boolean preloaded;
    private ContextListener listener;

    public static I3dContext getInstance() {
        return instance;
    }

    private I3dContext() {
        textureManager = new TextureManager();
        preloaded = false;
    }

    public void initCanvas(String title, int width, int height) {
        canvas = new I3dCanvas(this, title, width, height);
        graphics = Graphics.getInstance();
        I3dRessourceManager.getInstance().setGraphics(graphics);
    }

    public void start() {
        canvas.init();
    }

    public I3dCanvas getCanvas() {
        return canvas;
    }

    public void addSurface(Surface surface) {
        surfaceList.add(surface);
        surface.configure(canvas.getWidth(), canvas.getHeight());
    }

    public void update(Time absTime, Time gameTime) {
        for (Surface surface : surfaceList) {
            surface.update(absTime, gameTime);
        }

        canvas.draw(graphics);
    }

    public List<Surface> getSurfaceList() {
        return surfaceList;
    }

    public void show() {
        canvas.show();
    }

    public void destroy() {
        canvas.destroy();
    }

    public void preload() {
        new Thread() {

            public void run() {
                // Generate font cache
                defaultFont = I3dRessourceManager.getInstance().loadFont("default@fonts");

                // Generate shaders

                preloaded = true;
            };
        }.start();
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public boolean isPreloaded() {
        return preloaded;
    }

    public void onMouseEvent(V3DMouseEvent mouseEvent) {

        // if(mouseEvent.getAction() != Action.MOUSE_CLICKED) {
        // return;
        // }

        for (Surface surface : surfaceList) {
            if (surface.contains(mouseEvent.getX(), mouseEvent.getY())) {
                V3DMouseEvent topLeftEvent = new V3DMouseEvent(mouseEvent.getAction(), mouseEvent.getX() - surface.x, (surface.y + surface.height)
                        - mouseEvent.getY(), mouseEvent.getButton());
                // Log.debug("topLeftEvent: x="+topLeftEvent.getX()+" y="+topLeftEvent.getY());

                surface.onMouseEvent(topLeftEvent);
                break;
            }
        }
    }

    public void onKeyEvent(V3DKeyEvent mouseEvent) {

        // if(mouseEvent.getAction() != Action.MOUSE_CLICKED) {
        // return;
        // }

        // for (Surface surface : surfaceList) {
        // if(surface.contains(mouseEvent.getX(), mouseEvent.getY())) {
        // V3DMouseEvent topLeftEvent = new
        // V3DMouseEvent(mouseEvent.getAction(), mouseEvent.getX() - surface.x,
        // (surface.y + surface.height) - mouseEvent.getY(),
        // mouseEvent.getButton());
        // //
        // Log.debug("topLeftEvent: x="+topLeftEvent.getX()+" y="+topLeftEvent.getY());
        //
        // surface.onMouseEvent(topLeftEvent);
        // break;
        // }
        // }
    }

    public void notifyQuit() {
        listener.onQuit();
    };

    public void setContextListener(ContextListener listener) {
        this.listener = listener;
    }

    public interface ContextListener {
        void onQuit();
    }
}
