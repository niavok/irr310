package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.MeasurePoint;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class Button extends TextView {

    public Button(Graphics g) {
        super(g);
    }
    
    @Override
    public void onDraw() {
        
        float bevel = 10;
        float height = layoutParams.getHeight() + layoutParams.computeMesure(layoutParams.getLayoutPaddingTop()) + layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());
        float width = layoutParams.getWidth() + layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft()) + layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());
        float offsetX = - layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());
        float offsetY = - layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());
        
        Color color1 = new Color(239, 239, 239);
        Color color2 = new Color(214, 214, 214);
        Color borderColor = new Color(95,95,95);
        
        Color firstStepColor = Color.mix(color1, color2, bevel/height);
        Color secondStepColor = Color.mix(color1, color2, 1 - bevel/height);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(offsetX, offsetY, 0);
        
      
        
        g.setColor(Color.emerald);
        
        GL11.glBegin(GL11.GL_TRIANGLES);
        //Corner 1
        g.setColor(color1);
        GL11.glVertex2f(bevel, 0f);
        g.setColor(firstStepColor);
        GL11.glVertex2f(bevel, bevel);
        g.setColor(color1);
        GL11.glVertex2f(0f, bevel);
        
        //Corner 2
        g.setColor(secondStepColor);
        GL11.glVertex2f(width, height - bevel);
        g.setColor(secondStepColor);
        GL11.glVertex2f(width- bevel,  height  - bevel);
        g.setColor(color2);
        GL11.glVertex2f(width- bevel, height);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        // Top box
        g.setColor(color1);
        GL11.glVertex2f(bevel, 0f);
        GL11.glVertex2f(width, 0f);
        g.setColor(firstStepColor);
        GL11.glVertex2f(width, bevel);
        GL11.glVertex2f(bevel, bevel);
        
        // Bottom box
        g.setColor(secondStepColor);
        GL11.glVertex2f(0, height  - bevel);
        GL11.glVertex2f(width-bevel, height  - bevel);
        g.setColor(color2);
        GL11.glVertex2f(width-bevel, height);
        GL11.glVertex2f(0, height);
        
        //Middle box
        g.setColor(firstStepColor);
        GL11.glVertex2f(0, bevel);
        GL11.glVertex2f(width, bevel);
        g.setColor(secondStepColor);
        GL11.glVertex2f(width, height  - bevel);
        GL11.glVertex2f(0, height  - bevel);
        
        GL11.glEnd();
        
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        g.setColor(borderColor);
        GL11.glVertex2f(bevel, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height  - bevel);
        GL11.glVertex2f(width- bevel, height);
        GL11.glVertex2f(0, height);
        GL11.glVertex2f(0, bevel);
        
        GL11.glEnd();
        
        
        GL11.glPopMatrix();
        
        
        super.onDraw();
    }
    
    @Override
    public void onMeasure() {
        super.onMeasure();
    }
    
    @Override
    public View duplicate() {
        Button view = new Button(g);
        duplicateTo(view);
        return view;
    }
    
    @Override
    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        if(mouseEvent.getAction() == Action.MOUSE_CLICKED) {
            performClick();
            return true;
        }
        return false;
    }
}
