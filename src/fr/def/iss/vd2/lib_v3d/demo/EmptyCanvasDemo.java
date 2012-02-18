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
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;

/**
 *
 * @author fberto
 */
public class EmptyCanvasDemo {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple2DCamera activeCamera;
    V3DCanvas canvas;

    public EmptyCanvasDemo() {


    	generateCanvas();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

         new EmptyCanvasDemo();


    }

    private void generateCanvas() {

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple2DCamera(context);
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);


        activeCamera.setShowCenter(true);
        activeCamera.setBackgroundColor(V3DColor.darkblue);
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
}
