package com.irr310.client.graphics;

import com.irr310.client.graphics.WorldRenderer.GuiLayer;
import com.irr310.i3d.scene.element.I3dElement;


public abstract class GuiAnimatedElement extends GenericGraphicalElement {

    public GuiAnimatedElement(WorldRenderer renderer) {
    }

    @Override
    public boolean isDisplayable() {
        return false;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
    
    @Override
    public I3dElement getV3DElement() {
        return null;
    }

    public abstract GuiLayer getLayer();

    
}
