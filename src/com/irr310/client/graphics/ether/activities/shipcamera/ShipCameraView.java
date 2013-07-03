package com.irr310.client.graphics.ether.activities.shipcamera;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.client.graphics.GraphicalElement;
import com.irr310.common.world.state.ShipState;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.scene.I3dEye3DCamera;
import com.irr310.i3d.scene.I3dScene;
import com.irr310.i3d.scene.controller.I3dFollow3DCameraController;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;

public class ShipCameraView extends View {

    private ShipState focusedShip;
    private I3dEye3DCamera activeCamera;
    private I3dFollow3DCameraController cameraController;
    private List<GraphicalElement> animatedList = new CopyOnWriteArrayList<GraphicalElement>();
    private V3DCameraBinding fullscreenBinding;
    private I3dScene scene;
    
    
    public ShipCameraView(ShipState ship) {
        this.focusedShip = ship;
        
        init();
        
    }

    

    @Override
    public void onDraw(Graphics g) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMeasure() {
        // TODO Auto-generated method stub

    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

private void init() {
        
        
        activeCamera = new I3dEye3DCamera();

        cameraController = new I3dFollow3DCameraController(activeCamera);
        configureDefaultCamera();
        animatedList.add(cameraController);

        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);
        activeCamera.setBackgroundColor(V3DColor.white);

        // Add zoom and pane camera controlleur
        // cameraController.setLimitBound(false);

        scene = new I3dScene();
        activeCamera.setScene(scene);

        /*
         * V3DBox sky = new V3DBox(context); sky.setSize(new V3DVect3(1024, 768,
         * 1)); sky.setPosition(1024 / 2, 768 / 2, 0);
         */

        // activeCamera.getBackgroundScene().add(new V3DColorElement(sky,
        // V3DColor.pink));
        // activeCamera.getBackgroundScene().add(new V3DColorElement(new
        // Sky(context, cameraController), V3DColor.pink));

        createBubble();

        // Add reference
        I3dElement ref0 = generateReference();
        ref0.setPosition(0, 0, 0);
        I3dElement ref1 = generateReference();
        ref1.setPosition(1, 0, 0);
        I3dElement ref2 = generateReference();
        ref2.setPosition(2, 0, 0);

        scene.add(ref0);
        scene.add(ref1);
        scene.add(ref2);

//        generateGuiStructure();

//        loadCurrentWorld();

        // activeCamera.setShowCenter(true);

        activeCamera.fitAll();        
    }
    
    private void configureDefaultCamera() {
        cameraController.configure(500,-2,-30, 2);
    }
    
    private void createBubble() {

//      File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
//      bubbleElement = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
//      // elementStructure.setShader("bubble");
//      bubbleElement.setScale((float)Game.getInstance().getWorld().getWorldSize());
//
//      V3DShader shader = new V3DShader("bubble") {
//          private int resolution;
//          private int time;
//          private long startTime;
//
//          protected void loadUniforms() {
//              resolution = ARBShaderObjects.glGetUniformLocationARB(shader, "resolution");
//              time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
//
//              startTime = new Date().getTime();
//          };
//
//          protected void setUniforms() {
//              // V3DVect3 rotation = camera.getRotation();
//              // ARBShaderObjects.glUniform3fARB(inputRotation, rotation.x,
//              // rotation.y, rotation.z);
//              ARBShaderObjects.glUniform2fARB(resolution, cameraController.getCamera().getCurrentWidth(), cameraController.getCamera()
//                                                                                                                          .getCurrentHeight());
//              float time2 = ((float) (new Date().getTime() - startTime)) / 10000.0f;
//              ARBShaderObjects.glUniform1fARB(time, time2);
//
//          }
//      };
//
//      scene.add(new V3DColorElement(new V3DShaderElement(bubbleElement, shader), new V3DColor(255, 255, 255)));
  }
    
    private I3dElement generateReference() {
        V3DLine xAxis = new V3DLine();
        xAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(1, 0, 0));

        V3DLine yAxis = new V3DLine();
        yAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 1, 0));

        V3DLine zAxis = new V3DLine();
        zAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 1));

        I3dGroupElement group = new I3dGroupElement();

        group.add(new V3DColorElement(xAxis, V3DColor.red));
        group.add(new V3DColorElement(yAxis, V3DColor.green));
        group.add(new V3DColorElement(zAxis, V3DColor.blue));
        return group;
    }
}
