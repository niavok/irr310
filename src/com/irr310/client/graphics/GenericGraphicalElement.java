package com.irr310.client.graphics;

import com.irr310.i3d.scene.element.I3dElement;

public abstract class GenericGraphicalElement implements GraphicalElement {

    private final WorldRenderer renderer;

    public GenericGraphicalElement(WorldRenderer renderer) {
        this.renderer = renderer;
    }
    
    public abstract boolean isAnimated();
    
    public abstract I3dElement getV3DElement();

    public abstract void update();
    
    public void destroy() {
        renderer.destroyElement(this);
    }
    
    public UiEngine getEngine() {
        return renderer.getEngine();
    }
    
    public WorldRenderer getRenderer() {
        return renderer;
    }
    
    
}
