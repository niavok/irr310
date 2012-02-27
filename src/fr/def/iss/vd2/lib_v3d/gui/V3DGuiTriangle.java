package fr.def.iss.vd2.lib_v3d.gui;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Dimension;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class V3DGuiTriangle extends V3DGuiComponent {

    private Widget widget;
    Dimension size = new Dimension(0, 0);
    V3DColor borderColor = null;
    V3DColor fillColor = null;
    private int xPos = 0;
    private int yPos = 0;
    private float borderWidth = 2;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int x3;
    private int y3;

    
    public V3DGuiTriangle() {
        
        widget = new Widget() {
            @Override
            public void paint(Graphics g) {
                if(fillColor != null) {
                    g.setColor(fillColor.toColor());
                    g.setLineWidth(borderWidth);
                    g.drawTriangle(x1, y1, x2, y2, x3, y3, true);
                }
                
                if(borderColor != null) {
                    g.setColor(borderColor.toColor());
                    g.drawTriangle(x1, y1, x2, y2, x3, y3, false);
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
    
    public void setPoint(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        size = new Dimension(Math.max(Math.max(x1, x2), x3) - Math.min(Math.min(x1, x2), x3), Math.max(Math.max(y1, y2), y3)
                - Math.min(Math.min(y1, y2), y3));
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