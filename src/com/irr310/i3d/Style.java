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

package com.irr310.i3d;

import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.view.BorderParams.CornerStyle;
import com.irr310.i3d.view.Drawable;
import com.irr310.i3d.view.LayoutParams.LayoutGravity;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.TextView.Gravity;
import com.irr310.i3d.view.View;

/**
 * Class representing a color with alpha channel
 * 
 * @author fberto
 */
public class Style {

    public Style() {
    }

    private String parentName = null;
    private Style parent = null;
    private Font font;
    private Color textColor;
    private Color borderColor;
    private String text;
    private LayoutGravity gravityY;
    private LayoutGravity gravityX;
    private Measure cornerRightBottomSize;
    private Measure cornerLeftBottomSize;
    private Measure cornerRightTopSize;
    private Measure cornerLeftTopSize;
    private Measure borderSize;
    private CornerStyle cornerLeftTopStyle;
    private CornerStyle cornerRightTopStyle;
    private CornerStyle cornerLeftBottomStyle;
    private CornerStyle cornerRightBottomStyle;
    private Drawable background;
    private Measure marginRight;
    private Measure marginLeft;
    private Measure marginBottom;
    private Measure marginTop;
    private Measure paddingRight;
    private Measure paddingLeft;
    private Measure paddingBottom;
    private Measure paddingTop;
    private LayoutMeasure layoutWidthMeasure;
    private Gravity gravity;
    private Measure widthMeasure;
    private LayoutMeasure layoutHeightMeasure;
    private Measure heightMeasure;
    private boolean heightMeasureDefined = false;
    private boolean layoutHeightMeasureDefined = false;
    private boolean widthMeasureDefined = false;
    private boolean layoutWidthMeasureDefined = false;
    private boolean gravityDefined = false;
    private boolean paddingTopDefined = false;
    private boolean paddingBottomDefined = false;
    private boolean paddingLeftDefined = false;
    private boolean paddingRightDefined = false;
    private boolean marginTopDefined = false;
    private boolean marginBottomDefined = false;
    private boolean marginLeftDefined = false;
    private boolean marginRightDefined = false;
    private boolean backgroundDefined = false;
    private boolean cornerRightBottomStyleDefined = false;
    private boolean cornerLeftBottomStyleDefined = false;
    private boolean cornerRightTopStyleDefined = false;
    private boolean cornerLeftTopStyleDefined = false;
    private boolean cornerRightBottomSizeDefined = false;
    private boolean cornerLeftBottomSizeDefined = false;
    private boolean cornerRightTopSizeDefined = false;
    private boolean cornerLeftTopSizeDefined = false;
    private boolean borderSizeDefined = false;
    private boolean gravityXDefined = false;
    private boolean gravityYDefined = false;
    private boolean fontDefined = false;
    private boolean textColorDefined = false;
    private boolean borderColorDefined = false;

    public void setParent(String parentName) {
        this.parentName = parentName;
    }

