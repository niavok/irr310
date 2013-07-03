// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.
package fr.def.iss.vd2.lib_v3d.demo;

import org.lwjgl.LWJGLException;

import com.irr310.i3d.scene.I3dScene;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DCircle;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DRectangle;

/**
 *
 * @author pgesta
 */
public class BasicSceneDemo {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple3DCamera activeCamera;
    V3DCanvas canvas;

    public BasicSceneDemo() {

generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

       new BasicSceneDemo();


    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple3DCamera();
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);


        // Add zoom and pane camera controlleur
        V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(activeCamera);
        activeCamera.addController(cameraController);
        //cameraController.setLimitBound(false);


        activeCamera.setScene(generateScene());

        
        //activeCamera.setShowCenter(true);
        
        activeCamera.fitAll();

        canvas.addCamera(fullscreenBinding);

        canvas.setEnabled(true);

        canvas.setShowFps(true);
        
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private I3dScene generateScene() {
        I3dScene scene = new I3dScene();


        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setQuality(64);
        circle.setSize(5);
        circle.setPosition(2, 3, 0);

        scene.add(new V3DColorElement(circle, V3DColor.green));

        //Add red rectangle
        V3DRectangle rectangle = new V3DRectangle();

        rectangle.setSize(5, 8);
        rectangle.setPosition(-2, -5, 0);
        rectangle.setRenderMode(V3DRectangle.RenderMode.PLAIN);

        scene.add(new V3DColorElement(rectangle, V3DColor.red));


        V3DBox box = new V3DBox();
        box.setSize(new V3DVect3(2, 7, 5));

        box.setPosition(2, 5, -1);

        scene.add(new V3DColorElement(box, V3DColor.blue));

        return scene;

    }
}
