package com.irr310.client.graphics.ether.activities.systemmap;



import org.lwjgl.opengl.GL11;

import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.View;

public class SystemDetailCircleView extends RelativeLayout {

    private final WorldSystem system;
    private Color selectionColor;
    private float scale;
    private float offset;
    private SystemMapActivity activity;

    public SystemDetailCircleView(SystemMapActivity activity, WorldSystem system) {
        this.activity = activity;
        this.system = system;
        
        reshape();
        selectionColor = I3dRessourceManager.getInstance().loadColor("selection@color");

        reload();
        
    }
    
 private void reload() {
        
        removeAllView();
        
        for(Nexus nexus: system.getNexuses()) {
            addViewInLayout(new NexusView(nexus, this));
        }
        
        for(Ship ship: system.getShips()) {
            addViewInLayout(new ShipView(activity, ship, this));
        }
    }
    
    void reshape() {
        
        mLayoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        mLayoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
        
        mLayoutParams.setMarginLeftMeasure(new Measure(20, false, Axis.HORIZONTAL));
        mLayoutParams.setMarginTopMeasure(new Measure(20, false, Axis.HORIZONTAL));
        mLayoutParams.setMarginRightMeasure(new Measure(20, false, Axis.HORIZONTAL));
        mLayoutParams.setMarginBottomMeasure(new Measure(20, false, Axis.HORIZONTAL));
    }

    @Override
    public void onDraw(Graphics g) {
        
        Color color = Color.grey;
        if(system.getOwner() != null) {
            color = system.getOwner().getColor();
        }
        
        Color centerColor = Color.grey.setAlpha(0.03f);
        
        
        
        g.setColor(color);
        //g.drawFilledRectangle(0, 0, zoomedSize, zoomedSize);
        
        float size = Math.min(mLayoutParams.getContentWidth(), mLayoutParams.getContentHeight());
        float radius = size / 2f;
        
        if(getState() == ViewState.SELECTED) {
            g.drawRing(radius,radius, radius * 1.04f + 2f, radius, selectionColor, selectionColor, 64);
        }

        // Draw center disque
        g.drawRing(radius,radius, radius - 10f, 0 , centerColor, centerColor, 64);

        
        
        // Draw lines
        GL11.glBegin(GL11.GL_LINES);
        
        // Center line
        g.setColor(Color.grey.setAlpha(0.8f));
        GL11.glVertex2d(radius, 0);
        GL11.glVertex2d(radius, radius *2);
        
        
        
        for(double i = 0; i < system.getRadius() ; i+=1000) {
            
            if((((int) i) /1000) % 10 == 0) {
                g.setColor(Color.grey.setAlpha(0.6f));
            } else if((((int) i) /1000) % 5 == 0) {
                g.setColor(Color.grey.setAlpha(0.4f));
            } else {
                g.setColor(Color.grey.setAlpha(0.2f));
            }
            
            float x = (float) (i * scale);
            float limit = (float) (Math.sqrt(radius * radius - x*x)); 
            
            GL11.glVertex2d(radius + x , radius - limit);
            GL11.glVertex2d(radius + x, radius  + limit);
            
            GL11.glVertex2d(radius - x , radius - limit);
            GL11.glVertex2d(radius - x, radius  + limit);
            
            GL11.glVertex2d(radius - limit, radius + x);
            GL11.glVertex2d(radius  + limit, radius + x);
            
            GL11.glVertex2d(radius - limit, radius - x);
            GL11.glVertex2d(radius  + limit, radius - x);
                
        }
            
        
        GL11.glEnd();

        // Draw outer circle
        g.drawRing(radius,radius, radius, radius - 10f, color, color, 128);
        
        g.setColor(color);
        
        if(system.isHomeSystem()) {
        
            // Optimize allocation
            Point point1_1 = new Point(size/2, 0).rotate(Math.PI /45);
            Point point1_2 = new Point(size/2, 0).rotate(-Math.PI /45);
            Point point1_3 = new Point((size * 1.1f) / 2, 0);
            
            point1_1 = point1_1.rotate(Math.PI /4);
            point1_2 = point1_2.rotate(Math.PI /4);
            point1_3 = point1_3.rotate(Math.PI /4);
            g.drawTriangle(point1_1.x + radius, point1_1.y + radius, point1_2.x + radius, point1_2.y + radius, point1_3.x + radius, point1_3.y + radius, true);
            
            point1_1 = point1_1.rotate(Math.PI /2);
            point1_2 = point1_2.rotate(Math.PI /2);
            point1_3 = point1_3.rotate(Math.PI /2);
            g.drawTriangle(point1_1.x + radius, point1_1.y + radius, point1_2.x + radius, point1_2.y + radius, point1_3.x + radius, point1_3.y + radius, true);
            
            point1_1 = point1_1.rotate(Math.PI /2);
            point1_2 = point1_2.rotate(Math.PI /2);
            point1_3 = point1_3.rotate(Math.PI /2);
            g.drawTriangle(point1_1.x + radius, point1_1.y + radius, point1_2.x + radius, point1_2.y + radius, point1_3.x + radius, point1_3.y + radius, true);
            
            point1_1 = point1_1.rotate(Math.PI /2);
            point1_2 = point1_2.rotate(Math.PI /2);
            point1_3 = point1_3.rotate(Math.PI /2);
            g.drawTriangle(point1_1.x + radius, point1_1.y + radius, point1_2.x + radius, point1_2.y + radius, point1_3.x + radius, point1_3.y + radius, true);
        
        }
        
        super.onDraw(g);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        float size = Math.min(mLayoutParams.getContentWidth(), mLayoutParams.getContentHeight());
        offset = size/2;
        scale = (float) (size / (2*system.getRadius()));
        
        super.onLayout(l, t, r, b);
    }
    
    public float getScale() {
        return scale;
    }

    public float getOffset() {
        return offset;
    }
    
    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }
}
