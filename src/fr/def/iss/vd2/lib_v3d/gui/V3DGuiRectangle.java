package fr.def.iss.vd2.lib_v3d.gui;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Dimension;
import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class V3DGuiRectangle extends V3DGuiComponent {

    private Widget widget;
    Dimension size = new Dimension(0, 0);
    V3DColor borderColor = null;
    V3DColor fillColor = null;
    private int xPos = 0;
    private int yPos = 0;
    private float borderWidth = 2;

    public V3DGuiRectangle() {
        
        widget = new Widget() {
            @Override
            public void paint(Graphics g) {
                if(fillColor != null) {
                    g.setColor(fillColor.toColor());
                    g.drawFilledRectangle(0, 0, size.getWidth(), size.getHeight());
                }
                
                if(borderColor != null) {
                    g.setColor(borderColor.toColor());
                    g.drawWireRectangle2(0,0, size.getWidth(), size.getHeight(),borderWidth);
                }
                
                
            }

            @Override
            public int getX() {
                return xPos;
            }

            @Override
            public int getY() {
                return yPos;
            }
            
            @Override
            public Dimension getSize() {
                return new Dimension(size.getWidth(), size.getHeight());
            }
            
        };
    }
    
    public void setBorderColor(V3DColor color) {
        this.borderColor = color;
    }
    
    public void setFillColor(V3DColor color) {
        this.fillColor = color;
    }

    public void setSize(int x, int y) {
        size = new Dimension(x, y);
    }

    @Override
    public Widget getFenGUIWidget() {
        
        return widget;
    }

    @Override
    public boolean containsPoint(int i, int i0) {
        return false;
    }

    @Override
    public void repack() {
        if(parent != null) {
            xPos = 0;
            yPos = 0;
            if(xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - size.getWidth() - x;
            }

            if(yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - size.getHeight() - y;
            }

            widget.setXY(xPos,  yPos);
        }
        
        
        
        widget.updateMinSize();
        widget.setSizeToMinSize();
        widget.layout();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }
    
}