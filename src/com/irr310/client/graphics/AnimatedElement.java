package com.irr310.client.graphics;


public abstract class AnimatedElement extends GenericGraphicalElement implements GraphicalElement {

    public AnimatedElement(WorldRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean isDisplayable() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    
}
