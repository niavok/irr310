package com.irr310.client.graphics;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Part;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.controller.V3DFollow3DCameraController;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;

public class GuiTrackingArrow extends GuiAnimatedElement {

    private final V3DFollow3DCameraController camera;
    private final Part followed;
    private V3DGuiRectangle v3dGuiRectangle;

    public GuiTrackingArrow( WorldRenderer renderer,V3DFollow3DCameraController cameraController, Part followed) {
        super(renderer);
        this.camera = cameraController;
        this.followed = followed;
        v3dGuiRectangle = new V3DGuiRectangle();
        v3dGuiRectangle.setSize(10, 10);
        v3dGuiRectangle.setFillColor(new V3DColor(0,0,255,0.5f));
    }

    @Override
    public void update() {
        
        
        Vec3 eye = camera.getEye();
        Vec3 target = camera.getTarget();
        Vec3 top = camera.getTop();
        
        
        
        
        
    }

    public V3DGuiComponent getGuiElement() {
        return v3dGuiRectangle;
    }
    
    

}
