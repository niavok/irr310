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
import com.irr310.i3d.utils.I3dColor;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class LootSkin extends Skin {

    private I3dGroupElement elements;
    private TransformMatrix transform;
    private V3DColorElement colorElement;
    private I3dColor baseColor = new I3dColor(227, 205, 182);
    private I3dColor highlightColor = new I3dColor(0, 255, 0, 0.8f);
    private double angle = 0;
    private Time lastDustEmission;
    private final CelestialObject object;

    public LootSkin(WorldRenderer renderer, final CelestialObject object) {
        this.object = object;
        UiEngine engine = renderer.getEngine();
        elements = new I3dGroupElement();

        File v3drawFileStructure = new File("graphics/output/asteroid.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure);
        elementStructure.setScale(object.getFirstPart().getShape().toV3DVect3());
        colorElement = new V3DColorElement(new V3DShaderElement(elementStructure, "propeller"), baseColor);
        elements.add(colorElement);

        transform = object.getFirstPart().getTransform();
        elements.setTransformMatrix(transform.toFloatBuffer());
        lastDustEmission = Time.getGameTime();
    }
    
    @Override
    public void init(Timestamp time) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(Timestamp time) {
//        elements.setTransformMatrix(transform.toFloatBuffer());
//
//        angle += getEngine().getFramerate().getSeconds() * 10;
//        float offset = (float) Math.sin(angle);
//        colorElement.setColor(new V3DColor(baseColor.r * offset + highlightColor.r * (1 - offset), baseColor.g * offset + highlightColor.g
//                * (1 - offset), baseColor.b * offset + highlightColor.b * (1 - offset), baseColor.a * offset + highlightColor.a * (1 - offset)));
//        
//        if(lastDustEmission.getDurationToNow(true).longer(new Duration(0.2f))) {
//            lastDustEmission = Time.getGameTime();
//            getRenderer().addElement(new AsteroidDust(getRenderer(), transform.getTranslation(), new Vec3(object.getFirstPart().getShape()).multiply(0.2) , new V3DColor(0, 155, 0, 0.5f)));
//        }
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
