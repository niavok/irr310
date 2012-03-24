package com.irr310.client.graphics.skin;

import java.io.File;

import javax.swing.Renderer;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.AsteroidDust;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;
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

    public RocketSkin(WorldRenderer renderer, final Component object) {
        super(renderer);
        this.renderer = renderer;
        this.object = object;
        GraphicEngine engine = renderer.getEngine();
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
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        if(lastDustEmission.getTimeToNow(true).longer(new Duration(0.001f))) {
            lastDustEmission = Time.getGameTime();
            renderer.addElement(new AsteroidDust(renderer, transform.getTranslation(), new Vec3(new Vec3(0.1, 0.1, 0.1)) , new V3DColor(255, 255, 255,0.6f)));
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
