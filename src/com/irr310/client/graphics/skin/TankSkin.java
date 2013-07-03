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
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class TankSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;

    public TankSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFile = new File("graphics/output/tank_structure.v3draw");
        final V3DrawElement element = V3DrawElement.LoadFromFile(v3drawFile, engine.getV3DContext());
        elements.add(new V3DColorElement(element, new V3DColor(135, 158, 255)));
        
        
        File v3drawFileTank = new File("graphics/output/tank_tank.v3draw");
        final V3DrawElement elementTank = V3DrawElement.LoadFromFile(v3drawFileTank, engine.getV3DContext());
        elements.add(new V3DColorElement(elementTank, new V3DColor(151, 32, 0)));
        
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
