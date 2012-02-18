package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.common.world.Component;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class TankSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementPanel;

    public TankSkin(V3DContext context, final Component object) {
        
        elements = new V3DGroupElement(context);

        File v3drawFile = new File("graphics/output/tank_structure.v3draw");
        final V3DrawElement element = V3DrawElement.LoadFromFile(v3drawFile, context);
        elements.add(new V3DColorElement(element, new V3DColor(135, 158, 255)));
        
        
        File v3drawFileTank = new File("graphics/output/tank_tank.v3draw");
        final V3DrawElement elementTank = V3DrawElement.LoadFromFile(v3drawFileTank, context);
        elements.add(new V3DColorElement(elementTank, new V3DColor(151, 32, 0)));
        
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
