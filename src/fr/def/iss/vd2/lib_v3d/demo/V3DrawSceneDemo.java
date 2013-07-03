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

import java.io.File;

import org.lwjgl.LWJGLException;

import com.irr310.i3d.scene.I3dScene;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawReader;

/**
 *
 * @author fberto
 */
public class V3DrawSceneDemo  {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple3DCamera activeCamera;
    V3DCanvas canvas;
    private I3dScene scene;

    public V3DrawSceneDemo() {

    	generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new V3DrawSceneDemo();
    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);
        activeCamera = new V3DSimple3DCamera();
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);

        // Add zoom and pane camera controlleur
        V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(activeCamera);
        activeCamera.addController(cameraController);
        activeCamera.setScene(generateScene());

        //activeCamera.fitAll();        // Centre sur les objets avant leurs rotations !
        activeCamera.fitAllIfInvalid(); // Centre sur les objets après leurs rotations !
        activeCamera.setShowCenter(true);
        activeCamera.setBackgroundColor(V3DColor.black);

        canvas.addCamera(fullscreenBinding);
        canvas.setShowFps(true);
        canvas.setEnabled(true);
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    private I3dScene generateScene() {

        scene = new I3dScene();

        displayBuilding();
        displayGround();
        displayBoule();

        return scene;
    }

    private void displayBuilding() {
        File v3drawFile = new File("graphics/output/building.v3draw");

        V3DrawElement v3DrawElement = V3DrawElement.LoadFromFile(v3drawFile, context);

        if (v3DrawElement != null) {

            System.out.println("v3draw version: " + v3DrawElement.getV3drawReader().getVersion());
            System.out.println("v3draw metadatas: ");
            for (String metadata : v3DrawElement.getV3drawReader().getMetadataList()) {
                System.out.println("    " + metadata);
            }
            System.out.println("v3draw minX: " + v3DrawElement.getV3drawReader().getMinX());
            System.out.println("v3draw maxX: " + v3DrawElement.getV3drawReader().getMaxX());
            System.out.println("v3draw minY: " + v3DrawElement.getV3drawReader().getMinY());
            System.out.println("v3draw maxY: " + v3DrawElement.getV3drawReader().getMaxY());
            System.out.println("v3draw minZ: " + v3DrawElement.getV3drawReader().getMinZ());
            System.out.println("v3draw maxZ: " + v3DrawElement.getV3drawReader().getMaxZ());

            v3DrawElement.setRotation(0, 90, 90);
            v3DrawElement.setScale(1);

            V3DColorElement colorElement = new V3DColorElement(v3DrawElement, V3DColor.white);

            V3DrawReader v3drawReader = v3DrawElement.getV3drawReader();
            float dx = -((v3drawReader.getMaxZ() -v3drawReader.getMinZ())/2);
            float dy = -((v3drawReader.getMaxX() -v3drawReader.getMinX())/2);
            float dz = -((v3drawReader.getMaxY() -v3drawReader.getMinY())/2);

            // Offsets d'ajustement "à la main"
            colorElement.setPosition(dx +0.012f, -0.021f, -dz -0.008f);

            scene.add(colorElement);

        } else {
            System.err.println("Fail to load file");
        }
    }

    private void displayGround() {
        File v3drawFile = new File("graphics/output/ground.v3draw");

        V3DrawElement v3DrawElement = V3DrawElement.LoadFromFile(v3drawFile, context);

        if (v3DrawElement != null) {

            v3DrawElement.setRotation(0, 90, 90);
            v3DrawElement.setScale(1);

            V3DColorElement colorElement = new V3DColorElement(v3DrawElement, V3DColor.white);

            V3DrawReader v3drawReader = v3DrawElement.getV3drawReader();
            float dx = -((v3drawReader.getMaxZ() - v3drawReader.getMinZ())/2 );
            colorElement.setPosition(dx, 0, 0);
            scene.add(colorElement);

        } else {
            System.err.println("Fail to load file graphics/output/ground.v3draw");
        }
    }

    private void displayBoule() {
        File v3drawFile = new File("graphics/output/boule.v3draw");

        V3DrawElement v3DrawElement = V3DrawElement.LoadFromFile(v3drawFile, context);

        if (v3DrawElement != null) {

            v3DrawElement.setScale(0.1f);
            v3DrawElement.setEnableLighting(true);
            
            // Offsets d'ajustement "à la main"
            v3DrawElement.setPosition(0, -0.165f, 0.03f);
            scene.add(new V3DColorElement(v3DrawElement, V3DColor.red));

        } else {
            System.err.println("Fail to load file graphics/output/boule.v3draw");
        }
    }
}
