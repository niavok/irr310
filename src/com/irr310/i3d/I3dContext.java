package com.irr310.i3d;

import java.util.ArrayList;
import java.util.List;


import com.irr310.i3d.fonts.Font;
import com.irr310.server.Time;



public class I3dContext {

    private static final I3dContext instance = new I3dContext();
    private I3dCanvas canvas;
    private Graphics graphics;
    private List<Surface> surfaceList = new ArrayList<Surface>();
    private Font defaultFont;
    private TextureManager textureManager;
    private boolean preloaded;
    
    public static I3dContext getInstance() {
        return instance;
    }
    
    private I3dContext() {
        textureManager = new TextureManager();
        preloaded = false;
    }

    public void initCanvas(String title, int width, int height) {
        canvas = new I3dCanvas(this, title, width, height);
        graphics = new Graphics();
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
        for(Surface surface: surfaceList) {
            surface.update(absTime, gameTime);
        }
        
        canvas.draw();
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
                //Generate font cache
                defaultFont = I3dRessourceManager.getInstance().loadFont("default@fonts");
                
                
                //Generate shaders
                
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
    };
}
