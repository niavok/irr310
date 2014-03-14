package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.AsteroidDust;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DTriangle;
import fr.def.iss.vd2.lib_v3d.element.V3DTriangle.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ReactorSkin extends Skin {

    private I3dGroupElement elements;
    private V3DrawElement elementRotor;
    private float angle;
    private LinearEngineCapacity linearEngineCapacity;
    private float speed;
    private TransformMatrix transform;
    private final Component object;
    private Time lastDustEmission;
    private V3DTriangle flame1;
    private V3DColorElement colorFlame;
    private V3DTriangle flame2;

    public ReactorSkin(WorldRenderer renderer, final Component object) {
        this.object = object;
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        // stator
        File v3drawFileStator = new File("graphics/output/reactor_stator.v3draw");
        final V3DrawElement elementStator = V3DrawElement.LoadFromFile(v3drawFileStator);
        elements.add(new V3DColorElement(new V3DShaderElement(elementStator, "propeller"), V3DColor.fromI3d(object.getShip().getOwner().getColor())));

        // rotor
        File v3drawFileRotor = new File("graphics/output/reactor_rotor.v3draw");
        elementRotor = V3DrawElement.LoadFromFile(v3drawFileRotor);
        elements.add(new V3DColorElement(new V3DShaderElement(elementRotor, "propeller"), new V3DColor(135, 158, 169)));

        // Flame
        flame1 = new V3DTriangle();
        flame1.setRenderMode(RenderMode.PLAIN);
        flame1.setSize((float)object.getFirstPart().getShape().x*0.8f, 0f);
        flame2 = new V3DTriangle();
        flame2.setRenderMode(RenderMode.PLAIN);
        flame2.setSize((float)object.getFirstPart().getShape().x*0.8f, 0f);
        flame2.setRotation(0, 90, 0);

        I3dGroupElement flames = new I3dGroupElement();
        flames.add(flame1);
        flames.add(flame2);
        colorFlame = new V3DColorElement(flames, V3DColor.transparent);
        elements.add(colorFlame);
        
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());

        
        // Configure animation
        linearEngineCapacity = object.getCapacitiesByClass(LinearEngineCapacity.class).get(0);
        angle = 0;
        speed = 100f;
        lastDustEmission = Time.getGameTime();
    }
  
    @Override
    public I3dElement getV3DElement() {
        return elements;
    }

    @Override
    public void init(Timestamp time) {
    }
    
    @Override
    public void update(Timestamp time) {
//        elements.setTransformMatrix(transform.toFloatBuffer());
//        
//        double t =  linearEngineCapacity.getCurrentThrust();
//        double s = 0;
//        if(t > 0) {
//            s = Math.sqrt(t);
//        } else {
//            s = - Math.sqrt(-t);
//        }
//        angle += getEngine().getFramerate().getSeconds() * speed * s;
//        angle = angle % 360f;
//        
//        elementRotor.setRotation(0, angle, 0);
//     
//        if(object.isBroken()) {
//            elements.setVisible(false);
//        } else {
//            elements.setVisible(true);
//            double durablility = object.getDurabilityRatio();
//            if(durablility < 1.0) {
//            
//                if(lastDustEmission.getDurationToNow(true).longer(new Duration(0.05f))) {
//                    lastDustEmission = Time.getGameTime();
//                    getRenderer().addElement(new AsteroidDust(getRenderer(), transform.getTranslation(), new Vec3(object.getFirstPart().getShape()).multiply(0.4 * 0.5 + 0.5 * (1-durablility)) , new V3DColor((float)durablility, (float)durablility, (float)durablility,(float)(1.0 - durablility/3.0))));
//                }
//            
//            }
//            
//        }
//        
//        
//        flame1.setSize((float)object.getFirstPart().getShape().x*0.7f, (float)linearEngineCapacity.getCurrentThrust());
//        flame1.setPosition(0, (float) - linearEngineCapacity.getCurrentThrust() /2, 0);
//        flame2.setSize((float)object.getFirstPart().getShape().x*0.7f, (float)linearEngineCapacity.getCurrentThrust());
//        flame2.setPosition(0, (float) - linearEngineCapacity.getCurrentThrust() /2, 0);
//        double percent = 1 - Math.abs(linearEngineCapacity.getCurrentThrust() / linearEngineCapacity.getMaxThrust());
//        colorFlame.setColor(new V3DColor((int) (108* percent + (1-percent) * 255) , (int) ( 200 * percent + (1-percent) * 180)  ,(int) (251 * percent + (1-percent) * 0) , 0.6f ));
//        
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
