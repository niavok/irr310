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
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.i3d.utils.I3dColor;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple2DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DCircle;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DPoint;

/**
 *
 * @author pgesta
 */
public class Shapes2DDemo  {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple2DCamera activeCamera;
    V3DCanvas canvas;
    private I3dScene scene;
    private float offsetX = 0;
    private float offsetY = 0;

    public Shapes2DDemo() {

    	generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new Shapes2DDemo();
        
    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple2DCamera();
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

    private I3dScene generateScene() {
        scene = new I3dScene();

        addElement(generatePlainCircle());
        addElement(generateThickBorderCircle());
        addElement(generateLargeBorderCircle());
        addElement(generateRingCircle());
        addElement(generateColorGradiantCircle());
        addElement(generateHaloCircle());
        addElement(generateLowQualityCircle());

        offsetY += 2;
        offsetX = 0;

        addElement(generateSmallPoint());
        addElement(generateMediumPoint());
        addElement(generateLargePoint());

        offsetY += 2;
        offsetX = 0;

        addElement(generateSmallLine());
        addElement(generateMediumLine());
        addElement(generateLargeLine());
        addElement(generateStrippleLine());

        return scene;
    }

    private void addElement(I3dElement element) {

        I3dColor color = I3dColor.randomLightOpaqueColor();

        element.setPosition(offsetY, offsetX, 0);
        offsetX += 2;
        scene.add(new V3DColorElement(element, color));
    }

    private I3dElement generatePlainCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setQuality(64);
        circle.setSize(0.8f);
        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        return circle;
    }

    private I3dElement generateThickBorderCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setRenderMode(V3DCircle.RenderMode.SOLID);
        circle.setQuality(16);
        circle.setSize(0.8f);
        circle.setThickness(1);
        return circle;
    }

    private I3dElement generateLargeBorderCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setRenderMode(V3DCircle.RenderMode.SOLID);
        circle.setQuality(16);
        circle.setSize(0.8f);
        circle.setThickness(10);
        return circle;
    }

    private I3dElement generateRingCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        circle.setInnerRadius(0.2f);
        circle.setQuality(64);
        circle.setSize(0.8f);
        return circle;
    }

    private I3dElement generateColorGradiantCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        circle.setInnerRadius(0.4f);
        circle.setColors(I3dColor.emerald, I3dColor.mauve);
        circle.setQuality(64);
        circle.setSize(0.8f);
        return circle;
    }

    private I3dElement generateHaloCircle() {

        I3dColor color = I3dColor.randomLightOpaqueColor();
        //Add green circle
        V3DCircle iCircle = new V3DCircle();

        iCircle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        iCircle.setInnerRadius(0.4f);
        iCircle.setColors(color, I3dColor.transparent);
        iCircle.setQuality(64);
        iCircle.setSize(0.8f);

        V3DCircle oCircle = new V3DCircle();

        oCircle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        oCircle.setColors(color, color);
        oCircle.setQuality(64);
        oCircle.setSize(0.4f);

        I3dGroupElement group = new I3dGroupElement();
        group.add(iCircle);
        group.add(oCircle);

        return group;
    }

    private I3dElement generateLowQualityCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle();

        circle.setQuality(10);
        circle.setSize(0.8f);
        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        return circle;
    }

    private I3dElement generateSmallPoint() {
        //Add green circle
        V3DPoint point = new V3DPoint();
        point.setSize(1);

        return point;
    }

    private I3dElement generateMediumPoint() {
        //Add green circle
        V3DPoint point = new V3DPoint();
        point.setSize(5);

        return point;
    }

    private I3dElement generateLargePoint() {
        //Add green circle
        V3DPoint point = new V3DPoint();
        point.setSize(30);

        return point;
    }

    private I3dElement generateSmallLine() {
        //Add green circle
        V3DLine line = new V3DLine();
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(1);

        return line;
    }

    private I3dElement generateMediumLine() {
        //Add green circle
        V3DLine line = new V3DLine();
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(3);

        return line;
    }

    private I3dElement generateLargeLine() {
        //Add green circle
        V3DLine line = new V3DLine();
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(10);

        return line;
    }

    private I3dElement generateStrippleLine() {
        //Add green circle
        V3DLine line = new V3DLine();
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(2);
        line.setStipple(1, (short) 5658);

        return line;
    }
}
