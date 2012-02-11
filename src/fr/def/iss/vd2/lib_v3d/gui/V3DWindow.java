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

import org.fenggui.Widget;
import org.fenggui.composite.Window;

import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DWindow extends V3DGuiComponent {

    private Window window;
    private int width = 150;
    private int height = 50;

    public V3DWindow(V3DContext context ,String text) {
        window = new Window( false,false,false,false);
        //label.setPosition(new Point(100,200));
        window.setXY(50, 100);
        
    }



    @Override
    public Widget getFenGUIWidget() {
        return window;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        // TODO: make windows transparent
         if(x < this.x || y < this.y) {
            return false;
        }

        if(x > this.x + window.getWidth() || y > this.y + window.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    public void repack() {
        window.setWidth(width);
        window.setWidth(height);
        
        if(parent != null) {
            window.setXY(x, parent.getHeight() - window.getHeight() - y );
        }
        
    }

}
