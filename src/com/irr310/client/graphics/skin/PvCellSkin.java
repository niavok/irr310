package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Component;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class PvCellSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementPanel;
    private TransformMatrix transform;

    public PvCellSkin(V3DContext context, final Component object) {
        
        elements = new V3DGroupElement(context);

        // structure
        File v3drawFileStructure = new File("graphics/output/pvcell_structure.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, context);
        elements.add(new V3DColorElement(elementStructure, new V3DColor(135, 158, 255)));

        // panel
        File v3drawFilePanel = new File("graphics/output/pvcell_panel.v3draw");
        elementPanel = V3DrawElement.LoadFromFile(v3drawFilePanel, context);
        elements.add(new V3DColorElement(elementPanel, new V3DColor(0,20,60)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        elementStructure.setRotation(90, 0, 0);
        elementPanel.setRotation(90, 0, 0);
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
