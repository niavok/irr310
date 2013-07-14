package com.irr310.client.graphics.effects;

import java.io.File;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.Vec3;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ExplosionEffect extends GenericGraphicalElement{

    private I3dGroupElement elements;
    private float speed;
    private float currentLocation;

    private final UiEngine engine;
    private final double radius;
    private V3DrawElement bubbleElement;

    public ExplosionEffect(WorldRenderer renderer, Vec3 from, double radius) {
        this.radius = radius;
        this.engine = renderer.getEngine();
        currentLocation = (float) (radius/10);
        speed = (float) radius*10; 
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
        bubbleElement = V3DrawElement.LoadFromFile(v3drawFileStructure);
        bubbleElement.setScale(0);

        bubbleElement.setPosition(from.toV3DVect3());
        //bullet.setLocation(from.toV3DVect3(), to.toV3DVect3());

        elements.add(new V3DColorElement(bubbleElement, new V3DColor(255, 204, 43)));
    }

    @Override
    public void update() {

        bubbleElement.setScale(currentLocation);
        
        currentLocation += speed *  engine.getFramerate().getSeconds();
        if (currentLocation > radius) {
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
