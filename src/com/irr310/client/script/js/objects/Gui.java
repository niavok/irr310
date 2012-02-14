package com.irr310.client.script.js.objects;

import com.irr310.client.GameClient;

import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class Gui {
	
	public Gui() {
	}

	public GuiRectangle createRectangle() {
	    GuiRectangle guiRectangle = new GuiRectangle(this);
	    V3DGuiComponent component = guiRectangle.getComponent();
	    GameClient.getInstance().getGraphicEngine().addGuiComponent(component);
        return guiRectangle;
	}
	
	public void destroyRectangle(GuiRectangle rectangle) {
	    V3DGuiComponent component = rectangle.getComponent();
	    GameClient.getInstance().getGraphicEngine().removeGuiComponent(component);
	}
	
}
