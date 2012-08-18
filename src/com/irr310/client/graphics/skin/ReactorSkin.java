package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.AsteroidDust;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DColor;
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
    private final Component object;
    private Time lastDustEmission;

    public ReactorSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        this.object = object;
        GraphicEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

        // stator
        File v3drawFileStator = new File("graphics/output/reactor_stator.v3draw");
        final V3DrawElement elementStator = V3DrawElement.LoadFromFile(v3drawFileStator, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStator, "propeller"), new V3DColor(108, 0, 0)));

        // rotor
        File v3drawFileRotor = new File("graphics/output/reactor_rotor.v3draw");
        elementRotor = V3DrawElement.LoadFromFile(v3drawFileRotor, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementRotor, "propeller"), new V3DColor(135, 158, 169)));

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        // Configure animation
        linearEngineCapacity = object.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        angle = 0;
        speed = 100f;
        lastDustEmission = Time.getGameTime();
    }
  
    @Override
    public V3DElement getV3DElement() {
        return elements;
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        double t =  linearEngineCapacity.getCurrentThrust();
        double s = 0;
        if(t > 0) {
            s = Math.sqrt(t);
        } else {
            s = - Math.sqrt(-t);
        }
        angle += getEngine().getFramerate().getSeconds() * speed * s;
        angle = angle % 360f;
        
        elementRotor.setRotation(0, angle, 0);
     
        if(object.isBroken()) {
            elements.setVisible(false);
        } else {
            elements.setVisible(true);
            double durablility = object.getDurabilityRatio();
            if(durablility < 1.0) {
            
                if(lastDustEmission.getTimeToNow(true).longer(new Duration(0.05f))) {
                    lastDustEmission = Time.getGameTime();
                    getRenderer().addElement(new AsteroidDust(getRenderer(), transform.getTranslation(), new Vec3(object.getFirstPart().getShape()).multiply(0.4 * 0.5 + 0.5 * (1-durablility)) , new V3DColor((float)durablility, (float)durablility, (float)durablility,(float)(1.0 - durablility/3.0))));
                }
            
            }
            
        }
        
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
