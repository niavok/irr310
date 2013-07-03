package com.irr310.client.graphics.effects;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.Vec3;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class BulletEffect extends GenericGraphicalElement{

    private I3dGroupElement elements;
    private float speed = 2000;
    private float currentLocation = 0;

    private final Vec3 to;
    private final Vec3 from;
    private double distance;
    private V3DLine bullet;
    private Vec3 bulletVector;
    private final UiEngine engine;

    public BulletEffect(WorldRenderer renderer, Vec3 from, Vec3 to) {
        super(renderer);
        this.engine = renderer.getEngine();
        this.from = from;
        this.to = to;
        distance = to.minus(from).length();

        bulletVector = to.minus(from).normalize();

        elements = new I3dGroupElement();

        bullet = new V3DLine();
        bullet.setThickness(3.5f);

        bullet.setLocation(from.toV3DVect3(), from.plus(bulletVector).toV3DVect3());
        //bullet.setLocation(from.toV3DVect3(), to.toV3DVect3());

        elements.add(new V3DColorElement(bullet, new V3DColor(235, 160, 140)));
    }

    @Override
    public void update() {

        Vec3 start = from.plus(bulletVector.multiply(currentLocation));
        bullet.setLocation(start.toV3DVect3(), start.plus(bulletVector.multiply(speed/20)).toV3DVect3());

        currentLocation += speed *  engine.getFramerate().getSeconds();
        if (currentLocation > distance) {
            destroy();
        }
    }

    @Override
    public boolean isDisplayable() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public I3dElement getV3DElement() {
        return elements;
    }

}
