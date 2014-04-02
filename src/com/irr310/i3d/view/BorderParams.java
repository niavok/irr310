package com.irr310.i3d.view;

import com.irr310.i3d.Color;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.StyleSelector;
import com.irr310.i3d.view.drawable.Drawable;

public class BorderParams {

    public enum CornerStyle {
        SQUARE, BEVEL,
    }

    private StyleSelector<Measure> borderSize;
    private StyleSelector<Color> borderColor;
    private CornerStyle cornerLeftTopStyle;
    private CornerStyle cornerRightTopStyle;
    private CornerStyle cornerLeftBottomStyle;
    private CornerStyle cornerRightBottomStyle;

    private Measure cornerLeftTopSize;
    private Measure cornerRightTopSize;
    private Measure cornerLeftBottomSize;
    private Measure cornerRightBottomSize;

    private StyleSelector<Drawable> background;

    public BorderParams() {
        borderSize = new StyleSelector<Measure>(new Measure(0, false, Axis.VERTICAL));
        borderColor = new StyleSelector<Color>(Color.black);
        
        
        cornerLeftTopStyle = CornerStyle.SQUARE;
        cornerRightTopStyle = CornerStyle.SQUARE;
        cornerLeftBottomStyle = CornerStyle.SQUARE;
        cornerRightBottomStyle = CornerStyle.SQUARE;
        
        cornerLeftTopSize = new Measure(0, false, Axis.VERTICAL);
        cornerRightTopSize = new Measure(0, false, Axis.VERTICAL);
        cornerLeftBottomSize = new Measure(0, false, Axis.VERTICAL);
        cornerRightBottomSize = new Measure(0, false, Axis.VERTICAL);
        
        background = null;
    }

    public void setBorderSize(StyleSelector<Measure> borderSize) {
        this.borderSize = borderSize;
    }

    public StyleSelector<Measure> getBorderSize() {
        return borderSize;
    }

    public StyleSelector<Color> getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(StyleSelector<Color> borderColor) {
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

    public StyleSelector<Drawable> getBackground() {
        return background;
    }

    public void setBackground(StyleSelector<Drawable> background) {
        this.background = background;
    }

    public BorderParams duplicate() {
        BorderParams border = new BorderParams();
        border.borderSize = borderSize.duplicate();
        border.borderColor = borderColor;
        border.cornerLeftTopStyle = cornerLeftTopStyle;
        border.cornerRightTopStyle = cornerRightTopStyle;
        border.cornerLeftBottomStyle = cornerLeftBottomStyle;
        border.cornerRightBottomStyle = cornerRightBottomStyle;
        border.cornerLeftTopSize= cornerLeftTopSize;
        border.cornerRightTopSize = new Measure(cornerRightTopSize);
        border.cornerLeftBottomSize = new Measure(cornerLeftBottomSize);
        border.cornerRightBottomSize = new Measure(cornerRightBottomSize);
        border.background = background;
        
        return border;
    }
}
