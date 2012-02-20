package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class MonolithSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementPanel;

    public MonolithSkin(V3DContext context, final CelestialObject object) {
        
        elements = new V3DGroupElement(context);

        File v3drawFileMonolith = new File("graphics/output/monolith_monolith.v3draw");
        final V3DrawElement element = V3DrawElement.LoadFromFile(v3drawFileMonolith, context);
        elements.add(new V3DColorElement(new V3DShaderElement(element, "propeller"), new V3DColor(20,20,20)));
        
        
        File v3drawFileArmature = new File("graphics/output/monolith_armature.v3draw");
        final V3DrawElement elementArmature = V3DrawElement.LoadFromFile(v3drawFileArmature, context);
        elements.add(new V3DColorElement(new V3DShaderElement(elementArmature, "propeller"), new V3DColor(135, 158, 169)));
        
        File v3drawFilePlasma = new File("graphics/output/monolith_plasma.v3draw");
        final V3DrawElement elementPlasma = V3DrawElement.LoadFromFile(v3drawFilePlasma, context);
        elements.add(new V3DColorElement(new V3DShaderElement(elementPlasma, "propeller"), new V3DColor(151, 32, 153)));
        
        
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
