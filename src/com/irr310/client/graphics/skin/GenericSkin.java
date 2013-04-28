package com.irr310.client.graphics.skin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.graphics.WorldRenderer;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.SystemObject;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DBox.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class GenericSkin extends Skin {

    private final SystemObject object;
    private final Map<Part, V3DElement> elementsMap = new HashMap<Part, V3DElement>();
    private final Map<Part, V3DLine> speedLineMap = new HashMap<Part, V3DLine>();
    private V3DGroupElement elements;

    public GenericSkin(WorldRenderer renderer, SystemObject object) {
        super(renderer);
        UiEngine engine = renderer.getEngine();
        this.object = object;
        elements = new V3DGroupElement(engine.getV3DContext());
        
        for (final Part part : object.getParts()) {

                final V3DBox box = new V3DBox(engine.getV3DContext());
                box.setRenderMode(RenderMode.SOLID);

                TransformMatrix transform = part.getTransform();
                //box.setTransformMatrix(transform.toFloatBuffer());

                box.setSize(part.getShape().toV3DVect3());


                V3DGroupElement element = new V3DGroupElement(engine.getV3DContext());
                //if (inShip) {
                element.add(new V3DColorElement(box, new V3DColor(0f, 0f, 1f )));
                /*} else {
                    element = new V3DColorElement(box, V3DColor.red);
                }*/
                    
                final V3DBox max = new V3DBox(engine.getV3DContext());
                max.setRenderMode(RenderMode.PLAIN);
                max.setPosition(part.getShape().toV3DVect3().divideBy(2));
                max.setSize(new V3DVect3(0.1f, 0.1f, 0.1f));
                
                element.add(new V3DColorElement(max, V3DColor.red));
                
                
                V3DLine speedLine = new V3DLine(engine.getV3DContext());
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
        for (Entry<Part, V3DElement> entry : elementsMap.entrySet()) {
            entry.getValue().setTransformMatrix(entry.getKey().getTransform().toFloatBuffer());
        }
        
        for (Entry<Part, V3DLine> entry : speedLineMap.entrySet()) {
            entry.getValue().setPosition(entry.getKey().getTransform().getTranslation().toV3DVect3());
            entry.getValue().setLocation(new V3DVect3(0, 0, 0), entry.getKey().getLinearSpeed().toV3DVect3());
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
