// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.controller;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.listener.V3DSelectionListener;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

public class V3DSelectController implements V3DCameraController {

    private V3DContext context;
    private List<V3DElement> whiteList = null;
    private int mouseEventType = MouseEvent.MOUSE_CLICKED;
    private int mouseButton = MouseEvent.BUTTON1;
    private boolean strict = false;
    private V3DSelectionListener listener;
    private boolean enable = true;

    public V3DSelectController(V3DContext context) {
        assert (context != null);
        this.context = context;
    }

    public void setWhiteList(List<V3DElement> whiteList) {
        this.whiteList = whiteList;
    }

    public void setListener(V3DSelectionListener listener) {
        this.listener = listener;
    }

    public void setAction(int mouseEventType, int mouseButton) {
        this.mouseEventType = mouseEventType;
        this.mouseButton = mouseButton;
    }

    @Override
    public void onEvent(InputEvent e) {


        if (!enable || e.isConsumed()) {
            return;
        }
        if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;

            if (em.getID() == mouseEventType && em.getButton() == mouseButton) {
                if (whiteList == null) {
                    V3DElement overElement = context.getMouseOverlapTop();
                    if (listener.select(overElement)) {
                        e.consume();
                    }

                } else {
                    if (strict) {
                        V3DElement overElement = context.getMouseOverlapTop();
                        if (whiteList.contains(overElement)) {
                            if (listener.select(overElement)) {
                                e.consume();
                            }
                        }
                    } else {
                        V3DElement overElement = context.getMouseOverlapTop(whiteList);
                        if (overElement != null || whiteList.contains(null)) {
                            if (listener.select(overElement)) {
                                e.consume();
                            }
                        }
                    }
                }
            }
        }


    }

    public void setEnabled(boolean b) {
        enable = b;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public void notifyRemove() {
    }
}
