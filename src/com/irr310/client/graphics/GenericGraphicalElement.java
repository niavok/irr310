package com.irr310.client.graphics;

import fr.def.iss.vd2.lib_v3d.element.V3DElement;

public abstract class GenericGraphicalElement implements GraphicalElement {

    private final WorldRenderer renderer;

    public GenericGraphicalElement(WorldRenderer renderer) {
        this.renderer = renderer;
    }
    
    public abstract boolean isAnimated();
    
    public abstract V3DElement getV3DElement();

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
