// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.gui;

import java.util.HashMap;
import java.util.Map;

import org.fenggui.Label;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import org.fenggui.util.fonttoolkit.FontFactory;

import fr.def.iss.vd2.lib_v3d.V3DColor;

/**
 * @author fberto
 */
public class V3DLabel extends V3DGuiComponent {

    Label label;
    private int xPos;
    private int yPos;
    private Color color;
    private int width;
    private boolean wordWrapping;

    public V3DLabel(String text) {
        label = new Label(text);
        label.setXY(0, 0);
        label.setExpandable(false);
        label.getAppearance().add(new PlainBackground(new Color(1.0f, 1.0f, 1.0f, 0.8f)));
        label.setShrinkable(true);
        color = Color.BLACK;
    }

    public void setColor(V3DColor foregroundColor, V3DColor backgroundColor) {
        label.getAppearance().removeAll();
        label.getAppearance().add(new PlainBackground(new Color(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a)));

        TextStyle style = label.getAppearance().getStyle(TextStyle.DEFAULTSTYLEKEY);
        TextStyleEntry textEntryStyle = new TextStyleEntry();
        color = new Color(foregroundColor.r, foregroundColor.g, foregroundColor.b, foregroundColor.a);
        textEntryStyle.setColor(color);
        style.addStyle(TextStyleEntry.DEFAULTSTYLESTATEKEY, textEntryStyle);

    }

    @Override
    public Label getFenGUIWidget() {
        return label;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        if (x < this.x || y < this.y) {
            return false;
        }

        if (x > this.x + label.getWidth() || y > this.y + label.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    public void repack() {
        label.updateMinSize();
        if (parent != null) {
            xPos = 0;
            yPos = 0;
            if (xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - label.getWidth() - x ;
            }

            if (yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y ;
            } else {
                yPos = parent.getHeight() - label.getHeight() - y ;
            }

            label.setXY(xPos, yPos);
        }
        if(!wordWrapping) {
            label.setSizeToMinSize();
        }
        label.layout();
    }

    public void setText(String text) {
        if(!label.getText().equals(text)) {
            label.setText(text);
            if (parent != null) {
                parent.repack();
            }
        }
    }

    public Point getSize() {
        Dimension dim = label.getSize();

        return new Point(dim.getWidth(), dim.getHeight());
    }

    public Point getComputedPosition() {
        Point position = label.getPosition();
        if (parent != null) {
            return new Point(xPos, yPos);
        } else {
            return position;
        }

    }

    public void setFontStyle(String font, String style, int size) {
        setFontStyle(new LabelStyle(font, style, size));
    }
    
   private void setFontStyle(LabelStyle labelStyle) {
        ImageFont font = V3DGui.getFont(labelStyle);

        

        setFontToDefaultStyle(label.getAppearance(), font, color);

    }

    private void setFontToDefaultStyle(TextAppearance appearance, IFont font, org.fenggui.util.Color color) {
        TextStyle def = appearance.getStyle(TextStyle.DEFAULTSTYLEKEY);
        def.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).setColor(color);

        ITextRenderer renderer = appearance.getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY).copy();
        renderer.setFont(font);
        appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);
    }

    public void setWordWarping(boolean wordWrapping, int width) {
        this.wordWrapping = wordWrapping;
        this.width = width;
        label.setSizeToMinSize();
        label.setSize(width, label.getY());
        System.err.println("getHeight: "+label.getTextRendererData().getSize().getHeight());
        label.setWordWarping(wordWrapping);
        System.err.println("getHeight: "+label.getTextRendererData().getSize().getHeight());
        label.setSize(width, label.getTextRendererData().getSize().getHeight());
    }

    

}
