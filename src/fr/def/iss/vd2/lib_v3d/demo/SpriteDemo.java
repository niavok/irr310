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

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.lwjgl.LWJGLException;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple2DCameraController;
import fr.def.iss.vd2.lib_v3d.element.TextureManager;
import fr.def.iss.vd2.lib_v3d.element.V3DAbsoluteSizeElement;
import fr.def.iss.vd2.lib_v3d.element.V3DSprite;

/**
 *
 * @author pgesta
 */
public class SpriteDemo {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple2DCamera activeCamera;
    V3DCanvas canvas;
    
    public SpriteDemo() {

    	generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new SpriteDemo();


    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple2DCamera(context);
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);


        // Add zoom and pane camera controlleur
        V3DSimple2DCameraController cameraController = new V3DSimple2DCameraController(activeCamera);
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

    private V3DScene generateScene() {
        V3DScene scene = new V3DScene(context);


        try {
            BufferedImage image = TextureManager.LoadImage("graphics/texture1.png");
            V3DSprite sprite1 = new V3DSprite(context, image);
            scene.add(sprite1);
        } catch (IOException ex) {
            System.err.println("File 'graphics/texture1.png' is not available");
        }

        try {
            BufferedImage image = TextureManager.LoadImage("graphics/texture2.png");
            V3DSprite sprite2 = new V3DSprite(context, image);
            //The sprite will always have a size of 200px
            V3DAbsoluteSizeElement absolute = new V3DAbsoluteSizeElement(sprite2, new V3DVect3(200, 200, 0));
            scene.add(absolute);
            sprite2.setPosition(0, 0, 1);
        } catch (IOException ex) {
            System.err.println("File 'graphics/texture2.png' is not available");
        }


        return scene;

    }
}
