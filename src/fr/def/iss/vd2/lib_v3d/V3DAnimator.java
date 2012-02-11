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

package fr.def.iss.vd2.lib_v3d;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author fberto
 */
public class V3DAnimator {
    private final AnimationThread animationThread;

    private List<V3DCanvas> canvasList = new CopyOnWriteArrayList<V3DCanvas>();

    //Framerate in millisecond
    long framerate = 20;

    private float lastFps = 0;
    private long lastFpsMesureTime = 0;
    private int frameMesureCount = 0;
    private boolean computeFps = false;

    private boolean running = true;

    void addCanvas(V3DCanvas canvas) {
        if(!canvasList.contains(canvas)) {
            canvasList.add(canvas);
        }
    }

    void removeCanvas(V3DCanvas canvas) {
        canvasList.remove(canvas);
    }

    float getFps() {
        return lastFps;
    }

    public boolean isComputeFps() {
        return computeFps;
    }

    public void setComputeFps(boolean computeFps) {
        this.computeFps = computeFps;
    }

    

    class AnimationThread extends Thread {

        @Override
        public void run() {
            final long NANO_IN_MILLI = 1000000;
            long currentTime = System.nanoTime()/NANO_IN_MILLI;
            lastFpsMesureTime = currentTime;
            while(running) {
                draw();
                currentTime = System.nanoTime()/NANO_IN_MILLI;
                updateFpsCounter(currentTime);
                waitNextFrame(currentTime);
            }
        }
        
        private void updateFpsCounter(long currentTime) {
            if(computeFps && currentTime > lastFpsMesureTime+1000) {
                lastFps = (float) (((double) frameMesureCount) / ((double) (currentTime-lastFpsMesureTime) / 1000.0));

                frameMesureCount = 0;
                lastFpsMesureTime = currentTime;

            }            
        }
        
        private void draw() {
            for(V3DCanvas canvas: canvasList) {
                if(canvas.isEnabled() && canvas.isInitied()) {
                    //Repair
                	//canvas.getCanvas().display();
                    frameMesureCount++;
                }
            }            
        }

        private void waitNextFrame(long currentTime) {
            try {
                long sleepTime = framerate - (currentTime % framerate);
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            }            
        }
        
    }

    public V3DAnimator() {
        animationThread = new AnimationThread();
        animationThread.setName("OpenGL animation thread");
        animationThread.setDaemon(true);
        animationThread.start();
    }

    public void terminate() {
        running = false;
    }



}
