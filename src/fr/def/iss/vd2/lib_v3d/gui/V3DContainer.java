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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fenggui.Container;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.layout.StaticLayout;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiXAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;

/**
 * @author fberto
 */
public class V3DContainer extends V3DGuiComponent implements V3DLocalisable {

    Container container;
    private int xPos;
    private int yPos;
    private int width = 0;
    private int height = 0;
    private List<V3DGuiComponent> guiComponentList = new CopyOnWriteArrayList<V3DGuiComponent>();

    public V3DContainer() {
        container = new Container();
        container.setXY(0, 0);
        container.setExpandable(false);
        //container.getAppearance().add(new PlainBackground(new Color(1.0f, 1.0f, 1.0f, 0.8f)));
        container.setShrinkable(true);

        //PlainBorder border = new PlainBorder(new Color(150, 150, 150), 1);
        //container.getAppearance().add(border);
        container.pack();
        container.setLayoutManager(new StaticLayout());
        //container.getAppearance().add(new PlainBackground(new Color(0.5f, 0.6f, 0.9f, 0.8f)));
        
        
    }

    public void setSize(int width, int height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.height = height;
            repack();
        }
    }

    public void setColor(V3DColor backgroundColor) {
        container.getAppearance().removeAll();
        container.getAppearance().add(new PlainBackground(new Color(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a)));
    }

    public void add(V3DGuiComponent component) {
        guiComponentList.add(component);
        container.addWidget(component.getFenGUIWidget());
        component.setParent(this);
        component.repack();
    }

    @Override
    public Container getFenGUIWidget() {
        return container;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        if (x < this.x || y < this.y) {
            return false;
        }

        if (x > this.x + container.getWidth() || y > this.y + container.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    public void repack() {

        container.setSize(width, height);
        
        //container.updateMinSize();
        if (parent != null) {
            xPos = 0;
            yPos = 0;
            if (xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - container.getWidth() - x;
            }

            if (yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - container.getHeight() - y;
            }

            container.setXY(xPos, yPos);
        }
        
        
        
        //container.setSizeToMinSize();
        if(width < 1000) {
        //System.err.println("width"+width);
        //System.err.println("height"+height);
        //System.err.println("xPos"+xPos);
        //System.err.println("yPos"+yPos);
        
        }
        
        //container.setXY(0, 0);

        for (V3DGuiComponent component : guiComponentList) {
            component.repack();
        }

        container.layout();
    }

    public Point getSize() {
        Dimension dim = container.getSize();

        return new Point(dim.getWidth(), dim.getHeight());
    }

    public Point getComputedPosition() {
        Point position = container.getPosition();
        if (parent != null) {
            return new Point(xPos, yPos);
        } else {
            return position;
        }

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void remove(V3DGuiComponent component) {
        if (guiComponentList.contains(component)) {
            guiComponentList.remove(component);
            container.removeWidget(component.getFenGUIWidget());
        }
    }

    @Override
    public int getAbsoluteX() {
        return xPos;
    }

    @Override
    public int getAbsoluteY() {
        return yPos;
    }
    
    public void removeAll() {
        for (V3DGuiComponent component : guiComponentList) {
            remove(component);
        }
    }

}
