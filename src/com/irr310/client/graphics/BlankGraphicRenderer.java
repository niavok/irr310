package com.irr310.client.graphics;

import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEventVisitor;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;

public class BlankGraphicRenderer implements GraphicRenderer {

    private V3DSimple2DCamera camera;
    private final GraphicEngine engine;
    private V3DCameraBinding cameraBinding;

    public BlankGraphicRenderer(GraphicEngine engine) {
        this.engine = engine;
    }
    
    @Override
    public void init() {
        camera = new V3DSimple2DCamera(engine.getV3DContext());
        camera.setBackgroundColor(V3DColor.white);
        cameraBinding = V3DCameraBinding.buildFullscreenCamera(camera);
    }
    
    @Override
    public V3DCameraBinding getCameraBinding() {
        return cameraBinding;
    }

    @Override
    public void frame() {
    }

    @Override
    public EngineEventVisitor getEventVisitor() {
        return new DefaultEngineEventVisitor();
    }

    @Override
    public void resetGui() {
    }

}
