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

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

/**
 *
 * @author fberto
 */
public class GuiFpsIndicator extends V3DLabel implements GraphicalElement{
    
    final long NANO_IN_MILLI = 1000000;

    private long currentTime;
    private float lastFps = 0;
    private long lastFpsMesureTime = 0;
    private int frameMesureCount = 0;

    private DecimalFormat format;

    private final GraphicEngine engine;
    
    public GuiFpsIndicator(GraphicEngine engine) {
        super("-- fps");
        this.engine = engine;
        currentTime = System.nanoTime()/NANO_IN_MILLI;
        setColor(new V3DColor(0.6f,0.6f,0.6f), V3DColor.transparent);
        format = new DecimalFormat("0.0");
    }
    
    @Override
    public void update() {
        currentTime = System.nanoTime()/NANO_IN_MILLI;
        frameMesureCount++;
        
        if(currentTime > lastFpsMesureTime+1000) {
            lastFps = (float) (((double) frameMesureCount) / ((double) (currentTime-lastFpsMesureTime) / 1000.0));

            frameMesureCount = 0;
            lastFpsMesureTime = currentTime;
            setText(""+format.format(lastFps)+" fps");
        }     
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
