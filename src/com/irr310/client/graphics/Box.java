package com.irr310.client.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

/**
* The vertex and fragment shaders are setup when the box object is
* constructed. They are applied to the GL state prior to the box
* being drawn, and released from that state after drawing.
* @author Stephen Jones
*/
public class Box {
    
    /*
    * if the shaders are setup ok we can use shaders, otherwise we just
    * use default settings
    */
    private boolean useShader=true;
    
    /*
    * program shader, to which is attached a vertex and fragment shaders.
    * They are set to 0 as a check because GL will assign unique int
    * values to each
    */
    private int shader=0;
    private int vertShader=0;
    private int fragShader=0;
	private int skyFragShader = 0;

    public Box(){
    
        /*
        * create the shader program. If OK, create vertex
        * and fragment shaders
        */
        shader=ARBShaderObjects.glCreateProgramObjectARB();
        
        if(shader!=0){
            vertShader=createVertShader("shaders/hello-gl.v.glsl");
            fragShader=createFragShader("shaders/hello-gl.f.glsl");
            //skyFragShader=createFragShader("shaders/sky-gl.f.glsl");
        }
        else useShader=false;

        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
        if(vertShader !=0 && fragShader !=0){
            ARBShaderObjects.glAttachObjectARB(shader, vertShader);
            ARBShaderObjects.glAttachObjectARB(shader, fragShader);
            ARBShaderObjects.glLinkProgramARB(shader);
            ARBShaderObjects.glValidateProgramARB(shader);
            useShader=printLogInfo(shader, "attach");
        }else useShader=false;
    }

    /*
    * If the shader was setup succesfully, we use the shader. Otherwise
    * we run normal drawing code.
    */
    public void draw(){
        if(useShader) {
            ARBShaderObjects.glUseProgramObjectARB(shader);
        }
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);//red

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
        GL11.glVertex3f(1.0f, 1.0f, 0.0f);
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glEnd();
        
        //GL11.glColor3f(0.0f, 1.0f, 0.0f);//green
        
        //if(useShader) {
            //ARBShaderObjects.glUseProgramObjectARB(fragShader);
        //}
        
        /*GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
        GL11.glVertex3f(1.0f, 1.0f, 0.0f);
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glEnd();*/

        //release the shader
        ARBShaderObjects.glUseProgramObjectARB(0);

    }

    /*
    * With the exception of syntax, setting up vertex and fragment shaders
    * is the same.
    * @param the name and path to the vertex shader
    */
    private int createVertShader(String filename){
        //vertShader will be non zero if succefully created

        vertShader=ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        //if created, convert the vertex shader code to a String
        if(vertShader==0){return 0;}
        String vertexCode="";
        String line;
        try{
            BufferedReader reader=new BufferedReader(new FileReader(filename));
            while((line=reader.readLine())!=null){
                vertexCode+=line + "\n";
            }
            reader.close();
        }catch(Exception e){
            System.out.println("Fail reading vertex shading code: "+ filename);
            return 0;
        }
        /*
        * associate the vertex code String with the created vertex shader
        * and compile
        */
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);
        //if there was a problem compiling, reset vertShader to zero
        if(!printLogInfo(vertShader, filename)){
            vertShader=0;
        }
        //if zero we won't be using the shader
        return vertShader;
    }

    //same as per the vertex shader except for method syntax
    private int createFragShader(String filename){

        fragShader=ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        if(fragShader==0){return 0;}
            String fragCode="";
            String line;
        try{
            BufferedReader reader=new BufferedReader(new FileReader(filename));
            while((line=reader.readLine())!=null){
                fragCode+=line + "\n";
            }
            reader.close();
        }catch(Exception e){
            System.out.println("Fail reading fragment shading code"+ filename);
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);
        if(!printLogInfo(fragShader, filename)){
            fragShader=0;
        }

        return fragShader;
    }
    
    private static boolean printLogInfo(int obj, String filename){
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj,
        ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        if (length > 1) {
            // We have some info we need to output.
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log for "+filename+":\n"+out);
        }
        else return true;
        return false;
    }

}
