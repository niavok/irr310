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

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Map;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DMouseOverlapListener;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

public class V3DTooltipController implements V3DCameraController {

    V3DLabel tooltip;
    boolean displayed = false;
    V3DCameraBinding currentBinding;
    private int mouseY;
    private int mouseX;
    Map<V3DElement, String> labelMap;
    private final V3DContext context;
    private V3DMouseOverlapListener mouseOverlapListener;
    private boolean killed = false;

    public V3DTooltipController(V3DContext context, V3DCameraBinding binding, Map<V3DElement, String> labelMap) {
        this.currentBinding = binding;
        this.context = context;
        this.labelMap = labelMap;

        tooltip = new V3DLabel(context, "null");

        mouseOverlapListener = new V3DMouseOverlapListener() {

            @Override
            public void selectionChange() {
                checkOnce();
            }
        };

        Point mouse = binding.getLastMousePosition();
        mouseX = mouse.x;
        mouseY = mouse.y;
        //TODO: memory leak because the listener is never removed
        context.addMouseOverlapListener(mouseOverlapListener);
    }

    @Override
    public void onEvent(InputEvent e) {
        if (e instanceof MouseEvent) {
            MouseEvent em = (MouseEvent) e;
            mouseX = em.getX();
            mouseY = em.getY();
        }

        if (e instanceof MouseWheelEvent) {
            MouseWheelEvent em = (MouseWheelEvent) e;
            mouseX = em.getX();
            mouseY = em.getY();
        }

        checkOnce();


    }

    void show() {
    }

    void hide() {
        displayed = false;
        currentBinding.getGui().remove(tooltip);
        currentBinding.getGui().repack();
    }

    public void setBinding(V3DCameraBinding binding) {
        displayed = false;
        currentBinding = binding;
    }

    public void checkOnce() {

        if(killed) {
            return;
        }
        
        if (!displayed) {
            displayed = true;
            currentBinding.getGui().add(tooltip);

        }

        if (context.getMouseOverlapTop() != null && context.getMouseOverCameraBinding() == currentBinding) {
            
            V3DElement topElement = context.getMouseOverlapTop();

            String text = labelMap.get(topElement);
            if (text == null) {
                hide();
                return;
            }

            tooltip.setText(text);

            tooltip.setPosition(mouseX + 5, mouseY + 15);
            currentBinding.getGui().repack();
            if (tooltip.getSize().getX() + tooltip.getComputedPosition().getX() > currentBinding.width) {

                int newX = currentBinding.width - tooltip.getSize().getX();
                if (newX < 0) {
                    newX = 0;
                }
                tooltip.setPosition(newX, tooltip.getPosition().getY());
                currentBinding.getGui().repack();
            }

            if (tooltip.getSize().getY() + tooltip.getComputedPosition().getY() > currentBinding.height) {
                int newY = tooltip.getComputedPosition().getY() - tooltip.getSize().getY() - 10;
                if (newY < 0) {
                    newY = 0;
                }
                tooltip.setPosition(tooltip.getComputedPosition().getX(), newY);
                currentBinding.getGui().repack();
            }


        } else {
            hide();
        }
    }

    @Override
    public void notifyRemove() {
        kill();
        context.removeMouseOverlapListener(mouseOverlapListener);
    }

    private void kill() {
        hide();
        killed = true;
    }
}
