package com.irr310.client.graphics;

import com.irr310.client.graphics.WorldRenderer.GuiLayer;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.zone.Part;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.controller.V3DFollow3DCameraController;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiTriangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

public class GuiTrackingArrow extends GuiAnimatedElement {

    private final V3DFollow3DCameraController camera;
    private final Part followed;
    private V3DGuiTriangle v3dGuiTriangle;
    private V3DLabel v3dLabel;
    private V3DContainer container;
    private final WorldRenderer renderer;
    private V3DColor color;

    public GuiTrackingArrow(WorldRenderer renderer, V3DFollow3DCameraController cameraController, Part followed) {
        super(renderer);
        this.renderer = renderer;
        this.camera = cameraController;
        this.followed = followed;
        v3dGuiTriangle = new V3DGuiTriangle();
        container = new V3DContainer();
        v3dLabel = new V3DLabel("plop");
        v3dLabel.setFontStyle("Ubuntu", "", 10);
        container.add(v3dGuiTriangle);
        container.add(v3dLabel);
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
        Vec3 left = top.cross(front).normalize();

        Vec3 distance = followed.getTransform().getTranslation().minus(eye);
        Vec3 relPos = distance.normalize();

        Vec2 viewportSize = renderer.getEngine().getViewportSize();
        int minViewportSize = (int) Math.min(viewportSize.x, viewportSize.y);

        Vec2 pos = new Vec2(-left.dot(relPos) * viewportSize.x, -top.dot(relPos) * viewportSize.y);
        Vec2 pxPos = pos.normalize().multiply(minViewportSize * 0.45).add(viewportSize.divide(2));

        
        
        double deltaAngle = front.dot(relPos);

        
        // System.err.println("eye: "+eye);
        // System.err.println("target: "+target);
        // System.err.println("distance: "+distance);
        //
        //
        // System.err.println("relPos: "+relPos);
        // System.err.println("front: "+front);
        // System.err.println("top: "+top);
        // System.err.println("left: "+left);
        //
        // System.err.println("front cross relPos: "+deltaAngle);
        // System.err.println("left cross relPos: "+left.dot(relPos));
        // System.err.println("top cross relPos: "+top.dot(relPos));

        if (deltaAngle < 0.88) {
            v3dGuiTriangle.setFillColor(color);
            v3dLabel.setColor(color, V3DColor.transparent);
        } else if (deltaAngle < 0.92) {
            V3DColor copy = color.copy();
            copy.a = (float) (copy.a * (0.92 - deltaAngle) / 0.04);
            v3dGuiTriangle.setFillColor(copy);
            v3dLabel.setColor(copy, V3DColor.transparent);
        } else {
            v3dGuiTriangle.setFillColor(V3DColor.transparent);
            v3dLabel.setColor(V3DColor.transparent, V3DColor.transparent);
        }

        // System.err.println("distance.length(): "+distance.length());
        v3dLabel.setText(""+(int) distance.length()+" m");
        
        double size = 10 + (Math.max(1000f - distance.length(), 0)) / 100f;

        assert(size != Double.NaN);
        double boxSize = size *5;
        
        container.setPosition((int) (pxPos.x - boxSize), (int) (pxPos.y  - boxSize));
        container.setSize((int) boxSize*2,(int) boxSize*2);
        v3dLabel.setFontStyle("Ubuntu", "", (int) (size/1.3));
        
        // System.err.println("size: "+size);

        double angle = -pos.getAngle() - Math.PI / 2;

        Vec2 posText = new Vec2(-0,50).rotate(-  angle);
        v3dLabel.setPosition((int) ( posText.x+boxSize)-10, (int)(posText.y +boxSize)+15);
        
        Vec2 topPoint = new Vec2(0, size).rotate(angle);
        Vec2 leftPoint = new Vec2(size / 3 * (1.5 + deltaAngle), -size).rotate(angle);
        Vec2 rightPoint = new Vec2(-size / 3 * (1.5 + deltaAngle), -size).rotate(angle);

        v3dGuiTriangle.setPosition((int)boxSize,(int) boxSize);
        
        v3dGuiTriangle.setPoint((int) topPoint.x , (int) topPoint.y, (int) leftPoint.x, (int) leftPoint.y, (int) rightPoint.x, (int) rightPoint.y);
        
        
        
    }

    @Override
    public V3DGuiComponent getGuiElement() {
        return container;
    }

    @Override
    public GuiLayer getLayer() {
        return GuiLayer.HUD;
    }

}
