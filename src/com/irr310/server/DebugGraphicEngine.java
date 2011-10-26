package com.irr310.server;

import org.lwjgl.opengl.Display;

import com.irr310.server.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.event.WorldObjectAddedEvent;
import com.irr310.server.event.WorldShipAddedEvent;
import com.irr310.server.game.world.Component;
import com.irr310.server.game.world.Part;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.WorldObject;

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

public class DebugGraphicEngine extends Engine {

	final V3DContext context = new V3DContext();
	V3DCameraBinding fullscreenBinding;
	V3DSimple3DCamera activeCamera;
	V3DCanvas canvas;
	private V3DScene scene;

	public DebugGraphicEngine() {
		framerate = new Duration(16666666);
	}

	@Override
	protected void init() {
		canvas = new V3DCanvas(context, 1024, 768);

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

		// activeCamera.fitAll();

		activeCamera.fit(new V3DVect3(0, 0, 0), new V3DVect3(5, 5, 5));

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
		for(Component component : ship.getComponents()) {
			addObject(component);
		}
	}
	
	protected void addObject(final WorldObject object) {
		for(final Part part: object.getParts()) {
		

			final V3DBox box = new V3DBox(context);
			box.setRenderMode(RenderMode.SOLID);

			TransformMatrix transform = part.getTransform();
			box.setTransformMatrix(transform.toFloatBuffer());

			box.setSize(part.getShape().toV3DVect3());
			scene.add(new V3DColorElement(box, V3DColor.red));

			/*
			 * position.addListener(new Vect3ChangeListener() {
			 * 
			 * @Override public void valueChanged() {
			 * box.setPosition(object.getPosition().toV3DVect3()); } });
			 */

			transform.addListener(new TransformMatrixChangeListener() {

				@Override
				public void valueChanged() {
					box.setTransformMatrix(part.getTransform()
							.toFloatBuffer());
				}
			});

		}
	}

	@Override
	protected void frame() {
		// activeCamera.fit(new V3DVect3(0,0,0), new V3DVect3(15,15,15));
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
			isRunning = false;
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
			addObject(event.getObject());
		}
		
		@Override
		public void visit(WorldShipAddedEvent event) {
			addShip(event.getShip());
		}

	}

}
