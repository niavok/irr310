package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.RocketSteam;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.RocketCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class RocketSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;
    private final WorldRenderer renderer;
    private Time lastDustEmission;
    private final Component object;
    private RocketCapacity rocketCapacity;
    private Vec3 lastPosition;

    public RocketSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        this.renderer = renderer;
        this.object = object;
        UiEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

        File v3drawFileStructure = new File("graphics/output/rocket_tube.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(103, 0, 7)));
        
        
        // fins
        File v3drawFileFins = new File("graphics/output/rocket_fins.v3draw");
        final V3DrawElement elementFins = V3DrawElement.LoadFromFile(v3drawFileFins, engine.getV3DContext());
        elements.add(new V3DColorElement(new V3DShaderElement(elementFins, "propeller"), new V3DColor(103, 103, 103)));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
        lastDustEmission = Time.getGameTime();
        
        rocketCapacity = object.getCapacitiesByClass(RocketCapacity.class).get(0);
        
        lastPosition = null;
        
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        if(rocketCapacity.currentThrust > 0 && lastDustEmission.getTimeToNow(true).longer(new Duration(0.001f))) {
            lastDustEmission = Time.getGameTime();
            
            if(lastPosition != null) {
                renderer.addElement(new RocketSteam(renderer, transform.getTranslation(), transform.getTranslation().diff(lastPosition) , new V3DColor(255, 255, 255,0.8f), 10, 1));
            }
            
            if(transform.getTranslation().length() > 0) {
                lastPosition = transform.getTranslation();
            }
        }
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
