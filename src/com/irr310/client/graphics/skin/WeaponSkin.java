package com.irr310.client.graphics.skin;

import java.io.File;
import java.util.List;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DPoint;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class WeaponSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;

    public WeaponSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/gun.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), V3DColor.fromI3d(object.getShip().getOwner().getColor())));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        
        
        double range = 1000;
        List<BalisticWeaponCapacity> balisticWeaponcapacities = object.getCapacitiesByClass(BalisticWeaponCapacity.class);
        
        if(!balisticWeaponcapacities.isEmpty()) {
            BalisticWeaponCapacity gunCapacity = (BalisticWeaponCapacity) balisticWeaponcapacities.get(0);
            range = gunCapacity.range;
        }
        
        final V3DLine line = new V3DLine();
        line.setThickness(1);
        line.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, (float) range, 0));
        elements.add(new V3DColorElement(line, new V3DColor(255, 135,  158 , 0.7f)));

        
        final V3DPoint point100 = new V3DPoint();
        point100.setSize(5);
        point100.setPosition(new V3DVect3(0, (float) range, 0));
        elements.add(new V3DColorElement(point100, new V3DColor(255, 35, 58, 0.8f)));
        
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
