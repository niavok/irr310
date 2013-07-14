package com.irr310.client.graphics.skin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.state.ComponentState;
import com.irr310.common.world.state.PartState;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.SystemObject;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DBox.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class GenericSkin extends Skin {

    private final ComponentState component;
    private final Map<PartState, I3dElement> elementsMap = new HashMap<PartState, I3dElement>();
    private final Map<PartState, V3DLine> speedLineMap = new HashMap<PartState, V3DLine>();
    private I3dGroupElement elements;

    public GenericSkin(ComponentState component) {
//        UiEngine engine = renderer.getEngine();
        this.component = component;
        elements = new I3dGroupElement();
        
        for (final PartState part : component.parts) {

                final V3DBox box = new V3DBox();
                box.setRenderMode(RenderMode.SOLID);

                TransformMatrix transform = part.transform;
                //box.setTransformMatrix(transform.toFloatBuffer());

                box.setSize(part.shape.toV3DVect3());


                I3dGroupElement element = new I3dGroupElement();
                //if (inShip) {
                element.add(new V3DColorElement(box, new V3DColor(0f, 0f, 1f )));
                /*} else {
                    element = new V3DColorElement(box, V3DColor.red);
                }*/
                    
                final V3DBox max = new V3DBox();
                max.setRenderMode(RenderMode.PLAIN);
                max.setPosition(part.shape.toV3DVect3().divideBy(2));
                max.setSize(new V3DVect3(0.1f, 0.1f, 0.1f));
                
                element.add(new V3DColorElement(max, V3DColor.red));
                
                
                V3DLine speedLine = new V3DLine();
                speedLine.setThickness(3);
                speedLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));

                
                elements.add(new V3DColorElement(speedLine, V3DColor.emerald));
                elements.add(element);
                elementsMap.put(part, element);
                speedLineMap.put(part, speedLine);
        }
    }


    @Override
    public void update() {
        for (Entry<PartState, I3dElement> entry : elementsMap.entrySet()) {
            entry.getValue().setTransformMatrix(entry.getKey().transform.toFloatBuffer());
        }
        
        for (Entry<PartState, V3DLine> entry : speedLineMap.entrySet()) {
            entry.getValue().setPosition(entry.getKey().transform.getTranslation().toV3DVect3());
            entry.getValue().setLocation(new V3DVect3(0, 0, 0), entry.getKey().linearSpeed.toV3DVect3());
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
