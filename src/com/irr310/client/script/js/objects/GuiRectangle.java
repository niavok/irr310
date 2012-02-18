package com.irr310.client.script.js.objects;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Dimension;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class GuiRectangle {

    private final Gui gui;
    private V3DGuiRectange component;

    public GuiRectangle(Gui gui) {
        this.gui = gui;
        component = new V3DGuiRectange();
    }
    public V3DGuiComponent getComponent() {
        return component;
    }
    
    public void setPosition(Vec2 position) {
        component.setPosition((int)position.getX(), (int) position.getY());
    }
    
    public void setSize(Vec2 size) {
        component.setSize((int)size.getX(), (int) size.getY());
    }
    
    public void setColor(Color color) {
        component.setColor(new V3DColor(color.r, color.g, color.b, color.a));
    }
    
    
    private class V3DGuiRectange extends V3DGuiComponent {

        private Widget widget;
        Dimension size = new Dimension(0, 0);
        V3DColor color = V3DColor.emerald;
        private int xPos = 0;
        private int yPos = 0;

        public V3DGuiRectange() {
            yAlignment = GuiYAlignment.BOTTOM;
            widget = new Widget() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(color.toColor());
                    g.setLineWidth(1);
                    g.drawWireRectangle(0, 0, size.getWidth(), size.getHeight());
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
                    return size;
                }
                
            };
        }
        
        public void setColor(V3DColor color) {
            this.color = color;
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
                    xPos = parent.getWidth() - widget.getWidth() - x;
                }

                if(yAlignment == GuiYAlignment.BOTTOM) {
                    yPos = y;
                } else {
                    yPos = parent.getHeight() - widget.getHeight() - y;
                }

                widget.setXY(xPos,  yPos);
            }
            
            widget.updateMinSize();
            widget.setSizeToMinSize();
            widget.layout();
        }
        
    }

    

}
