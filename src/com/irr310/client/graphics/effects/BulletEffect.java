package com.irr310.client.graphics.effects;

import java.io.File;

import com.irr310.client.graphics.Animated;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.CelestialObject;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class BulletEffect implements Animated {

    private V3DGroupElement elements;
    private float speed = 200;
    private float currentLocation = 0;

    protected Duration framerate;
    private final Vect3 to;
    private final Vect3 from;
    private double distance;
    private V3DLine bullet;
    private Vect3 bulletVector;

    public BulletEffect(V3DContext context, Vect3 from, Vect3 to) {

        this.from = from;
        this.to = to;
        distance = to.minus(from).length();

        bulletVector = to.minus(from).normalize();

        elements = new V3DGroupElement(context);

        bullet = new V3DLine(context);
        bullet.setThickness(3);

        bullet.setLocation(from.toV3DVect3(), from.plus(bulletVector).toV3DVect3());
        //bullet.setLocation(from.toV3DVect3(), to.toV3DVect3());

        elements.add(new V3DColorElement(bullet, new V3DColor(255, 183, 8)));
    }

    @Override
    public void animate() {

        Vect3 start = from.plus(bulletVector.multiply(currentLocation));
        bullet.setLocation(start.toV3DVect3(), start.plus(bulletVector.multiply(3)).toV3DVect3());

        currentLocation += speed * framerate.getSeconds();
        if (currentLocation > distance) {
            destroy();
        }
    }

    private void destroy() {
        // TODO Auto-generated method stub
        System.err.println("do destroy");
    }

    public V3DElement getElement() {
        return elements;
    }

    public void setFramerate(Duration framerate) {
        this.framerate = framerate;
    }

}
