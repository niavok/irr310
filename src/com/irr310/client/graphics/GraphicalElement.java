package com.irr310.client.graphics;

import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.server.Time.Timestamp;


public interface GraphicalElement {

    public boolean isAnimated();
    
    public void update(Timestamp time);
    
    public void destroy();

    public boolean isDisplayable();

    public I3dElement getV3DElement();

    void init(Timestamp time);
    
}
