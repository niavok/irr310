package com.irr310.client.graphics.ether.activities.worldmap;


import org.newdawn.slick.util.Log;

import com.irr310.common.world.Faction;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class SystemView extends View {

    private final WorldSystem system;

    public SystemView(WorldSystem system) {
        this.system = system;
        
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.FIXED);
        layoutParams.setWidthMeasure(new Measure(150, false));
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.FIXED);
        layoutParams.setHeightMeasure(new Measure(150, false));
        
        layoutParams.setMarginLeftMeasure(new Measure((float) (system.getLocation().x * 3.), false));
        layoutParams.setMarginTopMeasure(new Measure((float) (system.getLocation().y * 3.), false));
    }

    @Override
    public void onDraw(Graphics g) {
        Faction faction = system.getOwner();
        
        Color color = Color.grey;
        if(faction != null) {
            color = faction.getColor();
        }
            
        g.setColor(color);
        g.drawFilledRectangle(0, 0, 150, 150);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
       Log.debug("SystemView onLayout"); 
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
