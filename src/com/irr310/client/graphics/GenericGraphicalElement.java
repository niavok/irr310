package com.irr310.client.graphics;

import com.irr310.i3d.scene.element.I3dElement;

public abstract class GenericGraphicalElement implements GraphicalElement {

    public GenericGraphicalElement() {
    }
    
    public abstract boolean isAnimated();
    
    public abstract I3dElement getV3DElement();

    public abstract void update();
    
    public void destroy() {
    }
    
    
    
}
