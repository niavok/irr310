package com.irr310.client.graphics;

import java.util.Date;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DShader;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;
import fr.def.iss.vd2.lib_v3d.element.V3DBoundingBox;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;

/**
 * The vertex and fragment shaders are setup when the box object is constructed.
 * They are applied to the GL state prior to the box being drawn, and released
 * from that state after drawing.
 * 
 * @author Stephen Jones
 */
public class Sky extends V3DElement {

    /*
     * if the shaders are setup ok we can use shaders, otherwise we just use
     * default settings
     */
   
    
    private V3DShader shader;

    public Sky(V3DContext context) {
        super(context);
        
        
        
        
        
        shader = new V3DShader("sky") {
            private int inputRotation;
            private int resolution;
            private int time;
            private long startTime;
            
            protected void loadUniforms() {
                inputRotation = ARBShaderObjects.glGetUniformLocationARB(shader, "inputRotation");
                resolution = ARBShaderObjects.glGetUniformLocationARB(shader, "resolution");
                time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
                
                startTime = new Date().getTime();
            };
            
            protected void setUniforms(V3DCamera camera) {
                V3DVect3 rotation = camera.getRotation();
                ARBShaderObjects.glUniform3fARB(inputRotation, rotation.x, rotation.y, rotation.z);
                ARBShaderObjects.glUniform2fARB(resolution, camera.getCurrentWidth(), camera.getCurrentHeight());
                float time2 = ((float) ( new Date().getTime() -startTime))/10000.0f;
                ARBShaderObjects.glUniform1fARB(time, time2);
                
            };
            
        };
        
    }

    public V3DVect3 getSize() {
        return V3DVect3.ZERO;
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected void doDisplay(V3DCamera camera) {
        float x0 = 0;
        float x1 = camera.getCurrentWidth();
        float y0 = 0;
        float y1 = camera.getCurrentHeight();
        float z0 = -2000;

        shader.begin(camera);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        
        GL11.glNormal3f(0, 0, 1);
        GL11.glColor3d(1.0, 0.0, 0.0);
        GL11.glVertex3d(x0, y0, z0);
        GL11.glColor3d(0.0, 1.0, 0.0);
        GL11.glVertex3d(x0, y1, z0);
        GL11.glColor3d(0.0, 0.0, 1.0);
        GL11.glVertex3d(x1, y1, z0);
        GL11.glVertex3d(x1, y0, z0);

        GL11.glEnd();
        
        shader.end();
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        // Not used
        return null;
    }

}
