package com.irr310.client.graphics;

import com.irr310.client.graphics.WorldRenderer.GuiLayer;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Part;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.controller.V3DFollow3DCameraController;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiTriangle;

public class GuiTrackingArrow extends GuiAnimatedElement {

    private final V3DFollow3DCameraController camera;
    private final Part followed;
    private V3DGuiTriangle v3dGuiTriangle;
    private final WorldRenderer renderer;
    private V3DColor color;

    public GuiTrackingArrow(WorldRenderer renderer, V3DFollow3DCameraController cameraController, Part followed) {
        super(renderer);
        this.renderer = renderer;
        this.camera = cameraController;
        this.followed = followed;
        v3dGuiTriangle = new V3DGuiTriangle();
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

        Vec2 viewportSize = renderer.getEngine().getViewportSize();
        int minViewportSize = (int) Math.min(viewportSize.x, viewportSize.y);

        Vec2 pos = new Vec2(-left.dot(relPos) * viewportSize.x, -top.dot(relPos) * viewportSize.y);
        Vec2 pxPos = pos.normalize().multiply(minViewportSize * 0.45).add(viewportSize.divide(2));

        v3dGuiTriangle.setPosition((int) pxPos.x, (int) pxPos.y);

        double deltaAngle = front.dot(relPos);

        if (deltaAngle < 0.88) {
            v3dGuiTriangle.setFillColor(color);
        } else if (deltaAngle < 0.92) {
            V3DColor copy = color.copy();
            copy.a = (float) (copy.a * (0.92 - deltaAngle) / 0.04);
            v3dGuiTriangle.setFillColor(copy);
        } else {
            v3dGuiTriangle.setFillColor(V3DColor.transparent);
        }

        double size = 5 + (Math.min(1000f - distance.length(), 0)) / 50f;

        double angle = -pos.getAngle() - Math.PI / 2;

        Vec2 topPoint = new Vec2(0, size).rotate(angle);
        Vec2 leftPoint = new Vec2(size / 3 * (1.5 + deltaAngle), -size).rotate(angle);
        Vec2 rightPoint = new Vec2(-size / 3 * (1.5 + deltaAngle), -size).rotate(angle);

        v3dGuiTriangle.setPoint((int) topPoint.x, (int) topPoint.y, (int) leftPoint.x, (int) leftPoint.y, (int) rightPoint.x, (int) rightPoint.y);
    }

    @Override
    public V3DGuiComponent getGuiElement() {
        return v3dGuiTriangle;
    }

    @Override
    public GuiLayer getLayer() {
        return GuiLayer.HUD;
    }

}
