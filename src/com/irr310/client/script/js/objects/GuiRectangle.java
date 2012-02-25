package com.irr310.client.script.js.objects;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Dimension;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class GuiRectangle implements GuiComponent {

    private final Gui gui;
    private V3DGuiRectangle component;

    public GuiRectangle(Gui gui) {
        this.gui = gui;
        component = new V3DGuiRectangle();
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
    
    public void setBorderColor(Color color) {
        component.setBorderColor(new V3DColor(color.r, color.g, color.b, color.a));
    }
    
    public void setFillColor(Color color) {
        component.setFillColor(new V3DColor(color.r, color.g, color.b, color.a));
    }
    
    private class V3DGuiRectangle extends V3DGuiComponent {

        private Widget widget;
        Dimension size = new Dimension(0, 0);
        V3DColor borderColor = null;
        V3DColor fillColor = null;
        private int xPos = 0;
        private int yPos = 0;

        public V3DGuiRectangle() {
            yAlignment = GuiYAlignment.BOTTOM;
            widget = new Widget() {
                @Override
                public void paint(Graphics g) {
                    if(fillColor != null) {
                        g.setColor(fillColor.toColor());
                        g.setLineWidth(2f);
                        g.drawFilledRectangle(5, 5, size.getWidth(), size.getHeight());
                    }
                    
                    if(borderColor != null) {
                        g.setColor(borderColor.toColor());
                        g.drawWireRectangle(5, 5, size.getWidth(), size.getHeight());
                    }
                    
                    
                }

                @Override
                public int getX() {
                    return xPos-5;
                }

                @Override
                public int getY() {
                    return yPos-5;
                }
                
                @Override
                public Dimension getSize() {
                    return new Dimension(size.getWidth()+10, size.getHeight()+10);
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
