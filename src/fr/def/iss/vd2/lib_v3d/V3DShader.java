package fr.def.iss.vd2.lib_v3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;

public class V3DShader {

    private final String name;
    /*
     * program shader, to which is attached a vertex and fragment shaders. They
     * are set to 0 as a check because GL will assign unique int values to each
     */
    protected int shader = 0;
    private int vertShader = 0;
    private int fragShader = 0;
    private int geoShader = 0;
    private boolean useShader = true;
    private boolean useGeoShader = true;
    private boolean loaded = false;

    public V3DShader(String name) {
        this.name = name;
        load();
    }

    public void reload() {
        if (loaded) {
            destroy();
        }
        load();
    }

    private void load() {

        loaded = false;
        useShader = false;
        useGeoShader = false;
        /*
         * create the shader program. If OK, create vertex and fragment shaders
         */
        shader = ARBShaderObjects.glCreateProgramObjectARB();

        if (shader != 0) {
            vertShader = createVertShader("shaders/" + name + ".v.glsl");
            geoShader = createGeometryShader("shaders/" + name + ".g.glsl");
            fragShader = createFragShader("shaders/" + name + ".f.glsl");
        } else {
            useShader = false;
        }

        if(geoShader != 0) {
        	useGeoShader = true;
        }
        
        /*
         * if the vertex and fragment shaders setup sucessfully, attach them to
         * the shader program, link the sahder program (into the GL context I
         * suppose), and validate
         */
        if (vertShader != 0 && fragShader != 0 ) {
            ARBShaderObjects.glAttachObjectARB(shader, vertShader);
            if(useGeoShader) {
            	ARBShaderObjects.glAttachObjectARB(shader, geoShader);
            }
            ARBShaderObjects.glAttachObjectARB(shader, fragShader);
            ARBShaderObjects.glLinkProgramARB(shader);
            ARBShaderObjects.glValidateProgramARB(shader);
            loadUniforms();

            useShader = printLogInfo(shader, "attach");
            loaded = useShader;
        } else {
            useShader = false;
        }

    }

    private void destroy() {
        ARBShaderObjects.glDeleteObjectARB(shader);
        ARBShaderObjects.glDeleteObjectARB(vertShader);
        ARBShaderObjects.glDeleteObjectARB(fragShader);
        if(useGeoShader) {
        	ARBShaderObjects.glDeleteObjectARB(geoShader);
        }
        shader = 0;
        vertShader = 0;
        fragShader = 0;
        geoShader = 0;

        loaded = false;
    }

    protected void loadUniforms() {

    }

    protected void setUniforms() {

    }

    public void begin() {
        if (useShader) {
            try {
            ARBShaderObjects.glUseProgramObjectARB(shader);
            setUniforms();
            } catch (OpenGLException e) {
                System.err.println("error using shader: "+e.getMessage());
                e.printStackTrace();
            }
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
    
 // same as per the vertex shader except for method syntax
    private int createGeometryShader(String filename) {

        if(!GLContext.getCapabilities().GL_EXT_geometry_shader4) {
        	System.err.println("Geometry shader not available");
        	return 0;
        }
        
        geoShader = ARBShaderObjects.glCreateShaderObjectARB(ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB);
        if (geoShader == 0) {
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
            System.out.println("Fail reading geometry shading code" + filename);
            return 0;
        }
        
        
        
        
        ARBShaderObjects.glShaderSourceARB(geoShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(geoShader);
        
        int maxVertices = GL11.glGetInteger(ARBGeometryShader4.GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB);
        System.out.println("max vertice for geometry shader: "+maxVertices);
        
        ARBGeometryShader4.glProgramParameteriARB(shader, ARBGeometryShader4.GL_GEOMETRY_INPUT_TYPE_ARB, GL11.GL_TRIANGLES);
        //ARBGeometryShader4.glProgramParameteriARB(shader, ARBGeometryShader4.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL11.GL_TRIANGLES);
        ARBGeometryShader4.glProgramParameteriARB(shader, ARBGeometryShader4.GL_GEOMETRY_VERTICES_OUT_ARB, 64);
        
        
        
        if (!printLogInfo(geoShader, filename)) {
            geoShader = 0;
        }

        return geoShader;
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
