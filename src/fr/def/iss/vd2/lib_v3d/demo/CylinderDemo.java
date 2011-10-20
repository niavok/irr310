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

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DCircle;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DCylinder;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DTube;

/**
 *
 * @author pgesta
 */
public class CylinderDemo {

    public static float UN = 1F;      // Constante sinus 90°
    public static float R3S2 = 0.866F;  // Constante sinus 60° : racine de 3 / 2
    public static float R2S2 = 0.7071F; // Constante sinus 45° : racine de 2 / 2
    public static float DEMI = 0.5F;    // Constante sinus 30° : 1 / 2
    public static float ZERO = 0F;      // Constante sinus 0°
    public static float SPAG = 6F;      // Espacement entre élements de la scène
    final V3DContext context = new V3DContext();
    V3DCameraBinding viewport;
    V3DSimple3DCamera camera;
    V3DCanvas canvas;
    V3DScene scene;

    public CylinderDemo() {
        generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

         new CylinderDemo();
      
    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);
        camera = new V3DSimple3DCamera(context);

        viewport = V3DCameraBinding.buildFullscreenCamera(camera);
        scene = generateScene();

        V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(camera);
        camera.addController(cameraController);
        camera.setScene(scene);
        camera.fit(new V3DVect3(0, 0, 0), new V3DVect3(24, 0, 0));

        canvas.addCamera(viewport);
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

        V3DElement element = null;
        V3DScene scene = new V3DScene(context);

        element = getCircle();
        // element.setPosition(-R3S2 * SPAG, -DEMI * SPAG, 0);
        element.setPosition(-9, 0, 0);
        scene.add(new V3DColorElement(element, V3DColor.emerald));

        element = getTube();
        // element.setPosition(-R3S2 * SPAG, DEMI * SPAG, 0);
        element.setPosition(-3, 0, 0);
        scene.add(new V3DColorElement(element, V3DColor.fushia));

        element = getRoundBox();
        // element.setPosition(-R3S2 * SPAG, DEMI * SPAG, 0);
        element.setPosition(3 , 0, 0);
        scene.add(new V3DColorElement(element, V3DColor.lavander));

        element = getCylinder();
        // element.setPosition(-R3S2 * SPAG, DEMI * SPAG, 0);
        element.setPosition(9 , 0, 0);
        scene.add(new V3DColorElement(element, V3DColor.mauve));

        return scene;
    }

    private V3DCircle getCircle() {

        V3DCircle circle = new V3DCircle(context);
        // V3DColor color = V3DColor.randomLightOpaqueColor(); fadetoblack
        // circle.setRenderMode(V3DCircle.RenderMode.SOLID); // Mode Fil de fer
        // circle.setColors(V3DColor.emerald, V3DColor.fadetoblack);
        // circle.setColors(V3DColor.fadetoblack, V3DColor.emerald);
        circle.setQuality(64);          // Nombre de segments d'émulation
        circle.setSize(2);              // Rayon du cercle externe
        circle.setInnerRadius(0.4f); // Rayon du cercle interne
        // circle.setThickness(1); // Epaisseur trait périmètre mode Fil de fer
        return circle;
    }

    private V3DTube getTube() {

        V3DTube tube = new V3DTube(context, 2, 2);
        return tube;
    }

    private V3DCylinder getRoundBox() {

        V3DCylinder cylinder = new V3DCylinder(context, 2, 2, 16);
        cylinder.setShowTop(false);
        return cylinder;
    }

        private V3DCylinder getCylinder() {
        V3DCylinder cylinder = new V3DCylinder(context, 2, 2, 32);
        return cylinder;
    }
}

