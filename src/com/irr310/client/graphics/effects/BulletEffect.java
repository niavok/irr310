package com.irr310.client.graphics.effects;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.GraphicEngine;
import com.irr310.common.tools.Vec3;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class BulletEffect extends GenericGraphicalElement{

    private V3DGroupElement elements;
    private float speed = 500;
    private float currentLocation = 0;

    private final Vec3 to;
    private final Vec3 from;
    private double distance;
    private V3DLine bullet;
    private Vec3 bulletVector;
    private final GraphicEngine engine;

    public BulletEffect(GraphicEngine engine, Vec3 from, Vec3 to) {
        super(engine);
        this.engine = engine;
        this.from = from;
        this.to = to;
        distance = to.minus(from).length();

        bulletVector = to.minus(from).normalize();

        elements = new V3DGroupElement(engine.getV3DContext());

        bullet = new V3DLine(engine.getV3DContext());
        bullet.setThickness(3);

        bullet.setLocation(from.toV3DVect3(), from.plus(bulletVector).toV3DVect3());
        //bullet.setLocation(from.toV3DVect3(), to.toV3DVect3());

        elements.add(new V3DColorElement(bullet, new V3DColor(255, 183, 8)));
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
    public V3DElement getV3DElement() {
        return elements;
    }

}
