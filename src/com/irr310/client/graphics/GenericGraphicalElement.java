package com.irr310.client.graphics;

import fr.def.iss.vd2.lib_v3d.element.V3DElement;

public abstract class GenericGraphicalElement implements GraphicalElement {

    private final GraphicEngine engine;

    public GenericGraphicalElement(GraphicEngine engine) {
        this.engine = engine;
    }
    
    public abstract boolean isAnimated();
    
    public abstract V3DElement getV3DElement();

    public abstract void update();
    
    public void destroy() {
        engine.destroyElement(this);
    }
    
    public GraphicEngine getEngine() {
        return engine;
    }
    
    
}
