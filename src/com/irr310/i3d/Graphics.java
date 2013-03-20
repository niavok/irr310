package com.irr310.i3d;

import org.lwjgl.opengl.GL11;

public class Graphics {

    private static Graphics instance = new Graphics();

    public static Graphics getInstance() {
        return instance;
    }

    private Graphics() {
    }

    /**
     * Sets the current pen colour, with varying opacity.
     * 
     * @param red The red component of the colour
     * @param green The green component of the colour
     * @param blue The blue component of the colour
     * @param alpha The level of opacity
     */
    public void setColor(Color color) {
        GL11.glColor4f(color.r, color.g, color.b, color.a);
    }

    /**
     * Draws a filled rectangle using the current colour.
     * 
     * @param x The left hand side of the rectangle
     * @param y The top of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     */
    public void drawFilledRectangle(float x, float y, float width, float height) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
    }
    
    public void drawLine(int x1, int y1, int x2, int y2) {
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }
    
    /**
     * Draws a Triangle.
     * 
     * @param x1 the x value of the first point
     * @param y1 the y value of the first point
     * @param x2 the x value of the second point
     * @param y2 the y value of the second point
     * @param x3 the x value of the third point
     * @param y3 the y value of the third point
     * @param filled to fill the triangle or not
     */
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, boolean filled) {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(x3, y3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
    }

    public void drawRing(float x, float y, float radius, float innerRadius, Color innerColor, Color outerColor, int quality) {

        float step = 2f * (float) Math.PI / (float) quality;

        boolean customColor = (innerColor != null && outerColor != null);

        if (customColor) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
            // GL11.glEnable(GL11.GL_ALPHA_TEST);
            // GL11.glBlendFunc (GL11.GL_SRC_ALPHA,
            // GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        for (int i = 0; i <= quality; i++) {
            if (customColor) {
                GL11.glColor4f(innerColor.r, innerColor.g, innerColor.b, innerColor.a);
            }
            GL11.glVertex3f((float) (innerRadius * Math.cos(step * i)) + x, (float) (innerRadius * Math.sin(step * i)) + y, 0f);

            if (customColor) {
                GL11.glColor4f(outerColor.r, outerColor.g, outerColor.b, outerColor.a);
            }
            GL11.glVertex3f((float) (radius * Math.cos(step * i)) + x, (float) (radius * Math.sin(step * i)) + y, 0f);
        }

        GL11.glEnd();

        if (customColor) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopAttrib();
        }

    }

}
