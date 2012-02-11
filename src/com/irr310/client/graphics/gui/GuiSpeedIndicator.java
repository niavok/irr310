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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fenggui.Widget;

import com.irr310.client.graphics.Animated;
import com.irr310.common.world.Part;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

/**
 *
 * @author fberto
 */
public class GuiSpeedIndicator extends V3DLabel implements Animated{
    
    final long NANO_IN_MILLI = 1000000;

    private long currentTime;
    private float lastFps = 0;
    private long lastFpsMesureTime = 0;
    private int frameMesureCount = 0;

    private final Part part;

    private DecimalFormat format;
    
    public GuiSpeedIndicator(V3DContext context, Part part) {
        super(context, "-- m/s");
        this.part = part;
        currentTime = System.nanoTime()/NANO_IN_MILLI;
        setColor(V3DColor.darkgrey, V3DColor.transparent);
        format = new DecimalFormat("0.0");
    }
    
    @Override
    public void animate() {
        
            setText(format.format(part.getLinearSpeed().length())+" m/s");
    }

}
