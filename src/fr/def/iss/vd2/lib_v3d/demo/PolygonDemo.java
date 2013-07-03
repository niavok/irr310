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

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;

import com.irr310.i3d.scene.I3dScene;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DPolygon;
import fr.def.iss.vd2.lib_v3d.element.V3DPolygonBox;
import fr.def.iss.vd2.lib_v3d.element.V3DPolygonWalls;

/**
 *
 * @author pgesta
 */
public class PolygonDemo {

    public static float UN   = 1F;      // Constante sinus 90°
    public static float R3S2 = 0.866F;  // Constante sinus 60° : racine de 3 / 2
    public static float R2S2 = 0.7071F; // Constante sinus 45° : racine de 2 / 2
    public static float DEMI = 0.5F;    // Constante sinus 30° : 1 / 2
    public static float ZERO = 0F;      // Constante sinus 0°
    public static float SPAG = 6F;      // Espacement entre élements de la scène

    final V3DContext context = new V3DContext();
    V3DCameraBinding viewport;
    V3DSimple3DCamera camera;
    V3DCanvas canvas;
    I3dScene scene;

    public PolygonDemo() {
        
        generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new PolygonDemo();
    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);
        camera = new V3DSimple3DCamera();
        viewport = V3DCameraBinding.buildFullscreenCamera(camera);
        scene = generateScene();

        V3DSimple3DCameraController cameraController = new V3DSimple3DCameraController(camera);
        camera.addController(cameraController);
        camera.setShowCenter(true); // Landmark axis and image center show
        camera.setScene(scene);
        camera.fit(new V3DVect3(0, 0, 0), new V3DVect3(18, 18, 0));

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

    private I3dScene generateScene() {

        I3dElement element = null;
        I3dScene scene = new I3dScene();

        element = getFlatHexaGon();
        element.setPosition(-R3S2*SPAG, -DEMI*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.violet));

        element = getWallHexaGon();
        element.setPosition(-R3S2*SPAG, DEMI*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.lavander));

        element = getBoxHexaGonNoTop();
        element.setPosition(ZERO*SPAG, UN*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.lilas));

        element = getBoxHexaGon();
        element.setPosition(R3S2*SPAG, DEMI*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.fushia));

        element = getHollowHexaGon();
        element.setPosition(R3S2*SPAG, -DEMI*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.emerald));

        element = getStarPolyGon();
        element.setPosition(ZERO*SPAG, -UN*SPAG, 0);
        scene.add(new V3DColorElement(element, V3DColor.mauve));
        
        return scene;
    }

    private V3DPolygon getFlatHexaGon() {

        V3DPolygon polygon = new V3DPolygon();
        polygon.setPointList(getHexaGonPointList());
        return polygon;
    }

    private V3DPolygonWalls getWallHexaGon() {

        V3DPolygonWalls polygon = new V3DPolygonWalls();
        polygon.setHeight(1);
        polygon.setPointList(getHexaGonPointList());
        return polygon;
    }

    private V3DPolygonBox getBoxHexaGonNoTop() {

        V3DPolygonBox polygon = new V3DPolygonBox();
        polygon.setHeight(1);
        polygon.setPointList(getHexaGonPointList(), false);
        polygon.setShowTop(false);
        return polygon;
    }

        private V3DPolygonBox getBoxHexaGon() {

        V3DPolygonBox polygon = new V3DPolygonBox();
        polygon.setHeight(2);
        polygon.setPointList(getHexaGonPointList(), false);
        return polygon;
    }

    private V3DPolygonBox getHollowHexaGon() {

        V3DPolygonBox polygon = new V3DPolygonBox();
        polygon.setHeight(1);
        polygon.setPointList(getHexaGonPointList(), false);
        //polygon.addPointList(getTrianglePointList(), true);
        //polygon.addPointList(getTriang1ePointList(), true);
        polygon.addPointList(getInnerHexagonPointList(), true);
        return polygon;
    }

    private V3DPolygonBox getStarPolyGon() {

        V3DPolygonBox polygon = new V3DPolygonBox();
        polygon.setHeight(1);
        polygon.setPointList(getInnerStarPointList(), false);
        polygon.setShowTop(false);
        return polygon;
    }

    private List<V3DVect3> getHexaGonPointList() {

        List<V3DVect3> pointsList = new ArrayList<V3DVect3>();
        pointsList.add(new V3DVect3( R3S2, DEMI, 0));
        pointsList.add(new V3DVect3( ZERO, UN,   0));
        pointsList.add(new V3DVect3(-R3S2, DEMI, 0));
        pointsList.add(new V3DVect3(-R3S2,-DEMI, 0));
        pointsList.add(new V3DVect3( ZERO,-UN,   0));
        pointsList.add(new V3DVect3( R3S2,-DEMI, 0));
        return pointsList;
    }

    @SuppressWarnings("unused")
    private List<V3DVect3> getTrianglePointList() {

        List<V3DVect3> pointsList = new ArrayList<V3DVect3>();
        pointsList.add(new V3DVect3( R3S2, DEMI, 0));
        pointsList.add(new V3DVect3(-R3S2, DEMI, 0));
        pointsList.add(new V3DVect3( ZERO,-UN,   0));
        return pointsList;
    }
        @SuppressWarnings("unused")
        private List<V3DVect3> getTriang1ePointList() {

        List<V3DVect3> pointsList = new ArrayList<V3DVect3>();
        pointsList.add(new V3DVect3( ZERO, UN,   0));
        pointsList.add(new V3DVect3(-R3S2,-DEMI, 0));
        pointsList.add(new V3DVect3( R3S2,-DEMI, 0));
        return pointsList;
    }
        private List<V3DVect3> getInnerHexagonPointList() {

        List<V3DVect3> pointsList = new ArrayList<V3DVect3>();
        pointsList.add(new V3DVect3( DEMI, 0.29f, 0));
        pointsList.add(new V3DVect3( ZERO, 0.59f, 0));
        pointsList.add(new V3DVect3(-DEMI, 0.29f, 0));
        pointsList.add(new V3DVect3(-DEMI,-0.29f, 0));
        pointsList.add(new V3DVect3( ZERO,-0.59f, 0));
        pointsList.add(new V3DVect3( DEMI,-0.29f, 0));
        return pointsList;
    }

       private List<V3DVect3> getInnerStarPointList() {

        List<V3DVect3> pointsList = new ArrayList<V3DVect3>();
        pointsList.add(new V3DVect3(   UN,  ZERO, 0));
        pointsList.add(new V3DVect3( DEMI, 0.29f, 0));
        pointsList.add(new V3DVect3( DEMI,  R3S2, 0));
        pointsList.add(new V3DVect3( ZERO, 0.59f, 0));
        pointsList.add(new V3DVect3(-DEMI,  R3S2, 0));
        pointsList.add(new V3DVect3(-DEMI, 0.29f, 0));
        pointsList.add(new V3DVect3(  -UN,  ZERO, 0));
        pointsList.add(new V3DVect3(-DEMI,-0.29f, 0));
        pointsList.add(new V3DVect3(-DEMI, -R3S2, 0));
        pointsList.add(new V3DVect3( ZERO,-0.59f, 0));
        pointsList.add(new V3DVect3( DEMI, -R3S2, 0));
        pointsList.add(new V3DVect3( DEMI,-0.29f, 0));
        return pointsList;
    }
}
