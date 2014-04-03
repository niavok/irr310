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

package com.irr310.i3d.utils;

import java.awt.Color;

/**
 * Class representing a color with alpha channel
 * 
 * @author fberto
 */
public class I3dColor {

    public static I3dColor randomLightOpaqueColor() {

        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();

        if (r + g + b < 0.3) {
            return randomLightOpaqueColor();
        } else {
            return new I3dColor(r, g, b);
        }

    }

    public float r = 0;
    public float g = 0;
    public float b = 0;
    public float a = 1;

    /**
     * Initialize black opaque color
     */
    public I3dColor() {
    }

    /**
     * Initialize color
     * 
     * @param r red between 0.0 and 1.0
     * @param g green between 0.0 and 1.0
     * @param b blue between 0.0 and 1.0
     * @param a alpha between 0.0 and 1.0
     */
    public I3dColor(float r, float g, float b, float a) {
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
    public I3dColor(int r, int g, int b, float a) {
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
    public I3dColor(I3dColor color) {
        this(color.r, color.g, color.b, color.a);
    }

    /**
     * Initialize opaque color
     * 
     * @param r red between 0.0 and 1.0
     * @param g green between 0.0 and 1.0
     * @param b blue between 0.0 and 1.0
     */
    public I3dColor(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    /**
     * Initialize opaque color
     * 
     * @param r red between 0 and 1
     * @param g green between 0 and 1
     * @param b blue between 0 and 1
     */
    public I3dColor(int r, int g, int b) {
        this(r, g, b, 1f);
    }

    /**
     * Initialize for an awt color
     * 
     * @param color awt color
     */
    public I3dColor(Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f);
    }

    public static I3dColor transparent = new I3dColor(1.0f, 1.0f, 1.0f, 0.0f);
    public static I3dColor fadetoblack = new I3dColor(0.0f, 0.0f, 0.0f, 0.0f);

    public static I3dColor azure = new I3dColor(204, 204, 255);
    public static I3dColor black = new I3dColor(0, 0, 0);
    public static I3dColor blue = new I3dColor(0, 0, 255);
    public static I3dColor darkblue = new I3dColor(0, 0, 51);
    public static I3dColor darkgrey = new I3dColor(64, 64, 64);
    public static I3dColor emerald = new I3dColor(1, 215, 88);
    public static I3dColor fushia = new I3dColor(244, 0, 161);
    public static I3dColor green = new I3dColor(0, 255, 0);
    public static I3dColor grey = new I3dColor(128, 128, 128);
    public static I3dColor lightgrey = new I3dColor(192, 192, 192);
    public static I3dColor lavander = new I3dColor(150, 131, 236);
    public static I3dColor lilas = new I3dColor(182, 102, 210);
    public static I3dColor magenta = new I3dColor(255, 0, 255);
    public static I3dColor mauve = new I3dColor(212, 115, 212);
    public static I3dColor pink = new I3dColor(253, 108, 158);
    public static I3dColor red = new I3dColor(255, 0, 0);
    public static I3dColor violet = new I3dColor(207, 160, 233);
    public static I3dColor white = new I3dColor(255, 255, 255);
    public static I3dColor yellow = new I3dColor(255, 255, 0);

    /**
     * Return a new instance of the same color
     * 
     * @return copied color
     */
    public I3dColor copy() {
        return new I3dColor(r, g, b, a);
    }

    public boolean isSame(I3dColor sameColor) {
        return a == sameColor.a && r == sameColor.r && g == sameColor.g && b == sameColor.b;
    }

    public I3dColor setAlpha(float alpha) {
        a = alpha;
        return this;
    }

    public static I3dColor mix(I3dColor baseColor, I3dColor targetColor, float mix) {
        return new I3dColor(baseColor.r * (1 - mix) + targetColor.r * mix, //
                            baseColor.g * (1 - mix) + targetColor.g * mix, //
                            baseColor.b * (1 - mix) + targetColor.b * mix, //
                            baseColor.a * (1 - mix) + targetColor.a * mix); //
    }

    public static I3dColor fromI3d(com.irr310.i3d.Color color) {
        return new I3dColor(color.r, color.g, color.b, color.a);
    }
}
