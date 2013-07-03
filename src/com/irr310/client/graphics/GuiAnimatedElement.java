package com.irr310.client.graphics;

import com.irr310.client.graphics.WorldRenderer.GuiLayer;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;


public abstract class GuiAnimatedElement extends GenericGraphicalElement {

    public GuiAnimatedElement(WorldRenderer renderer) {
        super(renderer);
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

    public abstract V3DGuiComponent getGuiElement();

    public abstract GuiLayer getLayer();

    
}
