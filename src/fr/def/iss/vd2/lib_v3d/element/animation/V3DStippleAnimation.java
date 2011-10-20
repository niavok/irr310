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

package fr.def.iss.vd2.lib_v3d.element.animation;

import fr.def.iss.vd2.lib_v3d.element.LineDrawable;

/**
 *
 * @author orenau
 */
public class V3DStippleAnimation implements V3DAnimation {

    private LineDrawable element;
    private final long period;
    private final int factor;
    private final int pattern;

    public V3DStippleAnimation(LineDrawable element, long period, int factor, short pattern) {
        this.element = element;
        this.period = period;
        this.factor = factor;
        this.pattern = (pattern & 0x0000FFFF) | ((pattern << 16) & 0xFFFF0000);
    }

    @Override
    public void preDisplay() {
        long t = System.currentTimeMillis();
        long d = period / 16;
        long p = (t % period) / d;
        int newPattern = Integer.rotateRight(pattern, (int) p);
        element.setStipple(factor, (short) (newPattern & 0xFFFF));
    }

    @Override
    public void postDisplay() {
    }

    @Override
    public boolean doDisplay() {
        return true;
    }

}
