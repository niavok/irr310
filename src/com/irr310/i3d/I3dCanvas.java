package com.irr310.i3d;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.irr310.common.Game;
import com.irr310.common.event.QuitGameEvent;

import fr.def.iss.vd2.lib_v3d.element.TextureManager;

public class I3dCanvas {

    private I3dContext context;
    private int width;
    private int height;
    private final String title;
    private JFrame frame;
    private TextureManager textureManager = new TextureManager();
    
    private boolean polygonOffset = false;

    public I3dCanvas(final I3dContext context,String title, int width, int height) {
        this.context = context;
        this.title = title;
        this.width = width;
        this.height = height;
    }
    
    public void init() {
        try{
            
            frame = new JFrame(title);
            //frame.setSize(width,height);
            //frame.setUndecorated(true);  //here
            //frame.setAlwaysOnTop(true);
            frame.setLocation(0, 0);
            Canvas canvas = new Canvas();
            canvas.setMinimumSize(new Dimension(800, 600));
            canvas.setPreferredSize(new Dimension(width, height));
            frame.add(canvas);
            frame.pack();
            frame.addWindowListener(generateWindowListener());
            
            frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
                
                @Override
                public void ancestorMoved(HierarchyEvent e) {
                }
                @Override
                public void ancestorResized(HierarchyEvent e) {
                    width = frame.getContentPane().getWidth();
                    height = frame.getContentPane().getHeight();
                    reshape(0,0,width, height);
                }          
            });
            
            Display.setDisplayMode(new DisplayMode(width, height));
            //Display.setFullscreen(true);
            Display.setVSyncEnabled(false);
            Display.setTitle("Irr310");
            Display.setParent(canvas);
            Display.create();
            canvas.requestFocus();
        }catch(Exception e){
            System.out.println("Error setting up display" + e);
            System.exit(0);
        }

        // Enable z- (depth) buffer for hidden surface removal.
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);

        if (polygonOffset) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, 1.0f);
        }
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glClearDepth(1.0); // Enables Clearing Of The Depth Buffer
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);

        // Enable smooth shading.
        GL11.glShadeModel(GL11.GL_SMOOTH);

        // Define "clear" color.
        GL11.glClearColor(0f, 0f, 0.4f, 0f);

        // We want a nice perspective.
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glFlush();

        getTextureManager().clearCache();
        //initied = true;
        
        new LWJGLBinding();
        
    }
    
    private void reshape(int x, int y, int width, int height) {
        for (Surface surface : context.getSurfaceList()) {
            surface.configure(width, height);
        }
    }
    
    
    private WindowListener generateWindowListener() {
        return new WindowListener() {
            
            @Override
            public void windowOpened(WindowEvent e) {
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO put this out of here
                Game.getInstance().sendToAll(new QuitGameEvent());
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
                
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
            }
        };
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public TextureManager getTextureManager() {
        return textureManager;
    }

    
    public boolean draw() {
        if(Display.isCloseRequested()) {
            return false;
        }
        
        display();
        Display.update();
        return true;
    }

    public void display() {

        GL11.glClear(GL11.GL_ACCUM_BUFFER_BIT);

        for (Surface surface: context.getSurfaceList()) {
            surface.draw();
        }
        GL11.glFlush();
    }

    public void show() {
        frame.setVisible(true);        
    }

    public void destroy() {
        Display.destroy();
        frame.dispose();
    }
    
}
