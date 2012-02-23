package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.GunCapacity;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DPoint;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class GunSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;

    public GunSkin(GraphicEngine engine, final Component object) {
        super(engine);
        elements = new V3DGroupElement(engine.getV3DContext());

        File v3drawFileStructure = new File("graphics/output/gun.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(135, 158, 255)));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        
        
        GunCapacity gunCapacity = (GunCapacity) object.getCapacitiesByName("gun");
        
        
        final V3DLine line = new V3DLine(engine.getV3DContext());
        line.setThickness(1);
        line.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, (float) gunCapacity.range, 0));
        elements.add(new V3DColorElement(line, new V3DColor(255, 158, 135, 0.5f)));

        
        final V3DPoint point100 = new V3DPoint(engine.getV3DContext());
        point100.setSize(5);
        point100.setPosition(new V3DVect3(0, (float) gunCapacity.range, 0));
        elements.add(new V3DColorElement(point100, new V3DColor(255, 58, 35, 0.8f)));
        
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
    }

    @Override
    public V3DElement getV3DElement() {
        return elements;
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
