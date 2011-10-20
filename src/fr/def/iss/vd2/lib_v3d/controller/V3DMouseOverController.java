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

public class V3DMouseOverController implements V3DCameraController {

    private V3DContext context;
    private List<V3DElement> whiteList = null;
    private int mouseEventType = MouseEvent.MOUSE_MOVED;
    
    private V3DSelectionListener listener;

    public V3DMouseOverController(V3DContext context) {
        this.context = context;
    }

    public void setWhiteList(List<V3DElement> whiteList) {
        this.whiteList = whiteList;
    }

    

    public void setListener(V3DSelectionListener listener) {
        this.listener = listener;
    }

    public void setAction(int mouseEventType) {
        this.mouseEventType = mouseEventType;
    }

    @Override
    public void onEvent(InputEvent e) {
        if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;

            if(em.getID() == mouseEventType) {
                if(whiteList == null) {
                    V3DElement overElement =  context.getMouseOverlapTop();
                    listener.select(overElement);
                }else {
                    V3DElement overElement =  context.getMouseOverlapTop(whiteList);
                    listener.select(overElement);
                }
            }
        }
      
      
    }


    @Override
    public void notifyRemove() {
    }
}
