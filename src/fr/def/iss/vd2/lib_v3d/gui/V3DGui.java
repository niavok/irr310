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

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.binding.render.jogl.EventBinding;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.layout.StaticLayout;
import org.fenggui.util.Color;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;

/**
 *
 * @author fberto
 */
public class V3DGui implements V3DLocalisable{

    private Display display = null;
    List<V3DGuiComponent> guiComponentList = new ArrayList<V3DGuiComponent>();
    boolean generated = false;
    private V3DCameraBinding parentBinding;
    Container rootContainer;
    EventBinding eventBinding;
    private int y;
    private int x;
    private int width;
    private int height;
    

    public V3DGui(V3DCameraBinding parent) {
        generated = false;
        parentBinding = parent;
    }

    public void generate() {
        generated = false;
    }

    public void doGenerate() {

            if(display == null) {
                display = FengGUI.createWidget(Display.class);
                // Repair
                //eventBinding = new EventBinding(canvas, display);
            }else {
                display.removeAllWidgets();
            }
            

            rootContainer = FengGUI.createWidget(Container.class);
          
            for (V3DGuiComponent component : guiComponentList) {
                rootContainer.addWidget(component.getFenGUIWidget());
                component.setParent(this);
            }

            PlainBorder border = new PlainBorder(new Color(150,150, 150),1);
            rootContainer.getAppearance().add(border);

            display.addWidget(rootContainer);
            rootContainer.pack();
            rootContainer.setLayoutManager(new StaticLayout());
            rootContainer.layout();

            generated = true;

    }

    public boolean isFocused() {
        return false;
    }

    public boolean containsPoint(int x, int y) {
        for (V3DGuiComponent component : guiComponentList) {
            if(component.containsPoint(x - this.x, y - this.y)) {
                return true;
            }
        }

        return false;
    }

    public void add(V3DGuiComponent component) {
        guiComponentList.add(component);
        generate();
    }

    public void display() {
        if (!generated) {
            doGenerate();
            repack();
        }

        if (display != null) {
            display.display();
        }

    }

    @Override
    public void repack() {
        this.x = parentBinding.mouseX;
        this.y = parentBinding.mouseY;
        this.width = parentBinding.width;
        this.height = parentBinding.height;

        if (rootContainer != null) {
            rootContainer.setXY(parentBinding.x, parentBinding.y);
            rootContainer.setSize(parentBinding.width, parentBinding.height);
            for (V3DGuiComponent component : guiComponentList) {
                component.repack();
            }
            rootContainer.layout();
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
        if(guiComponentList.contains(component)) {
            guiComponentList.remove(component);
            generate();
        }
    }

   
}

    