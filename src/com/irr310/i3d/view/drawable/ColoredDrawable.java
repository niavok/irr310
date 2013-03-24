package com.irr310.i3d.view.drawable;


import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dRessourceManager;

public class ColoredDrawable extends Drawable {

    private String drawableName = null;
    private Drawable innerDrawable = null;
    private Color color = Color.pink;
    
    public ColoredDrawable() {
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public void setGraphics(Graphics g) {
        super.setGraphics(g);
        g.setColor(color);
    }
    
    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
        if(innerDrawable == null) {
            loadDrawable();
        }
        innerDrawable.setBounds(left, top, right, bottom);
    }

    private void loadDrawable() {
        if(innerDrawable == null) {
            innerDrawable = I3dRessourceManager.getInstance().loadDrawable(drawableName);
        }
    }

    @Override
    public void close() {
        innerDrawable.close();
    }

    @Override
    public int getIntrinsicWidth() {
        if(innerDrawable == null) {
            loadDrawable();
        }
        return innerDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        if(innerDrawable == null) {
            loadDrawable();
        }
        return innerDrawable.getIntrinsicHeight();
    }

    @Override
    public void draw() {
        innerDrawable.draw();
    }
    
    @Override
    public void begin(int glType) {
        innerDrawable.begin(glType);
    }
    
    @Override
    public void end() {
        innerDrawable.end();
    }
    
    @Override
    public void vertex(float x, float y) {
        innerDrawable.vertex(x, y);
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }

}
