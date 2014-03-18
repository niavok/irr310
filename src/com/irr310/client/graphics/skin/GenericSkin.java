package com.irr310.client.graphics.skin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Part;
import com.irr310.common.world.system.Slot;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DBox.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class GenericSkin extends Skin {

    private final Component component;
    private final Map<Part, I3dElement> elementsMap = new HashMap<Part, I3dElement>();
    private final Map<Part, V3DLine> speedLineMap = new HashMap<Part, V3DLine>();
    private final Map<LinearEngineCapacity, V3DLine> thrustLineMap = new HashMap<LinearEngineCapacity, V3DLine>();
    private I3dGroupElement elements;

    public GenericSkin(Component component) {
//        UiEngine engine = renderer.getEngine();
        this.component = component;
        elements = new I3dGroupElement();
        
        
        
        for (final Part part : component.getParts()) {

                final V3DBox box = new V3DBox();
                box.setRenderMode(RenderMode.SOLID);

                TransformMatrix transform = part.getTransform();
                //box.setTransformMatrix(transform.toFloatBuffer());

                box.setSize(part.getShape().toV3DVect3());


                I3dGroupElement element = new I3dGroupElement();
                //if (inShip) {
                element.add(new V3DColorElement(box, new V3DColor(0f, 0f, 1f )));
                /*} else {
                    element = new V3DColorElement(box, V3DColor.red);
                }*/
                    
                final V3DBox center = new V3DBox();
                center.setRenderMode(RenderMode.PLAIN);
                center.setPosition(V3DVect3.ZERO);
                //max.setPosition();
                center.setSize(new V3DVect3(0.1f, 0.1f, 0.1f));
                
                element.add(new V3DColorElement(center, V3DColor.black));
                
                V3DVect3 halfShape = part.getShape().toV3DVect3().divideBy(2);
                V3DLine xLine = new V3DLine();
                xLine.setThickness(1);
                xLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(halfShape.x, 0, 0));
                element.add(new V3DColorElement(xLine, V3DColor.red));
                
                V3DLine yLine = new V3DLine();
                yLine.setThickness(1);
                yLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, halfShape.y, 0));
                element.add(new V3DColorElement(yLine, V3DColor.green));
                
                V3DLine zLine = new V3DLine();
                zLine.setThickness(1);
                zLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, halfShape.z));
                element.add(new V3DColorElement(zLine, V3DColor.blue));
                
        
                for(Slot slot : component.getSlots()) {
                    if(slot.getPart().equals(part)) {
                        final V3DBox slotBox = new V3DBox();
                        slotBox.setRenderMode(RenderMode.PLAIN);
                        slotBox.setPosition(slot.getPosition().toV3DVect3());
                        //max.setPosition();
                        slotBox.setSize(new V3DVect3(0.05f, 0.05f, 0.05f));
                        
                        element.add(new V3DColorElement(slotBox, V3DColor.darkblue));
                    }
                }
                
                V3DLine speedLine = new V3DLine();
                speedLine.setThickness(3);
                speedLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));

                
                
              
                elements.add(new V3DColorElement(speedLine, V3DColor.emerald));
                elements.add(element);
                elementsMap.put(part, element);
                speedLineMap.put(part, speedLine);
        }
        
        List<LinearEngineCapacity> engines = component.getCapacitiesByClass(LinearEngineCapacity.class);
        for (LinearEngineCapacity engine : engines) {
            V3DLine thrustLine = new V3DLine();
            thrustLine.setThickness(3);
            thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));
            elements.add(new V3DColorElement(thrustLine, V3DColor.magenta));
            thrustLineMap.put(engine, thrustLine);
        }
        
    }

    @Override
    public void init(Timestamp time) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(Timestamp time) {
        for (Entry<Part, I3dElement> entry : elementsMap.entrySet()) {
            entry.getValue().setTransformMatrix(entry.getKey().getTransform().toFloatBuffer());
        }
        
        for (Entry<Part, V3DLine> entry : speedLineMap.entrySet()) {
            entry.getValue().setPosition(entry.getKey().getTransform().getTranslation().toV3DVect3());
            entry.getValue().setLocation(new V3DVect3(0, 0, 0), entry.getKey().getLinearSpeed().toV3DVect3());
        }
        
        for (Entry<LinearEngineCapacity, V3DLine> entry : thrustLineMap.entrySet()) {
            entry.getValue().setPosition(component.getParts().get(0).getTransform().getTranslation().toV3DVect3());
            entry.getValue().setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, (float) entry.getKey().getCurrentThrust(), 0));
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
