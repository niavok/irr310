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

package com.irr310.client.graphics.gui;

import java.text.DecimalFormat;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.GraphicalElement;
import com.irr310.common.world.Part;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

/**
 *
 * @author fberto
 */
public class GuiSpeedIndicator extends V3DLabel implements GraphicalElement{
    
    final long NANO_IN_MILLI = 1000000;

    private final Part part;

    private DecimalFormat format;

    private final GraphicEngine engine;
    
    public GuiSpeedIndicator(GraphicEngine engine, Part part) {
        super("-- m/s");
        this.engine = engine;
        this.part = part;
        setColor(V3DColor.darkgrey, V3DColor.transparent);
        format = new DecimalFormat("0.0");
    }
    
    @Override
    public void update() {
        
            setText(format.format(part.getLinearSpeed().length())+" m/s");
    }

    
    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void destroy() {
        engine.destroyElement(this);
    }

    @Override
    public boolean isDisplayable() {
        return false;
    }

    @Override
    public V3DElement getV3DElement() {
        return null;
    }
}
