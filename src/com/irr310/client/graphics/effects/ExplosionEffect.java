package com.irr310.client.graphics.effects;

import java.io.File;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.Vec3;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.i3d.utils.I3dColor;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ExplosionEffect extends GenericGraphicalElement{

    private I3dGroupElement elements;
    private float speed;
    private float initialLocation;

    private final UiEngine engine;
    private final double radius;
    private V3DrawElement bubbleElement;
    private Time mInitialTime;

    public ExplosionEffect(WorldRenderer renderer, Vec3 from, double radius) {
        this.radius = radius;
        this.engine = renderer.getEngine();
        initialLocation = (float) (radius/10);
        speed = (float) radius*10; 
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
        bubbleElement = V3DrawElement.LoadFromFile(v3drawFileStructure);
        bubbleElement.setScale(0);

        bubbleElement.setPosition(from.toV3DVect3());
        //bullet.setLocation(from.toV3DVect3(), to.toV3DVect3());

        elements.add(new V3DColorElement(bubbleElement, new I3dColor(255, 204, 43)));
    }

    @Override
    public void init(Timestamp timestamp) {
        mInitialTime = timestamp.getGameTime();
    }
    
    @Override
    public void update(Timestamp time) {

        float currentLocation = speed * mInitialTime.durationTo(time.getGameTime()).getSeconds();
        
        bubbleElement.setScale(currentLocation);
        
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
