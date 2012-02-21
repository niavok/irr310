package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.CelestialObject;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class AsteroidSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;

    public AsteroidSkin(V3DContext context, final CelestialObject object) {
        
        elements = new V3DGroupElement(context);

        File v3drawFileStructure = new File("graphics/output/asteroid.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, context);
        elementStructure.setScale(object.getFirstPart().getShape().toV3DVect3());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(227, 205, 182)));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
    }

    @Override
    public void animate() {
        elements.setTransformMatrix(transform.toFloatBuffer());
    }

    @Override
    public V3DElement getElement() {
        return elements;
    }

}
