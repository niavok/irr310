package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.AsteroidDust;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class AsteroidSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;
    private final WorldRenderer renderer;
    private Time lastDustEmission;
    private final CelestialObject object;

    public AsteroidSkin(WorldRenderer renderer, final CelestialObject object) {
        super(renderer);
        this.renderer = renderer;
        this.object = object;
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/asteroid.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elementStructure.setScale(object.getFirstPart().getShape().toV3DVect3());
        elements.add(new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), new V3DColor(227, 205, 182)));
        
        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
        lastDustEmission = Time.getGameTime();
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());
        
        if(lastDustEmission.getDurationToNow(true).longer(new Duration(0.5f))) {
            lastDustEmission = Time.getGameTime();
            renderer.addElement(new AsteroidDust(renderer, transform.getTranslation(), new Vec3(object.getFirstPart().getShape()).multiply(0.2) , new V3DColor(127, 105, 82,0.3f)));
        }
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
