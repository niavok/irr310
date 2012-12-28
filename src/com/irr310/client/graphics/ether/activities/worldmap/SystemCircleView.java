package com.irr310.client.graphics.ether.activities.worldmap;



import org.lwjgl.opengl.GL11;

import com.irr310.common.world.Faction;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.View;

public class SystemCircleView extends View {

    private final WorldSystem system;
    private float size;
    private float zoom;
    private float zoomedSize;
    private boolean selected;
    private Color selectionColor;

    public SystemCircleView(WorldSystem system) {
        this.system = system;
        
        size = 30;
        zoom = 1;
        reshape();
        selected = false;
        selectionColor = I3dRessourceManager.getInstance().loadColor("selection@color");
    }
    
    void reshape() {
        
        zoomedSize = size * zoom;
        
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.FIXED);
        layoutParams.setWidthMeasure(new Measure(zoomedSize, false));
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.FIXED);
        layoutParams.setHeightMeasure(new Measure(zoomedSize, false));
        
//        layoutParams.setMarginLeftMeasure(new Measure((float) (system.getLocation().x * zoom) - zoomedSize/2, false));
//        layoutParams.setMarginTopMeasure(new Measure((float) (system.getLocation().y * zoom) - zoomedSize/2, false));
    }
    
    
    public void setZoom(float zoom) {
        this.zoom = zoom;
        reshape();
        if(getParent() != null) {
            getParent().requestLayout();
        }
    }
    

    @Override
    public void onDraw(Graphics g) {
        Faction faction = system.getOwner();
        
        Color color = Color.grey;
        if(faction != null) {
            color = faction.getColor();
        }
        
        Color centerColor = color.copy().setAlpha(0.2f);
        
        
        g.setColor(color);
        //g.drawFilledRectangle(0, 0, zoomedSize, zoomedSize);
        
        float radius = zoomedSize / 2f;
        if(selected) {
            g.drawRing(radius,radius, radius * 1.04f + 2f, radius, selectionColor, selectionColor, 64);
        }
        
        g.drawRing(radius,radius, radius, radius * 0.90f - 3f, color, color, 64);
        g.drawRing(radius,radius, radius * 0.90f - 3f, 0 , centerColor, centerColor, 64);
        
        if(system.isHomeSystem()) {
        
            // Optimize allocation
            Point point1_1 = new Point(zoomedSize/2, 0).rotate(Math.PI /15);
            Point point1_2 = new Point(zoomedSize/2, 0).rotate(-Math.PI /15);
            Point point1_3 = new Point((zoomedSize * 1.3f + 5) / 2, 0);
            
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
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
    }

    @Override
    public void onMeasure() {
    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
