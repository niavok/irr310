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
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple2DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DCircle;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
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
    private V3DScene scene;
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
        scene = new V3DScene(context);

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

    private void addElement(V3DElement element) {

        V3DColor color = V3DColor.randomLightOpaqueColor();

        element.setPosition(offsetY, offsetX, 0);
        offsetX += 2;
        scene.add(new V3DColorElement(element, color));
    }

    private V3DElement generatePlainCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setQuality(64);
        circle.setSize(0.8f);
        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        return circle;
    }

    private V3DElement generateThickBorderCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setRenderMode(V3DCircle.RenderMode.SOLID);
        circle.setQuality(16);
        circle.setSize(0.8f);
        circle.setThickness(1);
        return circle;
    }

    private V3DElement generateLargeBorderCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setRenderMode(V3DCircle.RenderMode.SOLID);
        circle.setQuality(16);
        circle.setSize(0.8f);
        circle.setThickness(10);
        return circle;
    }

    private V3DElement generateRingCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        circle.setInnerRadius(0.2f);
        circle.setQuality(64);
        circle.setSize(0.8f);
        return circle;
    }

    private V3DElement generateColorGradiantCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        circle.setInnerRadius(0.4f);
        circle.setColors(V3DColor.emerald, V3DColor.mauve);
        circle.setQuality(64);
        circle.setSize(0.8f);
        return circle;
    }

    private V3DElement generateHaloCircle() {

        V3DColor color = V3DColor.randomLightOpaqueColor();
        //Add green circle
        V3DCircle iCircle = new V3DCircle(context);

        iCircle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        iCircle.setInnerRadius(0.4f);
        iCircle.setColors(color, V3DColor.transparent);
        iCircle.setQuality(64);
        iCircle.setSize(0.8f);

        V3DCircle oCircle = new V3DCircle(context);

        oCircle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        oCircle.setColors(color, color);
        oCircle.setQuality(64);
        oCircle.setSize(0.4f);

        V3DGroupElement group = new V3DGroupElement(context);
        group.add(iCircle);
        group.add(oCircle);

        return group;
    }

    private V3DElement generateLowQualityCircle() {
        //Add green circle
        V3DCircle circle = new V3DCircle(context);

        circle.setQuality(10);
        circle.setSize(0.8f);
        circle.setRenderMode(V3DCircle.RenderMode.PLAIN);
        return circle;
    }

    private V3DElement generateSmallPoint() {
        //Add green circle
        V3DPoint point = new V3DPoint(context);
        point.setSize(1);

        return point;
    }

    private V3DElement generateMediumPoint() {
        //Add green circle
        V3DPoint point = new V3DPoint(context);
        point.setSize(5);

        return point;
    }

    private V3DElement generateLargePoint() {
        //Add green circle
        V3DPoint point = new V3DPoint(context);
        point.setSize(30);

        return point;
    }

    private V3DElement generateSmallLine() {
        //Add green circle
        V3DLine line = new V3DLine(context);
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(1);

        return line;
    }

    private V3DElement generateMediumLine() {
        //Add green circle
        V3DLine line = new V3DLine(context);
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(3);

        return line;
    }

    private V3DElement generateLargeLine() {
        //Add green circle
        V3DLine line = new V3DLine(context);
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(10);

        return line;
    }

    private V3DElement generateStrippleLine() {
        //Add green circle
        V3DLine line = new V3DLine(context);
        line.setLocation(new V3DVect3(-0.5f, -0.5f, 0), new V3DVect3(0.5f, 0.5f, 0));
        line.setThickness(2);
        line.setStipple(1, (short) 5658);

        return line;
    }
}
