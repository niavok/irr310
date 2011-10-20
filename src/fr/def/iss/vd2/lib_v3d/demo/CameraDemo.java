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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple2DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DRotateOnlyCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;

/**
 *
 * @author fberto
 */
public class CameraDemo{

    final V3DContext context = new V3DContext();

    public CameraDemo() {


         generateCanvas();


      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new CameraDemo();


    }

    private void generateCanvas() {



        V3DCanvas canvas;

        canvas = new V3DCanvas(context, 1024, 768);

        V3DScene scene = new V3DScene(context);


        //Left
        {

            V3DSimple2DCamera camera = new V3DSimple2DCamera(context);
            camera.setBackgroundColor(V3DColor.violet);
            camera.setShowCenter(true);
            V3DCameraBinding binding = new V3DCameraBinding();

            camera.addController(new V3DSimple2DCameraController(camera));

            binding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredYMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

            binding.preferredX = 0;
            binding.preferredY = 0;
            binding.preferredWidth = 33;
            binding.preferredHeight = 100;

            binding.setCamera(camera);
            camera.setScene(scene);

            canvas.addCamera(binding);

        }


        //Center
        {

            V3DSimple3DCamera camera = new V3DSimple3DCamera(context);
            camera.setBackgroundColor(V3DColor.mauve);
            camera.setShowCenter(true);
            V3DCameraBinding binding = new V3DCameraBinding();

            camera.addController(new V3DSimple3DRotateOnlyCameraController(camera));

            binding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredYMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

            binding.preferredX = 33;
            binding.preferredY = 0;
            binding.preferredWidth = 34;
            binding.preferredHeight = 100;

            binding.setCamera(camera);
            camera.setScene(scene);

            canvas.addCamera(binding);

        }

        //Right
        {



            V3DSimple3DCamera camera = new V3DSimple3DCamera(context);
            camera.setBackgroundColor(V3DColor.lavander);
            camera.setShowCenter(true);
            V3DCameraBinding binding = new V3DCameraBinding();

            camera.addController(new V3DSimple3DCameraController(camera));

            binding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredYMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
            binding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

            binding.preferredX = 67;
            binding.preferredY = 0;
            binding.preferredWidth = 33;
            binding.preferredHeight = 100;

            binding.setCamera(camera);
            camera.setScene(scene);

            canvas.addCamera(binding);

        }


        fillScene(scene);



        canvas.setShowFps(true);
        canvas.setEnabled(true);
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void fillScene(V3DScene scene) {
        V3DBox box = new V3DBox(context);
        box.setSize(new V3DVect3(1, 1, 2));

        scene.add(new V3DColorElement(box, V3DColor.emerald));
    }
}
