package com.irr310.client.script.js.objects;


import com.irr310.common.tools.Vec2;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;

public class GuiRectangle implements GuiComponent {

    private final Gui gui;
    private V3DGuiRectangle component;

    public GuiRectangle(Gui gui) {
        this.gui = gui;
        component = new V3DGuiRectangle();
        component.setyAlignment(GuiYAlignment.BOTTOM);
    }
    public V3DGuiComponent getComponent() {
        return component;
    }
    
    public void setPosition(Vec2 position) {
        component.setPosition((int)position.getX(), (int) position.getY());
    }
    
    public void setSize(Vec2 size) {
        component.setSize((int)size.getX(), (int) size.getY());
    }
    
    public void setBorderColor(Color color) {
        component.setBorderColor(new V3DColor(color.r, color.g, color.b, color.a));
    }
    
    public void setFillColor(Color color) {
        component.setFillColor(new V3DColor(color.r, color.g, color.b, color.a));
    }

    

}
