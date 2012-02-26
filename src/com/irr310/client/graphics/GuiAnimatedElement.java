package com.irr310.client.graphics;

import fr.def.iss.vd2.lib_v3d.element.V3DElement;


public abstract class GuiAnimatedElement extends GenericGraphicalElement implements GraphicalElement {

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
    public V3DElement getV3DElement() {
        return null;
    }

    
}
