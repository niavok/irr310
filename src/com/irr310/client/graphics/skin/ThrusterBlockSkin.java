package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Component;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ThrusterBlockSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;

    public ThrusterBlockSkin(WorldRenderer renderer, final Component object) {
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/thruster_block.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure);
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), V3DColor.fromI3d(object.getShip().getOwner().getColor())));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
        
    }

    @Override
    public void update() {
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
