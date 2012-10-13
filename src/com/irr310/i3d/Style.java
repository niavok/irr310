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

import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.LayoutParams.LayoutAlign;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;



/**
 * Class representing a color with alpha channel
 * 
 * @author fberto
 */
public class Style extends TextView {

    public Style() {
        super(null);
    }

    private String parentName = null;
    private Style parent = null;

    public void setParent(String parentName) {
        this.parentName = parentName;
        
    }

    public void apply(View view) {
        
        applyParentStyle(view);
        
        view.getLayoutParams().setLayoutWidthMeasure(getLayoutParams().getLayoutWidthMeasure());
        view.getLayoutParams().setWidthMeasure(getLayoutParams().getMeasurePoint().getX());
        
        view.getLayoutParams().setLayoutHeightMeasure(getLayoutParams().getLayoutHeightMeasure());
        view.getLayoutParams().setHeightMeasure(getLayoutParams().getMeasurePoint().getY());
        
        view.getLayoutParams().setLayoutGravityX(getLayoutParams().getLayoutGravityX());
        
        view.getLayoutParams().setLayoutGravityY(getLayoutParams().getLayoutGravityY());
        
        view.getLayoutParams().setMarginBottomMeasure(getLayoutParams().getLayoutMarginBottom());
        view.getLayoutParams().setMarginTopMeasure(getLayoutParams().getLayoutMarginTop());
        view.getLayoutParams().setMarginLeftMeasure(getLayoutParams().getLayoutMarginLeft());
        view.getLayoutParams().setMarginRightMeasure(getLayoutParams().getLayoutMarginRight());
        
        view.getLayoutParams().setPaddingBottomMeasure(getLayoutParams().getLayoutPaddingBottom());
        view.getLayoutParams().setPaddingTopMeasure(getLayoutParams().getLayoutPaddingTop());
        view.getLayoutParams().setPaddingLeftMeasure(getLayoutParams().getLayoutPaddingLeft());
        view.getLayoutParams().setPaddingRightMeasure(getLayoutParams().getLayoutPaddingRight());
        
        view.getBorderParams().setBackground(getBorderParams().getBackground());
        
        view.getBorderParams().setBorderColor(getBorderParams().getBorderColor());
        view.getBorderParams().setBorderSize(getBorderParams().getBorderSize());
        
        view.getBorderParams().setCornerLeftTopStyle(getBorderParams().getCornerLeftBottomStyle());
        view.getBorderParams().setCornerRightTopStyle(getBorderParams().getCornerRightTopStyle());
        view.getBorderParams().setCornerLeftBottomStyle(getBorderParams().getCornerLeftBottomStyle());
        view.getBorderParams().setCornerRightBottomStyle(getBorderParams().getCornerRightBottomStyle());
        view.getBorderParams().setCornerLeftTopSize(getBorderParams().getCornerLeftTopSize());
        view.getBorderParams().setCornerRightTopSize(getBorderParams().getCornerRightTopSize());
        view.getBorderParams().setCornerLeftBottomSize(getBorderParams().getCornerLeftBottomSize());
        view.getBorderParams().setCornerRightBottomSize(getBorderParams().getCornerRightBottomSize());
        
        if(view.getClass().isAssignableFrom(TextView.class)) {
            TextView textView = (TextView) view;
            textView.setFont(font);
            textView.setTextColor(textColor);
        }
    }

    private void applyParentStyle(View view) {
        if(parentName != null) {
            if(parent == null) {
                parent = I3dRessourceManager.getInstance().loadStyle(parentName);
            }
            
            if(parent != null) {
                parent.apply(view);
            }
            
        }
    }
   
}
