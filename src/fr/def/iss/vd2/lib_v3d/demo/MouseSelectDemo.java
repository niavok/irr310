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

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DDoubleClickController;
import fr.def.iss.vd2.lib_v3d.controller.V3DMouseOverController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSelectController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.listener.V3DSelectionListener;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DPolygonBox;

/**
 * Démo présentant les fonctionnalités de detection de survol, clic et double
 * clic de la souris sur des éléments.
 *
 * Controles : survol, clic et double clic sur des éléments.
 *
 * @author fberto
 */
public class MouseSelectDemo{

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple3DCamera activeCamera;
    V3DCanvas canvas;

    public MouseSelectDemo() {

    	generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new MouseSelectDemo();



    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple3DCamera(context);
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);
        activeCamera.addController(new V3DSimple3DCameraController(activeCamera));


        activeCamera.setShowCenter(true);
        activeCamera.setBackgroundColor(V3DColor.darkblue);

        activeCamera.setScene(generateScene());

        generateControllers();


        canvas.addCamera(fullscreenBinding);

        canvas.setShowFps(true);
        canvas.setEnabled(true);

        activeCamera.fitAll();
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private V3DScene generateScene() {
        V3DScene scene = new V3DScene(context);

        {
            V3DBox box = new V3DBox(context);
            box.setSize(new V3DVect3(1, 2, 3));
            V3DColorElement colorElement = new V3DColorElement(box, V3DColor.azure);

            colorElement.setPosition(4, 2, 0);
            colorElement.setSelectable(true);
            scene.add(colorElement);
        }

        {
            V3DBox box = new V3DBox(context);
            box.setSize(new V3DVect3(1, 1, 2));
            V3DColorElement colorElement = new V3DColorElement(box, V3DColor.azure);

            colorElement.setPosition(2, 1, 0);
            colorElement.setSelectable(true);
            scene.add(colorElement);
        }

        {
            V3DBox box = new V3DBox(context);
            box.setSize(new V3DVect3(4, 4, 1));
            V3DColorElement colorElement = new V3DColorElement(box, V3DColor.azure);

            colorElement.setPosition(0, -3, 0);
            colorElement.setSelectable(true);
            scene.add(colorElement);
        }

        {
            V3DBox box = new V3DBox(context);
            box.setSize(new V3DVect3(2, 2, 6));
            V3DColorElement colorElement = new V3DColorElement(box, V3DColor.azure);

            colorElement.setPosition(-4, -3, 0);
            colorElement.setSelectable(true);
            scene.add(colorElement);
        }

        {
            V3DElement box = getBoxHexaGon();
            V3DColorElement colorElement = new V3DColorElement(box, V3DColor.azure);

            colorElement.setPosition(4, -4, 0);
            colorElement.setSelectable(true);
            scene.add(colorElement);
        }



        return scene;
    }

    private void generateControllers() {

        {
            V3DDoubleClickController controller = new V3DDoubleClickController(context);
            controller.setListener(new V3DSelectionListener() {

                @Override
                public boolean select(V3DElement selection) {
                    if (selection instanceof V3DColorElement) {
                        V3DColorElement colorElement = (V3DColorElement) selection;
                        if (colorElement.getColor().isSame(V3DColor.violet) || colorElement.getColor().isSame(V3DColor.mauve)) {
                            colorElement.setColor(V3DColor.fushia);
                        } else if( colorElement.getColor().isSame(V3DColor.fushia)) {
                            colorElement.setColor(V3DColor.violet);
                        }
                    }

                    return true;
                }
            });

            activeCamera.addController(controller);

        }

        {
            V3DSelectController controller = new V3DSelectController(context);
            controller.setListener(new V3DSelectionListener() {

                @Override
                public boolean select(V3DElement selection) {
                    if (selection instanceof V3DColorElement) {
                        V3DColorElement colorElement = (V3DColorElement) selection;
                        if (colorElement.getColor().isSame(V3DColor.violet)) {
                            colorElement.setColor(V3DColor.mauve);
                        } else if( colorElement.getColor().isSame(V3DColor.mauve)) {
                            colorElement.setColor(V3DColor.violet);
                        }
                    }

                    return true;
                }
            });

            activeCamera.addController(controller);

        }

        

        {
            V3DMouseOverController controller = new V3DMouseOverController(context);

            controller.setListener(new V3DSelectionListener() {

                V3DColorElement over;

                @Override
                public boolean select(V3DElement selection) {
                    //System.err.println("over");

                    if (over != null) {
                        if (over.getColor().isSame(V3DColor.violet)) {
                            over.setColor(V3DColor.azure);
                        }
                    }
                    over = null;

                    if (selection instanceof V3DColorElement) {
                        V3DColorElement colorElement = (V3DColorElement) selection;
                        if (colorElement.getColor().isSame(V3DColor.azure) || colorElement.getColor().isSame(V3DColor.violet)) {
                            colorElement.setColor(V3DColor.violet);
                            over = colorElement;
                        }
                    }


                    return true;
                }
            });




            activeCamera.addController(controller);

        }

    }

    private V3DPolygonBox getBoxHexaGon() {

        V3DPolygonBox polygon = new V3DPolygonBox(context);
        polygon.setHeight(2);
        polygon.setPointList(getHexaGonPointList(), false);
        return polygon;
    }

     public static float UN   = 1F;      // Constante sinus 90°
    public static float R3S2 = 0.866F;  // Constante sinus 60° : racine de 3 / 2
    public static float R2S2 = 0.7071F; // Constante sinus 45° : racine de 2 / 2
    public static float DEMI = 0.5F;    // Constante sinus 30° : 1 / 2
    public static float ZERO = 0F;      // Constante sinus 0°
    public static float SPAG = 6F;      // Espacement entre élements de la scène

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
}
