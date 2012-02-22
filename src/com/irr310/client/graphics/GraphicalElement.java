package com.irr310.client.graphics;

import fr.def.iss.vd2.lib_v3d.element.V3DElement;


public interface GraphicalElement {

    public boolean isAnimated();
    
    public void update();
    
    public void destroy();

    public boolean isDisplayable();

    public V3DElement getV3DElement();
    
}
