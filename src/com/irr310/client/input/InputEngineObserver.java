package com.irr310.client.input;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;


public interface InputEngineObserver {
    void onMouseEvent(V3DMouseEvent event);

    void onKeyEvent(V3DKeyEvent event);

    void onQuitEvent();
}
