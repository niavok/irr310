package com.irr310.client.graphics;

import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiLayer;

public interface GraphicRenderer {

    V3DCameraBinding getCameraBinding();

    void init();

    void frame(Timestamp timestamp);


    void resetGui();

    V3DGuiLayer getPopupLayer();

}
