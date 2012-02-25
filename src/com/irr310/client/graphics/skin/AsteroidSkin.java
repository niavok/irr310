package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.CelestialObject;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class AsteroidSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;

    public AsteroidSkin(WorldRenderer renderer, final CelestialObject object) {
        super(renderer);
        GraphicEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

        File v3drawFileStructure = new File("graphics/output/asteroid.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elementStructure.setScale(object.getFirstPart().getShape().toV3DVect3());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(227, 205, 182)));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
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