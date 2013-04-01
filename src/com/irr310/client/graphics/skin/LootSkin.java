package com.irr310.client.graphics.skin;

import java.io.File;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.client.graphics.effects.AsteroidDust;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class LootSkin extends Skin {

    private V3DGroupElement elements;
    private TransformMatrix transform;
    private V3DColorElement colorElement;
    private V3DColor baseColor = new V3DColor(227, 205, 182);
    private V3DColor highlightColor = new V3DColor(0, 255, 0, 0.8f);
    private double angle = 0;
    private Time lastDustEmission;
    private final CelestialObject object;

    public LootSkin(WorldRenderer renderer, final CelestialObject object) {
        super(renderer);
        this.object = object;
        UiEngine engine = renderer.getEngine();
        elements = new V3DGroupElement(engine.getV3DContext());

        File v3drawFileStructure = new File("graphics/output/asteroid.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        elementStructure.setScale(object.getFirstPart().getShape().toV3DVect3());
        colorElement = new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), baseColor);
        elements.add(colorElement);

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
        lastDustEmission = Time.getGameTime();
    }

    @Override
    public void update() {
        elements.setTransformMatrix(transform.toFloatBuffer());

        angle += getEngine().getFramerate().getSeconds() * 10;
        float offset = (float) Math.sin(angle);
        colorElement.setColor(new V3DColor(baseColor.r * offset + highlightColor.r * (1 - offset), baseColor.g * offset + highlightColor.g
                * (1 - offset), baseColor.b * offset + highlightColor.b * (1 - offset), baseColor.a * offset + highlightColor.a * (1 - offset)));
        
        if(lastDustEmission.getDurationToNow(true).longer(new Duration(0.2f))) {
            lastDustEmission = Time.getGameTime();
            getRenderer().addElement(new AsteroidDust(getRenderer(), transform.getTranslation(), new Vec3(object.getFirstPart().getShape()).multiply(0.2) , new V3DColor(0, 155, 0, 0.5f)));
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
