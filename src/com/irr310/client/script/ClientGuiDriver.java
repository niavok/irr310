package com.irr310.client.script;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public interface ClientGuiDriver {

    void init();

    void keyRelease(int keyCode, String character);

    void keyPressed(int keyCode, String character);

    void mouseEvent(V3DMouseEvent mouseEvent);

    void frame();

}
