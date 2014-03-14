package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class MonolithSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;

    public MonolithSkin(WorldRenderer renderer, final CelestialObject object) {
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFileMonolith = new File("graphics/output/monolith_monolith.v3draw");
        final V3DrawElement element = V3DrawElement.LoadFromFile(v3drawFileMonolith);
        element.setRotation(90, 0, 0);
        elements.add(new V3DColorElement(new V3DShaderElement(element, "propeller"), new V3DColor(20, 20, 20)));

        File v3drawFileArmature = new File("graphics/output/monolith_armature.v3draw");
        final V3DrawElement elementArmature = V3DrawElement.LoadFromFile(v3drawFileArmature);
        elementArmature.setRotation(90, 0, 0);
        elements.add(new V3DColorElement(new V3DShaderElement(elementArmature, "propeller"), new V3DColor(135, 158, 169)));

        File v3drawFilePlasma = new File("graphics/output/monolith_plasma.v3draw");
        final V3DrawElement elementPlasma = V3DrawElement.LoadFromFile(v3drawFilePlasma);
        elements.add(new V3DColorElement(new V3DShaderElement(elementPlasma, "plasma"), new V3DColor(151, 32, 153, 0.3f)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
    }

    @Override
    public void init(Timestamp time) {
    }
    
    @Override
    public void update(Timestamp time) {
        elements.setTransformMatrix(transform.toFloatBuffer());
    }

    @Override
    public I3dElement getV3DElement() {
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
