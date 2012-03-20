package fr.def.iss.vd2.lib_v3d.gui;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class V3DGuiCircle extends V3DGuiComponent {

    private Widget widget;
    Dimension size = new Dimension(0, 0);
    V3DColor borderColor = null;
    V3DColor fillColor = null;
    private int xPos = 0;
    private int yPos = 0;
    private int quality = 16;
    private float borderWidth = 2;

    public V3DGuiCircle() {

        widget = new Widget() {
            @Override
            public void paint(Graphics g) {
                if (fillColor != null) {
                    g.drawCircle(0,0,size.getWidth(), size.getHeight(), quality, fillColor.getFengguiColor(), fillColor.getFengguiColor());
                    
                }

                if (borderColor != null) {
                    g.setLineWidth(borderWidth);
                    g.drawCircleBorder(0,0,size.getWidth(), size.getHeight(), quality, borderColor.getFengguiColor());
                }

            }

            
//            private void drawExternCircle() {
//
//                float step = 2f * (float) Math.PI / (float) quality;
//
//                if (customColor) {
//                    GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
//                    GL11.glEnable(GL11.GL_ALPHA_TEST);
//                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                }
//
//                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
//
//                for (int i = 0; i <= quality; i++) {
//                    if (customColor) {
//                        GL11.glColor4f(innerColor.r, innerColor.g, innerColor.b, innerColor.a);
//                    }
//                    GL11.glVertex3f((float) (innerRadius * Math.cos(step * i)), (float) (innerRadius * Math.sin(step * i)), 0f);
//
//                    if (customColor) {
//                        GL11.glColor4f(outerColor.r, outerColor.g, outerColor.b, outerColor.a);
//                    }
//                    GL11.glVertex3f((float) (radius * Math.cos(step * i)), (float) (radius * Math.sin(step * i)), 0f);
//                }
//
//                GL11.glEnd();
//
//                if (customColor) {
//                    GL11.glDisable(GL11.GL_ALPHA_TEST);
//                    GL11.glPopAttrib();
//                }
//
//            }
//
//            private void drawSolidCircle() {
//                float step = 2f * (float) Math.PI / (float) quality;
//
//                GL11.glBegin(GL11.GL_LINE_LOOP);
//
//                for (int i = 0; i <= quality; i++) {
//                    GL11.glVertex3f((float) (radius * Math.cos(step * i)), (float) (radius * Math.sin(step * i)), 0f);
//                }
//
//                GL11.glEnd();
//
//                GL11.glBegin(GL11.GL_POINTS);
//
//                for (int i = 0; i <= quality; i++) {
//                    GL11.glVertex3f((float) (radius * Math.cos(step * i)), (float) (radius * Math.sin(step * i)), 0f);
//                }
//
//                GL11.glEnd();
//
//            }

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
    
    @Override
    public Dimension getSize() {
        return size;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    public int getQuality() {
        return quality;
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
        if (parent != null) {
            xPos = 0;
            yPos = 0;
            if (xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - 1 - x;
            }

            if (yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - 1 - y;
            }

            widget.setXY(xPos, yPos);
        }

        widget.updateMinSize();
        widget.setSizeToMinSize();
        widget.layout();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

}
