package fr.def.iss.vd2.lib_v3d.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.fenggui.Widget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.TextureHandler;
import fr.def.iss.vd2.lib_v3d.element.TextureManager;

public class V3DGuiSprite extends V3DGuiComponent {

    private Widget widget;
    private int xPos = 0;
    private int yPos = 0;
    private TextureHandler texture;
    Dimension size = new Dimension(0, 0);

    public V3DGuiSprite(V3DContext context, String imagePath) throws IOException {
        this(context, TextureManager.LoadImage(imagePath));
    }

    public V3DGuiSprite(V3DContext context, BufferedImage image) {

        texture = new TextureHandler(context.getTextureManager(), image);
        widget = new Widget() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.drawImage(texture, 0, 0, size.getWidth(), size.getHeight());
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

    @Override
    public Widget getFenGUIWidget() {

        return widget;
    }

    @Override
    public boolean containsPoint(int i, int i0) {
        return false;
    }
    
    public void setSize(int x, int y) {
        size = new Dimension(x, y);
    }

    @Override
    public void repack() {
        if (parent != null) {
            xPos = 0;
            yPos = 0;
            if (xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - size.getWidth() - x;
            }

            if (yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - size.getHeight() - y;
            }

            widget.setXY(xPos, yPos);
        }

        widget.updateMinSize();
        widget.setSizeToMinSize();
        widget.layout();
    }
}
