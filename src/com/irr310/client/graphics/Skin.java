package com.irr310.client.graphics;

import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

public abstract class Skin {

    protected Duration framerate;

    public void bind(V3DScene scene) {
    }

    public abstract boolean isAnimated();

    public abstract V3DElement getElement();

    public void setFramerate(Duration framerate) {
        this.framerate = framerate;
    }

    public void animate() {
    }

}
