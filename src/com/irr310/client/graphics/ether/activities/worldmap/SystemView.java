package com.irr310.client.graphics.ether.activities.worldmap;


import com.irr310.common.world.Faction;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.View;

public class SystemView extends View {

    private final WorldSystem system;
    private float size;
    private float zoom;
    private float zoomedSize;

    public SystemView(WorldSystem system) {
        this.system = system;
        
        size = 50;
        zoom = 1;
        reshape();
        
    }
    
    private void reshape() {
        
        zoomedSize = size * zoom;
        
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.FIXED);
        layoutParams.setWidthMeasure(new Measure(zoomedSize, false));
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.FIXED);
        layoutParams.setHeightMeasure(new Measure(zoomedSize, false));
        
        layoutParams.setMarginLeftMeasure(new Measure((float) (system.getLocation().x * zoom) - zoomedSize/2, false));
        layoutParams.setMarginTopMeasure(new Measure((float) (system.getLocation().y * zoom) - zoomedSize/2, false));
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
            
        g.setColor(color);
        g.drawFilledRectangle(0, 0, zoomedSize, zoomedSize);
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
}
