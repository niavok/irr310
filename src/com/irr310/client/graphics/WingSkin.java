package com.irr310.client.graphics;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class WingSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementPanel;

    public WingSkin(V3DContext context, final Component object) {
        
        elements = new V3DGroupElement(context);

        File v3drawFileStructure = new File("graphics/output/wing.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, context);
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(135, 158, 255)));
        
        TransformMatrix transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        
        transform.addListener(new TransformMatrixChangeListener() {

            @Override
            public void valueChanged() {
                elements.setTransformMatrix(object.getFirstPart().getTransform().toFloatBuffer());
            }
        });
        
        
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public V3DElement getElement() {
        return elements;
    }

}
