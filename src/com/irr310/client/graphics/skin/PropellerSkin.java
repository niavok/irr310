package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.LinearEngineCapacity;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class PropellerSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementRotor;
    private float angle;
    private LinearEngineCapacity linearEngineCapacity;
    private float speed;
    private TransformMatrix transform;

    public PropellerSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        GraphicEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

        // stator
        File v3drawFileStator = new File("graphics/output/big_propeller_stator.v3draw");
        final V3DrawElement elementStator = V3DrawElement.LoadFromFile(v3drawFileStator, engine.getV3DContext());
        elements.add(new V3DColorElement(elementStator, new V3DColor(135, 158, 255)));

        // stator
        File v3drawFileRotor = new File("graphics/output/big_propeller_rotor.v3draw");
        elementRotor = V3DrawElement.LoadFromFile(v3drawFileRotor, engine.getV3DContext());
        elements.add(new V3DColorElement(elementRotor, new V3DColor(135, 158, 169)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        // Configure animation
        linearEngineCapacity = object.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        angle = 0;
        speed = 50f;
    }

    @Override
    public V3DElement getV3DElement() {
        return elements;
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        angle += getEngine().getFramerate().getSeconds()* speed * linearEngineCapacity.getCurrentThrust();
        angle = angle % 360f;
        
        elementRotor.setRotation(0, angle, 0);
    }
    
    @Override
    public boolean isDisplayable() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
    

}
