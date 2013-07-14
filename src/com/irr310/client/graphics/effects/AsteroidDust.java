package com.irr310.client.graphics.effects;

import com.irr310.client.graphics.GenericGraphicalElement;
import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.Vec3;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.server.Duration;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;

public class AsteroidDust extends GenericGraphicalElement{

    private I3dGroupElement elements;
    private Duration lifeDuration = new Duration(20f);
    private Time creationTime;
    private float location = 0;

    private final UiEngine engine;
    private I3dElement element;
    private final V3DColor color;
    private final Vec3 baseSize;
    private V3DColorElement colorElement;
    private V3DColor targetColor;

    public AsteroidDust(WorldRenderer renderer, Vec3 position , Vec3 size, V3DColor color) {
        this.baseSize = size;
        this.color = color;
        this.targetColor = color.copy().setAlpha(0);
        this.engine = renderer.getEngine();


        elements = new I3dGroupElement();

        element = new V3DBox();
        element.setPosition(position.toV3DVect3());
        element.setScale(baseSize.toV3DVect3());

        colorElement = new V3DColorElement(element, color);
        elements.add(colorElement);
        creationTime = Time.now(true);
    }

    @Override
    public void update() {
        
        double mix = creationTime.getDurationToNow(true).getSeconds() / lifeDuration.getSeconds();
        
        element.setScale(baseSize.multiply(1-mix).plus(baseSize.multiply(5).multiply(mix)).toV3DVect3());
        colorElement.setColor(V3DColor.mix(color, targetColor, (float) mix));
        if (creationTime.getDurationToNow(true).longer(lifeDuration)) {
            destroy();
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

    @Override
    public I3dElement getV3DElement() {
        return elements;
    }

}
