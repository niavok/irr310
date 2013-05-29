package com.irr310.client.graphics.ether.activities.systemmap;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.state.ComponentState;
import com.irr310.common.world.state.NexusState;
import com.irr310.common.world.state.PartState;
import com.irr310.common.world.state.ShipState;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class ShipView extends View {

    private static final double TARGET_SIZE = 20;
    private SystemDetailCircleView parentView;
    private ShipState ship;
    private boolean selected = false;
    private Color selectionColor;
    
    
    public ShipView(ShipState ship, SystemDetailCircleView parentView) {
        this.ship = ship;
        this.parentView = parentView;
        selectionColor = I3dRessourceManager.getInstance().loadColor("selection@color");
        
        
        setOnClickListener(new OnClickListener() {
            

            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                selected = true;
            }
        });
        
    }

    @Override
    public void onDraw(Graphics g) {
        
        // First find ship bounding box to find optimal display scale
        
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        
        for(ComponentState component : ship.components) {
        
            for(PartState part : component.parts) {
                Vec3 absoluteCorner1 = new Vec3(part.shape.x/2,part.shape.y/2,0).transform(part.transform);
                Vec3 absoluteCorner2 = new Vec3(-part.shape.x/2,part.shape.y/2,0).transform(part.transform);
                Vec3 absoluteCorner3 = new Vec3(-part.shape.x/2,-part.shape.y/2,0).transform(part.transform);
                Vec3 absoluteCorner4 = new Vec3(part.shape.x/2,-part.shape.y/2,0).transform(part.transform);
                
                minX = Math.min(absoluteCorner1.x, minX);
                minX = Math.min(absoluteCorner2.x, minX);
                minX = Math.min(absoluteCorner3.x, minX);
                minX = Math.min(absoluteCorner4.x, minX);
                
                minY = Math.min(absoluteCorner1.y, minY);
                minY = Math.min(absoluteCorner2.y, minY);
                minY = Math.min(absoluteCorner3.y, minY);
                minY = Math.min(absoluteCorner4.y, minY);
                
                maxX = Math.max(absoluteCorner1.x, maxX);
                maxX = Math.max(absoluteCorner2.x, maxX);
                maxX = Math.max(absoluteCorner3.x, maxX);
                maxX = Math.max(absoluteCorner4.x, maxX);
                
                maxY = Math.max(absoluteCorner1.y, maxY);
                maxY = Math.max(absoluteCorner2.y, maxY);
                maxY = Math.max(absoluteCorner3.y, maxY);
                maxY = Math.max(absoluteCorner4.y, maxY);
            }
        }
        
        Vec3 baseLocation = new Vec3((maxX + minX) /2 ,(maxY + minY) /2 ,0 );
        
        double width = maxX -minX;
        double height = maxY- minY;
        double scale = TARGET_SIZE / Math.max(width, height) ;
        
        
        //
        getLayoutParams().mLeft = (float) ((baseLocation.x) * parentView.getScale() + parentView.getOffset() - (scale*width)/2);
        getLayoutParams().mTop = (float) ((baseLocation.y) * parentView.getScale() + parentView.getOffset() - (scale*height)/2);
        getLayoutParams().mRight = (float) (getLayoutParams().mLeft + scale*width);  
        getLayoutParams().mBottom = (float) (getLayoutParams().mTop  + scale*height);
        
//        g.setColor(Color.pink);
//        g.drawFilledRectangle(0,0, (float)(width * scale),(float)(height * scale));
        
        
//        g.setColor(Color.red);
//        g.drawFilledRectangle(0,0, (float)(1 * scale),(float)(1 * scale));
//        g.drawFilledRectangle((float)(2 * scale),0, (float)(1 * scale),(float)(1 * scale));
//        g.drawFilledRectangle((float)(1 * scale),(float)(1 * scale), (float)(1 * scale),(float)(1 * scale));
//        g.drawFilledRectangle((float)(1 * scale),(float)(3 * scale), (float)(1 * scale),(float)(1 * scale));

//        g.setColor(Color.green);
//        g.drawFilledRectangle((float)((minX - baseLocation.x) * scale-5. + TARGET_SIZE/2),(float)((minY - baseLocation.y) * scale-5.+ TARGET_SIZE/2),10, 10);
//        g.setColor(Color.blue);
//        g.drawFilledRectangle((float)((maxX - baseLocation.x) * scale-5.+ TARGET_SIZE/2),(float)((maxY - baseLocation.y) * scale-5.+ TARGET_SIZE/2),10, 10);
//        
        
        if(selected) {
            g.drawRing((float) (scale*width)/2, (float) (scale*height)/2,(float) TARGET_SIZE/2,0,selectionColor, selectionColor, 16);
        }
        
        
        g.setColor(ship.owner.color);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        for(ComponentState component : ship.components) {
            
            for(PartState part : component.parts) {
                Vec3 absoluteCorner1 = new Vec3(part.shape.x/2,part.shape.y/2,0).transform(part.transform).minus(baseLocation);
                Vec3 absoluteCorner2 = new Vec3(-part.shape.x/2,part.shape.y/2,0).transform(part.transform).minus(baseLocation);
                Vec3 absoluteCorner3 = new Vec3(-part.shape.x/2,-part.shape.y/2,0).transform(part.transform).minus(baseLocation);
                Vec3 absoluteCorner4 = new Vec3(part.shape.x/2,-part.shape.y/2,0).transform(part.transform).minus(baseLocation);
                
                GL11.glVertex2d((absoluteCorner1.x + width/2) * scale, (absoluteCorner1.y + height/2) * scale);
                GL11.glVertex2d((absoluteCorner2.x + width/2) * scale, (absoluteCorner2.y + height/2) * scale);
                GL11.glVertex2d((absoluteCorner3.x + width/2) * scale, (absoluteCorner3.y + height/2) * scale);
                GL11.glVertex2d((absoluteCorner4.x + width/2) * scale, (absoluteCorner4.y + height/2) * scale);
            }
        }
        
        GL11.glEnd(); 
//        
//        
//        boolean first = true;
//        
//        ComponentState componentState = ship.components.get(0);
//        PartState firstPart = componentState.parts.get(0);
//        baseLocation = firstPart.transform.getTranslation();
//        
//        
//        g.drawRing((float) (baseLocation.x * parentView.getScale()), (float) (baseLocation.y * parentView.getScale()  - 0), 20, 19, Color.red, Color.red, 32);
//        
//        
//        for(ComponentState component : ship.components) {
//            for(PartState part : component.parts) {
//                
//                
//                
//                
//                Vec3 location = part.transform.getTranslation().minus(baseLocation).multiply(200);
//                
//                if(first) {
//                    g.drawRing((float) (location.x * parentView.getScale()  ),
//(float) (location.y * parentView.getScale()  ), 20, 19, Color.fushia, Color.fushia, 32);
//                    first = false;
//                }
//                    
//                
//                g.drawFilledRectangle((float) (location.x * parentView.getScale()  ),
//                                      (float) (location.y * parentView.getScale()  )
//                                      , (float) part.shape.x *10, (float) part.shape.y*10);
//            }
//        }
//        
        
        
        
        
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        
        ComponentState componentState = ship.components.get(0);
        PartState firstPart = componentState.parts.get(0);
        Vec3 baseLocation = firstPart.transform.getTranslation();
        
        
//        getLayoutParams().mLeft = (float) (baseLocation.x * parentView.getScale() + parentView.getOffset() - 0);
//        getLayoutParams().mTop = (float) (baseLocation.y * parentView.getScale() + parentView.getOffset() - 0);
        
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
