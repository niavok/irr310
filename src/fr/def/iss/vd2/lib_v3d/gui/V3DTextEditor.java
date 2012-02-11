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

import org.fenggui.TextEditor;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DTextEditor extends V3DGuiComponent {

    TextEditor editor;
    private int xPos;
    private int yPos;
    

    public V3DTextEditor(V3DContext context, String initialText) {
        editor = new TextEditor();
        editor.setText(initialText);
        editor.setXY(0, 0);
        editor.setExpandable(false);
        editor.getAppearance().add(new PlainBackground(new Color(1.0f,1.0f,1.0f,0.8f)));
        editor.setShrinkable(true);
        
    }

    public void setColor(V3DColor foregroundColor, V3DColor backgroundColor) {
        editor.getAppearance().removeAll();
        editor.getAppearance().add(new PlainBackground(new Color(backgroundColor.r,backgroundColor.g,backgroundColor.b,backgroundColor.a)));

        TextStyle style = editor.getAppearance().getStyle(TextStyle.DEFAULTSTYLEKEY);
        TextStyleEntry  textEntryStyle = new TextStyleEntry();
        textEntryStyle.setColor(new Color(foregroundColor.r,foregroundColor.g,foregroundColor.b,foregroundColor.a));
        style.addStyle(TextStyleEntry.DEFAULTSTYLESTATEKEY, textEntryStyle);

    }

    @Override
    public TextEditor getFenGUIWidget() {
        return editor;
    }

    @Override
    public boolean containsPoint(int x, int y) {
         if(x < this.x || y < this.y) {
            return false;
        }

        if(x > this.x + editor.getWidth() || y > this.y + editor.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    public void repack() {
        
        if(parent != null) {
            xPos = 0;
            yPos = 0;
            if(xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - editor.getWidth() - x;
            }

            if(yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - editor.getHeight() - y;
            }

            editor.setXY(xPos,  yPos);
        }
        editor.updateMinSize();
        editor.setSizeToMinSize();
        editor.layout();
    }

    public void setText(String text) {
        editor.setText(text);
        if(parent != null) {
            parent.repack();
        }
    }

    public String getText() {
        return editor.getText();
    }

    public Point getSize() {
        Dimension dim = editor.getSize();

        return new Point(dim.getWidth(), dim.getHeight());
    }

    public Point getComputedPosition() {
        Point position = editor.getPosition();
        if(parent != null) {
            return new Point(xPos, yPos);
        }else {
            return position;
        }
        
    }

}
