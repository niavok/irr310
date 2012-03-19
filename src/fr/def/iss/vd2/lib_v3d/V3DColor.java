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

package fr.def.iss.vd2.lib_v3d;

import java.awt.Color;

/**
 * Class representing a color with alpha channel
 * 
 * @author fberto
 */
public class V3DColor {

    public static V3DColor randomLightOpaqueColor() {

        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();

        if (r + g + b < 0.3) {
            return randomLightOpaqueColor();
        } else {
            return new V3DColor(r, g, b);
        }

    }

    public float r = 0;
    public float g = 0;
    public float b = 0;
    public float a = 1;

    /**
     * Initialize black opaque color
     */
    public V3DColor() {
    }

    /**
     * Initialize color
     * 
     * @param r red between 0.0 and 1.0
     * @param g green between 0.0 and 1.0
     * @param b blue between 0.0 and 1.0
     * @param a alpha between 0.0 and 1.0
     */
    public V3DColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Initialize color
     * 
     * @param r red between 0 and 255
     * @param g green between 0 and 255
     * @param b blue between 0 and 255
     * @param a alpha between 0.0 and 1.0
     */
    public V3DColor(int r, int g, int b, float a) {
        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
        this.a = a;
    }

    /**
     * Copy a color
     * 
     * @param color copied color
     */
    public V3DColor(V3DColor color) {
        this(color.r, color.g, color.b, color.a);
    }

    /**
     * Initialize opaque color
     * 
     * @param r red between 0.0 and 1.0
     * @param g green between 0.0 and 1.0
     * @param b blue between 0.0 and 1.0
     */
    public V3DColor(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    /**
     * Initialize opaque color
     * 
     * @param r red between 0 and 1
     * @param g green between 0 and 1
     * @param b blue between 0 and 1
     */
    public V3DColor(int r, int g, int b) {
        this(r, g, b, 1f);
    }

    /**
     * Initialize for an awt color
     * 
     * @param color awt color
     */
    public V3DColor(Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f);
    }

    public static V3DColor transparent = new V3DColor(1.0f, 1.0f, 1.0f, 0.0f);
    public static V3DColor fadetoblack = new V3DColor(0.0f, 0.0f, 0.0f, 0.0f);

    public static V3DColor azure = new V3DColor(204, 204, 255);
    public static V3DColor black = new V3DColor(0, 0, 0);
    public static V3DColor blue = new V3DColor(0, 0, 255);
    public static V3DColor darkblue = new V3DColor(0, 0, 51);
    public static V3DColor darkgrey = new V3DColor(64, 64, 64);
    public static V3DColor emerald = new V3DColor(1, 215, 88);
    public static V3DColor fushia = new V3DColor(244, 0, 161);
    public static V3DColor green = new V3DColor(0, 255, 0);
    public static V3DColor grey = new V3DColor(128, 128, 128);
    public static V3DColor lightgrey = new V3DColor(192, 192, 192);
    public static V3DColor lavander = new V3DColor(150, 131, 236);
    public static V3DColor lilas = new V3DColor(182, 102, 210);
    public static V3DColor magenta = new V3DColor(255, 0, 255);
    public static V3DColor mauve = new V3DColor(212, 115, 212);
    public static V3DColor pink = new V3DColor(253, 108, 158);
    public static V3DColor red = new V3DColor(255, 0, 0);
    public static V3DColor violet = new V3DColor(207, 160, 233);
    public static V3DColor white = new V3DColor(255, 255, 255);
    public static V3DColor yellow = new V3DColor(255, 255, 0);

    /**
     * Return a new instance of the same color
     * 
     * @return copied color
     */
    public V3DColor copy() {
        return new V3DColor(r, g, b, a);
    }

    public boolean isSame(V3DColor sameColor) {
        return a == sameColor.a && r == sameColor.r && g == sameColor.g && b == sameColor.b;
    }

    public org.fenggui.util.Color toColor() {
        return new org.fenggui.util.Color(r, g, b, a);
    }

    public V3DColor setAlpha(float alpha) {
        a = alpha;
        return this;
    }

    public org.fenggui.util.Color getFengguiColor() {
        return new org.fenggui.util.Color(r, g, b, a);
    }

    public static V3DColor mix(V3DColor baseColor, V3DColor targetColor, float mix) {
        return new V3DColor(baseColor.r * (1 - mix) + targetColor.r * mix, //
                            baseColor.g * (1 - mix) + targetColor.g * mix, //
                            baseColor.b * (1 - mix) + targetColor.b * mix, //
                            baseColor.a * (1 - mix) + targetColor.a * mix); //
    }
}
