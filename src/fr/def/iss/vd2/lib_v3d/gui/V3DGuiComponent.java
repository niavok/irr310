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
import org.fenggui.util.Point;

/**
 *
 * @author fberto
 */
public abstract class V3DGuiComponent {

    protected int x = 0;
    protected int y = 0;
    protected V3DLocalisable parent = null;

    protected GuiXAlignment xAlignment = GuiXAlignment.LEFT;
    protected GuiYAlignment yAlignment = GuiYAlignment.TOP;

    public enum GuiXAlignment {
        LEFT,
        RIGHT,
    }

    public enum GuiYAlignment {
        TOP,
        BOTTOM,
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        repack();
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public GuiXAlignment getxAlignment() {
        return xAlignment;
    }

    public void setxAlignment(GuiXAlignment xAlignment) {
        this.xAlignment = xAlignment;
    }

    public GuiYAlignment getyAlignment() {
        return yAlignment;
    }

    public void setyAlignment(GuiYAlignment yAlignment) {
        this.yAlignment = yAlignment;
    }

    

    public void setParent(V3DLocalisable parent) {
        this.parent = parent;
        repack();
    }


    abstract public Widget getFenGUIWidget();

    abstract public boolean containsPoint(int i, int i0);

    abstract public void repack();

    
    
}
