package com.irr310.client.script.js.objects;

import com.irr310.client.GameClient;
import com.irr310.common.tools.Vect2;

import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class Gui {
	
	public Gui() {
	}

	public Vec2 getViewportSize() {
	    return new Vec2(GameClient.getInstance().getGraphicEngine().getViewportSize());
	}
	
	public GuiRectangle createRectangle() {
	    GuiRectangle guiRectangle = new GuiRectangle(this);
	    V3DGuiComponent component = guiRectangle.getComponent();
	    GameClient.getInstance().getGraphicEngine().addGuiComponent(component);
        return guiRectangle;
	}
	
	public GuiLabel createLabel() {
        GuiLabel guiLabel = new GuiLabel(this);
        V3DGuiComponent component = guiLabel.getComponent();
        GameClient.getInstance().getGraphicEngine().addGuiComponent(component);
        return guiLabel;
    }
	
	public void destroyComponent(GuiComponent guiComponent) {
	    V3DGuiComponent component = guiComponent.getComponent();
	    GameClient.getInstance().getGraphicEngine().removeGuiComponent(component);
	}
	
}
