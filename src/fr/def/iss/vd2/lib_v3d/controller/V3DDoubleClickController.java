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

/**
 *
 * @author fberto
 */
public class V3DDoubleClickController implements V3DCameraController {

    int mouseXInitial = 0;
    int mouseYInitial = 0;
    private long lastClickTime = 0;
    List<V3DElement> whiteList = null;
    V3DContext context;
    V3DSelectionListener listener;
    private boolean strict = false;

    public V3DDoubleClickController(V3DContext context) {
        this.context = context;
    }


    
    public void setWhiteList(List<V3DElement> whiteList) {
        this.whiteList = whiteList;
    }

    public void setListener(V3DSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onEvent(InputEvent e) {
        
        if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;

            switch (em.getID()) {
                case MouseEvent.MOUSE_CLICKED: {
                    mouseClicked(em);
                }
                break;

            }

        }



    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 500) {
                if (e.isConsumed()) {
                    if(e.getID() == MouseEvent.MOUSE_CLICKED) {
                    }
                    return;
                }
                if (Math.abs(e.getX() - mouseXInitial) < 5 && Math.abs(e.getY() - mouseYInitial) < 5) {
                    if (whiteList == null) {
                        V3DElement overElement = context.getMouseOverlapTop();
                        listener.select(overElement);
                        e.consume();
                    } else {
                        if(strict) {
                            V3DElement overElement =  context.getMouseOverlapTop();
                            if(whiteList.contains(overElement)) {
                                listener.select(overElement);
                                e.consume();
                            }
                        }else{
                            V3DElement overElement = context.getMouseOverlapTop(whiteList);
                            listener.select(overElement);
                            e.consume();
                        }
                    }


                }
            }

            lastClickTime = currentTime;
            mouseXInitial = e.getX();
            mouseYInitial = e.getY();
        }
    }

    @Override
    public void notifyRemove() {
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }
}
