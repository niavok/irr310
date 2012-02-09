package fr.def.iss.vd2.lib_v3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

import fr.def.iss.vd2.lib_v3d.camera.V3DCamera;

public class V3DShader {

    
    private final String name;
    /*
     * program shader, to which is attached a vertex and fragment shaders. They
     * are set to 0 as a check because GL will assign unique int values to each
     */
    protected int shader = 0;
    private int vertShader = 0;
    private int fragShader = 0;
    private int skyFragShader = 0;
    private boolean useShader = true;
    
    public V3DShader(String name) {
        this.name = name;
        
        /*
         * create the shader program. If OK, create vertex and fragment shaders
         */
        shader = ARBShaderObjects.glCreateProgramObjectARB();

        if (shader != 0) {
            vertShader = createVertShader("shaders/"+name+".v.glsl");
            fragShader = createFragShader("shaders/"+name+".f.glsl");
            // skyFragShader=createFragShader("shaders/sky-gl.f.glsl");
        } else {
            useShader  = false;
        } 

        /*
         * if the vertex and fragment shaders setup sucessfully, attach them to
         * the shader program, link the sahder program (into the GL context I
         * suppose), and validate
         */
        if (vertShader != 0 && fragShader != 0) {
            ARBShaderObjects.glAttachObjectARB(shader, vertShader);
            ARBShaderObjects.glAttachObjectARB(shader, fragShader);
            ARBShaderObjects.glLinkProgramARB(shader);
            ARBShaderObjects.glValidateProgramARB(shader);
            loadUniforms();

            useShader = printLogInfo(shader, "attach");
        } else {
            useShader = false;
        }
    }
    
    protected void loadUniforms() {
        
    }
    
    protected void setUniforms(V3DCamera camera) {
        
    }
    
    public void begin(V3DCamera camera) {
        if (useShader) {
            ARBShaderObjects.glUseProgramObjectARB(shader);
            setUniforms(camera);
        }
    }
    
    public void end() {
        // release the shader
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    /*
     * With the exception of syntax, setting up vertex and fragment shaders is
     * the same.
     * @param the name and path to the vertex shader
     */
    private int createVertShader(String filename) {
        // vertShader will be non zero if succefully created

        vertShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        // if created, convert the vertex shader code to a String
        if (vertShader == 0) {
            return 0;
        }
        String vertexCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                vertexCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading vertex shading code: " + filename);
            return 0;
        }
        /*
         * associate the vertex code String with the created vertex shader and
         * compile
         */
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);
        // if there was a problem compiling, reset vertShader to zero
        if (!printLogInfo(vertShader, filename)) {
            vertShader = 0;
        }
        // if zero we won't be using the shader
        return vertShader;
    }

    // same as per the vertex shader except for method syntax
    private int createFragShader(String filename) {

        fragShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        if (fragShader == 0) {
            return 0;
        }
        String fragCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                fragCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading fragment shading code" + filename);
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);
        if (!printLogInfo(fragShader, filename)) {
            fragShader = 0;
        }

        return fragShader;
    }

    private static boolean printLogInfo(int obj, String filename) {
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        if (length > 1) {
            // We have some info we need to output.
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log for " + filename + ":\n" + out);
        } else
            return true;
        return false;
    }
}
