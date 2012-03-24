package com.irr310.client.graphics.effects;

import java.io.File;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.Game;
import com.irr310.common.tools.Vec3;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ExplosionEffect extends GenericGraphicalElement{

    private V3DGroupElement elements;
    private float speed = 500;
    private float currentLocation = 0;

    private final Vec3 from;
    private Vec3 bulletVector;
    private final GraphicEngine engine;
    private final double radius;
    private V3DrawElement bubbleElement;

    public ExplosionEffect(WorldRenderer renderer, Vec3 from, double radius) {
        super(renderer);
        this.radius = radius;
        this.engine = renderer.getEngine();
        this.from = from;


        elements = new V3DGroupElement(engine.getV3DContext());

        File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
        bubbleElement = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
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
    public V3DElement getV3DElement() {
        return elements;
    }

}
