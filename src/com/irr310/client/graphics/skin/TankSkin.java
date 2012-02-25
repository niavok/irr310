package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Component;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class TankSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;

    public TankSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        GraphicEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

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
