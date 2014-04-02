package com.irr310.i3d;

import java.util.ArrayList;
import java.util.List;

import com.irr310.client.graphics.SettingsPopupActivity;
import com.irr310.client.graphics.SurfaceFactory;
import com.irr310.common.tools.Log;
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.fonts.FontFactory;
import com.irr310.i3d.scene.I3dSceneManager;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.drawable.BitmapFactory;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class I3dContext {

    private static final I3dContext instance = new I3dContext();
    private I3dCanvas canvas;
    private Graphics graphics;
    private List<Surface> surfaceList = new ArrayList<Surface>();
    private Font defaultFont;
    private boolean preloaded;
    private ContextListener listener;
    private I3dSceneManager sceneManager = new I3dSceneManager();
    private SettingsPopupActivity mPopupView;
    private Surface mPopupSurface;
    private V3DMouseEvent mLastMouseEvent;
    
    public static I3dContext getInstance() {
        return instance;
    }

    private I3dContext() {
        preloaded = false;
    }

    public void initCanvas(String title, int width, int height, String iconPath) {
        canvas = new I3dCanvas(this, title, width, height, iconPath);
        graphics = Graphics.getInstance();
        I3dRessourceManager.getInstance().setGraphics(graphics);

        
    }

    public void start() {
        canvas.init();
        
        mPopupSurface = new Surface(this) {
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                if(mouseEvent.getAction() == Action.MOUSE_PRESSED) {
                    Log.log("plop");
                }
                
                if(!super.onMouseEvent(mouseEvent) ) {
                    if(mouseEvent.getAction() == Action.MOUSE_PRESSED) {
                        mPopupSurface.unstackActivity();
                    }
                    return false;
                } else {
                    return true;
                }
                
            };
        };
        
        mPopupSurface.preferredWidth = 100;
        mPopupSurface.preferredHeight = 100;
        mPopupSurface.preferredXMode = Surface.LocationMode.RELATIVE;
        mPopupSurface.preferredYMode = Surface.LocationMode.RELATIVE;
        mPopupSurface.preferredWidthMode = Surface.LocationMode.RELATIVE;
        mPopupSurface.preferredHeightMode = Surface.LocationMode.RELATIVE;
        mPopupSurface.setBackgroundColor(Color.transparent);
        mPopupSurface.configure(canvas.getWidth(), canvas.getHeight());
        
        
        
        surfaceList.add(mPopupSurface);
    }

    public I3dCanvas getCanvas() {
        return canvas;
    }

    public void addSurface(Surface surface) {
        
        surfaceList.remove(mPopupSurface);
        surfaceList.add(surface);
        surfaceList.add(mPopupSurface);
        
        surface.configure(canvas.getWidth(), canvas.getHeight());
    }

    public void update(Timestamp time) {
        for (Surface surface : surfaceList) {
            surface.update(time);
        }

        graphics.setTimestamp(time);
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
        if(defaultFont ==null) {
            defaultFont = I3dRessourceManager.getInstance().loadFont("default@fonts");
        }
        return defaultFont;
    }

    public boolean isPreloaded() {
        return preloaded;
    }
    
    public V3DMouseEvent getLastMouseEvent() {
        return mLastMouseEvent;
    }

    public void onMouseEvent(V3DMouseEvent mouseEvent) {
        mLastMouseEvent = mouseEvent;
        for(int i = surfaceList.size() -1 ; i >= 0; i--) {
            Surface surface = surfaceList.get(i);
            if (surface.contains(mouseEvent.getX(), mouseEvent.getY()) ||mouseEvent.getAction() == Action.MOUSE_DRAGGED) {
                V3DMouseEvent topLeftEvent = new V3DMouseEvent(mouseEvent.getAction(), mouseEvent.getX() - surface.x, (surface.y + surface.height)
                        - mouseEvent.getY(), mouseEvent.getButton(), mouseEvent.getClickCount());
                topLeftEvent.setParentEvent(mouseEvent);
                
                if(surface.onMouseEvent(topLeftEvent)) {
                    break;
                }
            }
        }
    }

    public void onKeyEvent(V3DKeyEvent keyEvent) {
        for(int i = surfaceList.size() -1 ; i >= 0; i--) {
            Surface surface = surfaceList.get(i);
            if (surface.contains(keyEvent.getMouseX(), keyEvent.getMouseY())) {
                V3DKeyEvent topLeftEvent = new V3DKeyEvent(keyEvent.getAction(), keyEvent.getKeyCode(), keyEvent.getCharacter(), keyEvent.getMouseX() - surface.x, (surface.y + surface.height)
                        - keyEvent.getMouseY());

                if(surface.onKeyEvent(topLeftEvent)) {
                    break;
                }
            }
        }
    }

    public void onControllerEvent(V3DControllerEvent controllerEvent) {
        for (Surface surface : surfaceList) {
            if (surface.contains(controllerEvent.getMouseX(), controllerEvent.getMouseY())) {
                V3DControllerEvent topLeftEvent = new V3DControllerEvent(controllerEvent.getAction(), controllerEvent.getName(), controllerEvent.getIndex(), controllerEvent.getState(), controllerEvent.getMouseX() - surface.x, (surface.y + surface.height)
                        - controllerEvent.getMouseY());

                surface.onControllerEvent(topLeftEvent);
                break;
            }
        }
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

    public void clearCaches() {
        I3dRessourceManager.getInstance().clearCache();
        TextureManager.clearCache();
        FontFactory.clearCache();
        BitmapFactory.clearCache();
        defaultFont = null;
    }

    public void reloadUi() {
        for (Surface surface : surfaceList) {
            surface.reloadUi();
        }
    }
 
    public I3dSceneManager getSceneManager() {
        return sceneManager;
    }

    public void setPopUpActivity(Intent popupActivity, I3dVec2 popupPreferredPosition) {
        mPopupSurface.setPopupPreferredPosition(popupPreferredPosition);
        mPopupSurface.startActivity(popupActivity);
    }
    
    
}
