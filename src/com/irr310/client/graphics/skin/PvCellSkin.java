package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Component;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.i3d.utils.I3dColor;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class PvCellSkin extends Skin {

    private I3dGroupElement elements;
    private V3DrawElement elementPanel;
    private TransformMatrix transform;

    public PvCellSkin(WorldRenderer renderer, final Component object) {
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        // structure
        File v3drawFileStructure = new File("graphics/output/pvcell_structure.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure);
        elements.add(new V3DColorElement(elementStructure, new I3dColor(135, 158, 255)));

        // panel
        File v3drawFilePanel = new File("graphics/output/pvcell_panel.v3draw");
        elementPanel = V3DrawElement.LoadFromFile(v3drawFilePanel);
        elements.add(new V3DColorElement(elementPanel, new I3dColor(0,20,60)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        elementStructure.setRotation(90, 0, 0);
        elementPanel.setRotation(90, 0, 0);
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
