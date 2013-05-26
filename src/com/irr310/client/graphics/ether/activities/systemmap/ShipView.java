package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.state.ComponentState;
import com.irr310.common.world.state.NexusState;
import com.irr310.common.world.state.PartState;
import com.irr310.common.world.state.ShipState;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.View;

public class ShipView extends View {

    private SystemDetailCircleView parentView;
    private ShipState ship;
    private Vec3 baseLocation;

    public ShipView(ShipState ship, SystemDetailCircleView parentView) {
        this.ship = ship;
        this.parentView = parentView;
        
    }

    @Override
    public void onDraw(Graphics g) {
        
        
        
       
        g.setColor(ship.owner.color);
        
        boolean first = true;
        
        ComponentState componentState = ship.components.get(0);
        PartState firstPart = componentState.parts.get(0);
        baseLocation = firstPart.transform.getTranslation();
        
        getLayoutParams().mLeft = (float) (baseLocation.x * parentView.getScale() + parentView.getOffset() - 0);
        getLayoutParams().mTop = (float) (baseLocation.y * parentView.getScale() + parentView.getOffset() - 0);
        
        g.drawRing((float) (baseLocation.x * parentView.getScale()), (float) (baseLocation.y * parentView.getScale()  - 0), 20, 19, Color.red, Color.red, 32);
        
        
        for(ComponentState component : ship.components) {
            for(PartState part : component.parts) {
                
                
                
                
                Vec3 location = part.transform.getTranslation().minus(baseLocation).multiply(1000);
                
                if(first) {
                    g.drawRing((float) (location.x * parentView.getScale()  ),
(float) (location.y * parentView.getScale()  ), 20, 19, Color.fushia, Color.fushia, 32);
                    first = false;
                }
                    
                
                g.drawFilledRectangle((float) (location.x * parentView.getScale()  ),
                                      (float) (location.y * parentView.getScale()  )
                                      , 5,5);
            }
        }
        
        
        
        
        
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        
        ComponentState componentState = ship.components.get(0);
        PartState firstPart = componentState.parts.get(0);
        baseLocation = firstPart.transform.getTranslation();
        
        
        getLayoutParams().mLeft = (float) (baseLocation.x * parentView.getScale() + parentView.getOffset() - 0);
        getLayoutParams().mTop = (float) (baseLocation.y * parentView.getScale() + parentView.getOffset() - 0);
        
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
