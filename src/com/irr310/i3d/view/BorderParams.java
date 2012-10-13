package com.irr310.i3d.view;

import com.irr310.i3d.Color;
import com.irr310.i3d.Measure;

public class BorderParams {

    public enum CornerStyle {
        SQUARE, BEVEL,
    }

    private Measure borderSize;
    private Color borderColor;
    private CornerStyle cornerLeftTopStyle;
    private CornerStyle cornerRightTopStyle;
    private CornerStyle cornerLeftBottomStyle;
    private CornerStyle cornerRightBottomStyle;

    private Measure cornerLeftTopSize;
    private Measure cornerRightTopSize;
    private Measure cornerLeftBottomSize;
    private Measure cornerRightBottomSize;

    private Drawable background;

    public BorderParams() {
        borderSize = new Measure(0, false);
        borderColor = Color.black;
        
        
        cornerLeftTopStyle = CornerStyle.SQUARE;
        cornerRightTopStyle = CornerStyle.SQUARE;
        cornerLeftBottomStyle = CornerStyle.SQUARE;
        cornerRightBottomStyle = CornerStyle.SQUARE;
        
        cornerLeftTopSize = new Measure(0, false);
        cornerRightTopSize = new Measure(0, false);
        cornerLeftBottomSize = new Measure(0, false);
        cornerRightBottomSize = new Measure(0, false);
        
        background = null;
    }

    public void setBorderSize(Measure borderSize) {
        this.borderSize = borderSize;
    }

    public Measure getBorderSize() {
        return borderSize;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public CornerStyle getCornerLeftTopStyle() {
        return cornerLeftTopStyle;
    }

    public void setCornerLeftTopStyle(CornerStyle cornerLeftTopStyle) {
        this.cornerLeftTopStyle = cornerLeftTopStyle;
    }

    public CornerStyle getCornerRightTopStyle() {
        return cornerRightTopStyle;
    }

    public void setCornerRightTopStyle(CornerStyle cornerRightTopStyle) {
        this.cornerRightTopStyle = cornerRightTopStyle;
    }

    public CornerStyle getCornerLeftBottomStyle() {
        return cornerLeftBottomStyle;
    }

    public void setCornerLeftBottomStyle(CornerStyle cornerLeftBottomStyle) {
        this.cornerLeftBottomStyle = cornerLeftBottomStyle;
    }

    public CornerStyle getCornerRightBottomStyle() {
        return cornerRightBottomStyle;
    }

    public void setCornerRightBottomStyle(CornerStyle cornerRightBottomStyle) {
        this.cornerRightBottomStyle = cornerRightBottomStyle;
    }

    public Measure getCornerLeftTopSize() {
        return cornerLeftTopSize;
    }

    public void setCornerLeftTopSize(Measure cornerLeftTopSize) {
        this.cornerLeftTopSize = cornerLeftTopSize;
    }

    public Measure getCornerRightTopSize() {
        return cornerRightTopSize;
    }

    public void setCornerRightTopSize(Measure cornerRightTopSize) {
        this.cornerRightTopSize = cornerRightTopSize;
    }

    public Measure getCornerLeftBottomSize() {
        return cornerLeftBottomSize;
    }

    public void setCornerLeftBottomSize(Measure cornerLeftBottomSize) {
        this.cornerLeftBottomSize = cornerLeftBottomSize;
    }

    public Measure getCornerRightBottomSize() {
        return cornerRightBottomSize;
    }

    public void setCornerRightBottomSize(Measure cornerRightBottomSize) {
        this.cornerRightBottomSize = cornerRightBottomSize;
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }
}
