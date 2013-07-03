package com.irr310.client.graphics;

import com.irr310.i3d.scene.element.I3dElement;


public interface GraphicalElement {

    public boolean isAnimated();
    
    public void update();
    
    public void destroy();

    public boolean isDisplayable();

    public I3dElement getV3DElement();
    
}
