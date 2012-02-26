package com.irr310.client.script.js.objects;

import com.irr310.client.GameClient;
import com.irr310.common.Game;
import com.irr310.common.event.AddGuiComponentEvent;
import com.irr310.common.event.RemoveGuiComponentEvent;

import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class Gui {
	
	public Gui() {
	}

	public Vec2 getViewportSize() {
	    return new Vec2(GameClient.getInstance().getGraphicEngine().getViewportSize());
	}
	
	public float getFps() {
	    return GameClient.getInstance().getGraphicEngine().getFps();
	}
	
	public GuiRectangle createRectangle() {
	    GuiRectangle guiRectangle = new GuiRectangle(this);
	    V3DGuiComponent component = guiRectangle.getComponent();
	    Game.getInstance().sendToAll(new AddGuiComponentEvent(component));
        return guiRectangle;
	}
	
	public GuiLine createLine() {
	    GuiLine guiLine = new GuiLine(this);
        V3DGuiComponent component = guiLine.getComponent();
        Game.getInstance().sendToAll(new AddGuiComponentEvent(component));
        return guiLine;
    }
	
	public GuiLabel createLabel() {
        GuiLabel guiLabel = new GuiLabel(this);
        V3DGuiComponent component = guiLabel.getComponent();
        Game.getInstance().sendToAll(new AddGuiComponentEvent(component));
        return guiLabel;
    }
	
	public void destroyComponent(GuiComponent guiComponent) {
	    V3DGuiComponent component = guiComponent.getComponent();
	    Game.getInstance().sendToAll(new RemoveGuiComponentEvent(component));
	}
	
}
