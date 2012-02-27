package com.irr310.client.graphics;

import com.irr310.client.graphics.WorldRenderer.GuiLayer;
import com.irr310.common.tools.Vec2;
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
    private final WorldRenderer renderer;
    private V3DColor color;

    public GuiTrackingArrow( WorldRenderer renderer,V3DFollow3DCameraController cameraController, Part followed) {
        super(renderer);
        this.renderer = renderer;
        this.camera = cameraController;
        this.followed = followed;
        v3dGuiRectangle = new V3DGuiRectangle();
        color = V3DColor.black;
    }
    
    public void setColor(V3DColor color) {
        this.color = color;
    }

    @Override
    public void update() {
        
        
        Vec3 eye = camera.getEye();
        Vec3 target = camera.getTarget();
        
        Vec3 top = camera.getTop().normalize();
        Vec3 front = target.minus(eye).normalize();
        Vec3 left = top.cross(front);
        
        Vec3 distance = followed.getTransform().getTranslation().minus(eye);
        Vec3 relPos = distance.normalize();
        
        System.err.println("dot top "+ top.dot(relPos));
        System.err.println("dot left "+ left.dot(relPos));
        System.err.println("dot front "+ front.dot(relPos));
        
        
        Vec2 viewportSize = renderer.getEngine().getViewportSize();
        int minViewportSize = (int) Math.min(viewportSize.x, viewportSize.y); 
        
        Vec2 pos = new Vec2(-left.dot(relPos)*viewportSize.x, -top.dot(relPos)*viewportSize.y);
        System.err.println("pos1 "+ pos);
        pos = pos.normalize().multiply(minViewportSize*0.45).add(viewportSize.divide(2));
        System.err.println("pos2 "+ pos);
        
        
        v3dGuiRectangle.setPosition((int)pos.x,(int) pos.y);
        
        
        if(front.dot(relPos) < 0.9) {
            v3dGuiRectangle.setFillColor(color);
        } else {
            v3dGuiRectangle.setFillColor(V3DColor.transparent);
        }
        
        double size = (1000 - distance.length())/20;
        System.err.println("size "+ size);
        v3dGuiRectangle.setSize((int) size,(int) size);
        
    }

    @Override
    public V3DGuiComponent getGuiElement() {
        return v3dGuiRectangle;
    }
    
    @Override
    public GuiLayer getLayer() {
        return GuiLayer.HUD;
    }
    
    

}
