package com.irr310.client.graphics.ether.activities.systemmap;



import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.View;

public class SystemDetailCircleView extends View {

    private final WorldSystemState system;
    private Color selectionColor;

    public SystemDetailCircleView(WorldSystemState system) {
        this.system = system;
        
        reshape();
        selectionColor = I3dRessourceManager.getInstance().loadColor("selection@color");
    }
    
    void reshape() {
        
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
        
        layoutParams.setMarginLeftMeasure(new Measure(20, false, Axis.HORIZONTAL));
        layoutParams.setMarginTopMeasure(new Measure(20, false, Axis.HORIZONTAL));
        layoutParams.setMarginRightMeasure(new Measure(20, false, Axis.HORIZONTAL));
        layoutParams.setMarginBottomMeasure(new Measure(20, false, Axis.HORIZONTAL));
    }

    @Override
    public void onDraw(Graphics g) {
        
        Color color = Color.grey;
        if(system.ownerId != -1) {
            color = system.ownerColor;
        }
        
        Color centerColor = color.setAlpha(0.03f);
        
        
        g.setColor(color);
        //g.drawFilledRectangle(0, 0, zoomedSize, zoomedSize);
        float size = Math.min(layoutParams.getWidth(), layoutParams.getHeight());
        float radius = size / 2f;
        if(getState() == ViewState.SELECTED) {
            g.drawRing(radius,radius, radius * 1.04f + 2f, radius, selectionColor, selectionColor, 64);
        }
        
        g.drawRing(radius,radius, radius, radius - 10f, color, color, 128);
        g.drawRing(radius,radius, radius - 10f, 0 , centerColor, centerColor, 64);
        
        if(system.homeSystem) {
        
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
