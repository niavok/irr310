package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.LinearEngineCapacity;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ReactorSkin extends Skin {

    private V3DGroupElement elements;
    private V3DrawElement elementRotor;
    private float angle;
    private LinearEngineCapacity linearEngineCapacity;
    private float speed;
    private TransformMatrix transform;

    public ReactorSkin(V3DContext context, final Component object) {
        elements = new V3DGroupElement(context);

        // stator
        File v3drawFileStator = new File("graphics/output/reactor_stator.v3draw");
        final V3DrawElement elementStator = V3DrawElement.LoadFromFile(v3drawFileStator, context);
        elements.add(new V3DColorElement(new V3DShaderElement(elementStator, "propeller"), new V3DColor(135, 158, 255)));

        // stator
        File v3drawFileRotor = new File("graphics/output/reactor_rotor.v3draw");
        elementRotor = V3DrawElement.LoadFromFile(v3drawFileRotor, context);
        elements.add(new V3DColorElement(new V3DShaderElement(elementRotor, "propeller"), new V3DColor(135, 158, 169)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        // Configure animation
        linearEngineCapacity = (LinearEngineCapacity) object.getCapacitiesByName("linearEngine");
        angle = 0;
        speed = 50f;
    }
  
    @Override
    public V3DElement getElement() {
        return elements;
    }

    @Override
    public void animate() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        angle += framerate.getSeconds()* speed * linearEngineCapacity.getCurrentThrust();
        angle = angle % 360f;
        
        elementRotor.setRotation(0, angle, 0);
     
        
    }
    
    

}
