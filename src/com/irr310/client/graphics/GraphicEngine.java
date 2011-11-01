package com.irr310.client.graphics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.Display;

import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldObjectAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DBox.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class GraphicEngine extends FramerateEngine {

	final V3DContext context = new V3DContext();
	V3DCameraBinding fullscreenBinding;
	V3DSimple3DCamera activeCamera;
	V3DCanvas canvas;
	private V3DScene scene;
    private V3DGroupElement fitOrder;
    private List<Pair<LinearEngineCapacity, V3DLine>> thrustLines;
    
	public GraphicEngine() {
		framerate = new Duration(16666666);
		thrustLines = new ArrayList<Pair<LinearEngineCapacity, V3DLine>>();

	}

	@Override
	protected void init() {
		canvas = new V3DCanvas(context, 1024, 768);

		fitOrder = null;
		
		activeCamera = new V3DSimple3DCamera(context);
		fullscreenBinding = V3DCameraBinding
				.buildFullscreenCamera(activeCamera);
		activeCamera.setBackgroundColor(V3DColor.white);

		// Add zoom and pane camera controlleur
		V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(
				activeCamera);
		activeCamera.addController(cameraController);
		// cameraController.setLimitBound(false);

		scene = new V3DScene(context);
		activeCamera.setScene(scene);

		// Add reference
		V3DElement ref0 = generateReference();
		ref0.setPosition(0, 0, 0);
		V3DElement ref1 = generateReference();
		ref1.setPosition(1, 0, 0);
		V3DElement ref2 = generateReference();
		ref2.setPosition(2, 0, 0);

		scene.add(ref0);
		scene.add(ref1);
		scene.add(ref2);

		// activeCamera.setShowCenter(true);

		activeCamera.fitAll();

		//activeCamera.fit(new V3DVect3(0, 0, 0), new V3DVect3(5, 5, 5));

		canvas.addCamera(fullscreenBinding);
		

		canvas.setEnabled(true);

		canvas.setShowFps(true);

	}

	private V3DElement generateReference() {
		V3DLine xAxis = new V3DLine(context);
		xAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(1, 0, 0));

		V3DLine yAxis = new V3DLine(context);
		yAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 1, 0));

		V3DLine zAxis = new V3DLine(context);
		zAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 1));

		V3DGroupElement group = new V3DGroupElement(context);

		group.add(new V3DColorElement(xAxis, V3DColor.red));
		group.add(new V3DColorElement(yAxis, V3DColor.green));
		group.add(new V3DColorElement(zAxis, V3DColor.blue));
		return group;
	}

	@Override
	protected void end() {
		Display.destroy();
	}

	protected void addShip(final Ship ship) {
	    
	    V3DGroupElement shipElements = new V3DGroupElement(context);
		for(Component component : ship.getComponents()) {
		    shipElements.add(addObject(component, true));
		    
		    for (Capacity capacity : component.getCapacities()) {
                if (capacity instanceof LinearEngineCapacity) {

                    V3DLine thrustLine = new V3DLine(context);
                    thrustLine.setThickness(3);
                    thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));

                    final V3DColorElement group = new V3DColorElement(thrustLine, V3DColor.fushia);

                    scene.add(group);

                    final Part part = component.getFirstPart();
                    part.getTransform().addListener(new TransformMatrixChangeListener() {

                        @Override
                        public void valueChanged() {
                            group.setTransformMatrix(part.getTransform().toFloatBuffer());
                        }
                    });

                    thrustLines.add(new ImmutablePair<LinearEngineCapacity, V3DLine>((LinearEngineCapacity) capacity, thrustLine));
                }

            }
		    
		}
		
		fitOrder = shipElements;
		
	}
	
	protected V3DElement addObject(final WorldObject object, boolean inShip) {
	    
	    V3DGroupElement elements = new V3DGroupElement(context);
		for(final Part part: object.getParts()) {
		

			final V3DBox box = new V3DBox(context);
			box.setRenderMode(RenderMode.SOLID);

			TransformMatrix transform = part.getTransform();
			box.setTransformMatrix(transform.toFloatBuffer());

			box.setSize(part.getShape().toV3DVect3());
			
			V3DElement element;
			
			if(inShip) {
			    element = new V3DColorElement(box, V3DColor.blue);
			} else {
			    element = new V3DColorElement(box, V3DColor.red);
			}

			
			scene.add(element);

			transform.addListener(new TransformMatrixChangeListener() {

				@Override
				public void valueChanged() {
					box.setTransformMatrix(part.getTransform()
							.toFloatBuffer());
				}
			});
			elements.add(element);
			

		}
		
		return elements;
	}

	@Override
	protected void frame() {
	    if(fitOrder == null) {
	        activeCamera.fitAll();
	    } else {
	        activeCamera.fit(fitOrder.getBoundingBox());
	    }
	    
	    // Apply forces
        for (Pair<LinearEngineCapacity, V3DLine> thrustLinePair : thrustLines) {
            V3DLine thrustLine = thrustLinePair.getRight();
            LinearEngineCapacity engine = thrustLinePair.getLeft();

            thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, - (float) engine.getCurrentThrust(), 0));
        }
	    
		canvas.frame();

	}

	@Override
	protected void processEvent(EngineEvent e) {
		e.accept(new DebugGraphicEngineEventVisitor());
	}

	private final class DebugGraphicEngineEventVisitor extends
			DefaultEngineEventVisitor {
		@Override
		public void visit(QuitGameEvent event) {
			System.out.println("stopping debug graphic engine");
			setRunning(false);
		}

		@Override
		public void visit(StartEngineEvent event) {
			pause(false);
		}

		@Override
		public void visit(PauseEngineEvent event) {
			pause(true);
		}

		@Override
		public void visit(WorldObjectAddedEvent event) {
			addObject(event.getObject(), false);
		}
		
		@Override
		public void visit(WorldShipAddedEvent event) {
			addShip(event.getShip());
		}

	}

}
