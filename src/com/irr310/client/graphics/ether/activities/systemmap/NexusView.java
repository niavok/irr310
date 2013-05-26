package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.common.world.state.NexusState;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.View;

public class NexusView extends View {

    private NexusState nexus;
    private SystemDetailCircleView parentView;
    private float radius;

    public NexusView(NexusState nexus, SystemDetailCircleView parentView) {
        this.nexus = nexus;
        this.parentView = parentView;
        
    }

    
    private static final Color portalOuterColor = new Color(102, 10, 190);
    
    @Override
    public void onDraw(Graphics g) {
        
        
        g.setColor(Color.green);
        g.drawRing(0,0,radius,0,Color.mauve, portalOuterColor, 16);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        
        radius = (float) (nexus.radius * parentView.getScale()) + 10;
        
        getLayoutParams().mLeft = (float) (nexus.location.x * parentView.getScale() + parentView.getOffset() - radius);
        getLayoutParams().mTop = (float) (nexus.location.y * parentView.getScale() + parentView.getOffset()- radius);
        
    }

    @Override
    public void onMeasure() {
        layoutParams.mContentWidth = 20;
        layoutParams.mContentHeight = 20;
    }

    @Override
    public View duplicate() {
        return null;
    }
    
    

}