    public void apply(View view) {

        applyParentStyle(view);

        if(layoutWidthMeasureDefined) view.getLayoutParams().setLayoutWidthMeasure(layoutWidthMeasure); 
        if(widthMeasureDefined) view.getLayoutParams().setWidthMeasure(widthMeasure);

        if(layoutHeightMeasureDefined) view.getLayoutParams().setLayoutHeightMeasure(layoutHeightMeasure);
        if(heightMeasureDefined) view.getLayoutParams().setHeightMeasure(heightMeasure);

        if(gravityXDefined) view.getLayoutParams().setLayoutGravityX(gravityX);

        if(gravityYDefined) view.getLayoutParams().setLayoutGravityY(gravityY);

        if(marginBottomDefined) view.getLayoutParams().setMarginBottomMeasure(marginBottom);
        if(marginTopDefined) view.getLayoutParams().setMarginTopMeasure(marginTop);
        if(marginLeftDefined) view.getLayoutParams().setMarginLeftMeasure(marginLeft);
        if(marginRightDefined) view.getLayoutParams().setMarginRightMeasure(marginRight);

        if(paddingBottomDefined) view.getLayoutParams().setPaddingBottomMeasure(paddingBottom);
        if(paddingTopDefined) view.getLayoutParams().setPaddingTopMeasure(paddingTop);
        if(paddingLeftDefined) view.getLayoutParams().setPaddingLeftMeasure(paddingLeft);
        if(paddingRightDefined) view.getLayoutParams().setPaddingRightMeasure(paddingRight);

        if(backgroundDefined) view.getBorderParams().setBackground(background);

        if(borderColorDefined) view.getBorderParams().setBorderColor(borderColor);
        if(borderSizeDefined) view.getBorderParams().setBorderSize(borderSize);

        if(cornerLeftTopStyleDefined) view.getBorderParams().setCornerLeftTopStyle(cornerLeftTopStyle);
        if(cornerRightTopStyleDefined) view.getBorderParams().setCornerRightTopStyle(cornerRightTopStyle);
        if(cornerLeftBottomStyleDefined) view.getBorderParams().setCornerLeftBottomStyle(cornerLeftBottomStyle);
        if(cornerRightBottomStyleDefined) view.getBorderParams().setCornerRightBottomStyle(cornerRightBottomStyle);
        if(cornerLeftTopSizeDefined) view.getBorderParams().setCornerLeftTopSize(cornerLeftTopSize);
        if(cornerRightTopSizeDefined) view.getBorderParams().setCornerRightTopSize(cornerRightTopSize);
        if(cornerLeftBottomSizeDefined) view.getBorderParams().setCornerLeftBottomSize(cornerLeftBottomSize);
        if(cornerRightBottomSizeDefined) view.getBorderParams().setCornerRightBottomSize(cornerRightBottomSize);

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if(fontDefined) textView.setFont(font);
            if(textColorDefined) textView.setTextColor(textColor);
            if(gravityDefined) textView.setGravity(gravity);
        }
    }

    private void applyParentStyle(View view) {
        if (parentName != null && !parentName.isEmpty()) {
            if (parent == null) {
                parent = I3dRessourceManager.getInstance().loadStyle(parentName);
            }

            if (parent != null) {
                parent.apply(view);
            }

        }
    }

    public void setHeightMeasure(Measure measure) {
        this.heightMeasure = measure;
        this.heightMeasureDefined = true;
    }

    public void setLayoutHeightMeasure(LayoutMeasure layoutMeasure) {
        this.layoutHeightMeasure = layoutMeasure;
        this.layoutHeightMeasureDefined = true;
    }

    public void setWidthMeasure(Measure measure) {
        this.widthMeasure = measure;
        this.widthMeasureDefined = true;
    }

    public void setLayoutWidthMeasure(LayoutMeasure layoutMeasure) {
        this.layoutWidthMeasure = layoutMeasure;
        this.layoutWidthMeasureDefined = true;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
        this.gravityDefined = true;
    }

    public void setPaddingTopMeasure(Measure measure) {
        this.paddingTop = measure;
        this.paddingTopDefined = true;
    }

    public void setPaddingBottomMeasure(Measure measure) {
        this.paddingBottom = measure;
        this.paddingBottomDefined = true;
    }

    public void setPaddingLeftMeasure(Measure measure) {
        this.paddingLeft = measure;
        this.paddingLeftDefined = true;
    }

    public void setPaddingRightMeasure(Measure measure) {
        this.paddingRight = measure;
        this.paddingRightDefined = true;
    }

    public void setMarginTopMeasure(Measure measure) {
        this.marginTop = measure;
        this.marginTopDefined = true;
    }

    public void setMarginBottomMeasure(Measure measure) {
        this.marginBottom = measure;
        this.marginBottomDefined = true;
    }

    public void setMarginLeftMeasure(Measure measure) {
        this.marginLeft = measure;
        this.marginLeftDefined = true;
    }

    public void setMarginRightMeasure(Measure measure) {
        this.marginRight = measure;
        this.marginRightDefined = true;
    }

    public void setBackground(Drawable drawable) {
        this.background = drawable;
        this.backgroundDefined = true;
    }

    public void setCornerRightBottomStyle(CornerStyle style) {
        this.cornerRightBottomStyle = style;
        this.cornerRightBottomStyleDefined = true;
    }

    public void setCornerLeftBottomStyle(CornerStyle style) {
        this.cornerLeftBottomStyle = style;
        this.cornerLeftBottomStyleDefined = true;
    }

    public void setCornerRightTopStyle(CornerStyle style) {
        this.cornerRightTopStyle = style;
        this.cornerRightTopStyleDefined = true;
    }

    public void setCornerLeftTopStyle(CornerStyle style) {
        this.cornerLeftTopStyle = style;
        this.cornerLeftTopStyleDefined = true;
    }

    public void setCornerRightBottomSize(Measure measure) {
        this.cornerRightBottomSize = measure;
        this.cornerRightBottomSizeDefined = true;
    }

    public void setCornerLeftBottomSize(Measure measure) {
        this.cornerLeftBottomSize = measure;
        this.cornerLeftBottomSizeDefined = true;
    }

    public void setCornerRightTopSize(Measure measure) {
        this.cornerRightTopSize = measure;
        this.cornerRightTopSizeDefined = true;
    }

    public void setCornerLeftTopSize(Measure measure) {
        this.cornerLeftTopSize = measure;
        this.cornerLeftTopSizeDefined = true;
    }

    public void setBorderSize(Measure measure) {
        this.borderSize = measure;
        this.borderSizeDefined = true;
    }

    public void setLayoutGravityX(LayoutGravity gravityX) {
        this.gravityX = gravityX;
        this.gravityXDefined = true;
    }

    public void setLayoutGravityY(LayoutGravity gravityY) {
        this.gravityY = gravityY;
        this.gravityYDefined = true;
    }
   
    public void setFont(Font font) {
        this.font = font;
        this.fontDefined = true;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        this.textColorDefined = true;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        this.borderColorDefined = true;
    }

}
