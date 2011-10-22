package com.irr310.server;

import org.lwjgl.opengl.Display;

import com.irr310.server.event.AddWorldObjectEvent;
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.InitEngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.event.UseScriptEvent;
import com.irr310.server.event.WorldObjectAddedEvent;
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
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;


public class DebugGraphicEngine extends Engine {

	final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple3DCamera activeCamera;
    V3DCanvas canvas;
	private V3DScene scene;
	
	public DebugGraphicEngine() {
	}
	
	@Override
	protected void init() {
		canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple3DCamera(context);
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);


        // Add zoom and pane camera controlleur
        V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(activeCamera);
        activeCamera.addController(cameraController);
        //cameraController.setLimitBound(false);

        scene = new V3DScene(context);
        activeCamera.setScene(scene);
        
       // Add reference
        V3DLine xAxis = new V3DLine(context);
    	xAxis.setLocation(new V3DVect3(0,0,0), new V3DVect3(1,0,0));
    	
    	V3DLine yAxis = new V3DLine(context);
    	yAxis.setLocation(new V3DVect3(0,0,0), new V3DVect3(0,1,0));
    	
    	V3DLine zAxis = new V3DLine(context);
    	zAxis.setLocation(new V3DVect3(0,0,0), new V3DVect3(0,0,1));
        
        scene.add(new V3DColorElement(xAxis, V3DColor.red));
        scene.add(new V3DColorElement(yAxis, V3DColor.green));
        scene.add(new V3DColorElement(zAxis, V3DColor.blue));

        
        //activeCamera.setShowCenter(true);
        
        activeCamera.fitAll();

        canvas.addCamera(fullscreenBinding);

        canvas.setEnabled(true);

        canvas.setShowFps(true);
		
	}
	
	@Override
	protected void end() {
		Display.destroy();
	}
	
	protected void addObject(WorldObject object) {
		V3DBox box = new V3DBox(context);
		box.setPosition(object.getPosition().toV3DVect3());
		box.setSize(object.getShape().getSize().toV3DVect3());
		scene.add(new V3DColorElement(box, V3DColor.red));
	}
	
	@Override
	protected void frame() {
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
		
		
		
		
	}

}
