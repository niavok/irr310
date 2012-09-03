package com.irr310.i3d;

import org.lwjgl.opengl.GL11;

public class Graphics {


    public Graphics() {
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
    public void drawFilledRectangle(int x, int y, int width, int height) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
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
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, boolean filled) {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(x3, y3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
    }
}
