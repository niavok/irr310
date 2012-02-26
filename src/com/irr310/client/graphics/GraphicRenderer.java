package com.irr310.client.graphics;

import com.irr310.common.event.EngineEventVisitor;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;

public interface GraphicRenderer {

    V3DCameraBinding getCameraBinding();

    void init();

    void frame();

    EngineEventVisitor getEventVisitor();

    void resetGui();

}
