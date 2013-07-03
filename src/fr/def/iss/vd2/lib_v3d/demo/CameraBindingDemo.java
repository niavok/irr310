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
public class CameraBindingDemo{

    final V3DContext context = new V3DContext();
    
    

    public CameraBindingDemo(int layout) {
        
        
        if(layout == 1) {
            generateCanvas1();
        } else if (layout == 2) {
            generateCanvas2();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Binding en plein écran avec une miniature
        new CameraBindingDemo(2);

    }

    private void generateCanvas1() {

        V3DCameraBinding fullscreenBinding;
        V3DCameraBinding miniBinding;
        V3DSimple2DCamera activeCamera;
        V3DCanvas canvas;

        canvas = new V3DCanvas(context, 1024, 768);

        activeCamera = new V3DSimple2DCamera();
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);

        miniBinding = V3DCameraBinding.buildAbsoluteCamera(activeCamera, 10, 20 , 200, 150);



        activeCamera.setShowCenter(true);
        activeCamera.setBackgroundColor(V3DColor.darkblue);
        canvas.addCamera(fullscreenBinding);
        canvas.addCamera(miniBinding);

        canvas.setShowFps(true);
        canvas.setEnabled(true);
        
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    private void generateCanvas2() {

        

        V3DCanvas canvas;

        canvas = new V3DCanvas(context, 1024, 768);

        //Left
        {
        V3DSimple2DCamera leftCamera;
        V3DCameraBinding leftBinding;

        

        leftCamera = new V3DSimple2DCamera();
        leftCamera.setBackgroundColor(V3DColor.emerald);
        leftCamera.setShowCenter(true);
        leftBinding = new V3DCameraBinding();

        leftBinding.preferredXMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        leftBinding.preferredYMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        leftBinding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
        leftBinding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

        leftBinding.preferredX = 0;
        leftBinding.preferredY = 0;
        leftBinding.preferredWidth = 50; //50%
        leftBinding.preferredHeight = 100; //100%

        leftBinding.setCamera(leftCamera);

        canvas.addCamera(leftBinding);

        }

        //Top right left
        {
        V3DSimple2DCamera topRightLeftCamera;
        V3DCameraBinding topRightLeftBinding;



        topRightLeftCamera = new V3DSimple2DCamera();
        topRightLeftCamera.setBackgroundColor(V3DColor.azure);
        topRightLeftCamera.setShowCenter(true);
        topRightLeftBinding = new V3DCameraBinding();

        topRightLeftBinding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
        topRightLeftBinding.preferredYMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        topRightLeftBinding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
        topRightLeftBinding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

        topRightLeftBinding.preferredX = 50; //50%
        topRightLeftBinding.preferredY = 0; //0px
        topRightLeftBinding.preferredWidth = 25; //25%
        topRightLeftBinding.preferredHeight = 40; //40%

        topRightLeftBinding.setCamera(topRightLeftCamera);

        canvas.addCamera(topRightLeftBinding);

        }

        //Top right right
        {
        V3DSimple2DCamera topRightRightCamera;
        V3DCameraBinding topRightRightinding;



        topRightRightCamera = new V3DSimple2DCamera();
        topRightRightCamera.setBackgroundColor(V3DColor.fushia);
        topRightRightCamera.setShowCenter(true);
        topRightRightinding = new V3DCameraBinding();

        topRightRightinding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
        topRightRightinding.preferredYMode = V3DCameraBinding.LocationMode.ABSOLUTE;
        topRightRightinding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
        topRightRightinding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

        topRightRightinding.preferredX = 75; //0%
        topRightRightinding.preferredY = 0; //0px
        topRightRightinding.preferredWidth = 25; //100px from left
        topRightRightinding.preferredHeight = 40; //100%

        topRightRightinding.setCamera(topRightRightCamera);

        canvas.addCamera(topRightRightinding);

        }
        

        //Bottom right
        {
        V3DSimple2DCamera bottomRightCamera;
        V3DCameraBinding bottomRightinding;



        bottomRightCamera = new V3DSimple2DCamera();
        bottomRightCamera.setBackgroundColor(V3DColor.lavander);
        bottomRightCamera.setShowCenter(true);
        bottomRightinding = new V3DCameraBinding();

        bottomRightinding.preferredXMode = V3DCameraBinding.LocationMode.RELATIVE;
        bottomRightinding.preferredYMode = V3DCameraBinding.LocationMode.RELATIVE;
        bottomRightinding.preferredWidthMode = V3DCameraBinding.LocationMode.RELATIVE;
        bottomRightinding.preferredHeightMode = V3DCameraBinding.LocationMode.RELATIVE;

        bottomRightinding.preferredX = 50; //50%
        bottomRightinding.preferredY = 40; //40%
        bottomRightinding.preferredWidth = 50; //50%
        bottomRightinding.preferredHeight = 60; //60%

        bottomRightinding.setCamera(bottomRightCamera);

        canvas.addCamera(bottomRightinding);

        }
        
        try {
			canvas.start();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
}
