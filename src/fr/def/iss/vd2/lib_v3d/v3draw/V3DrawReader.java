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
package fr.def.iss.vd2.lib_v3d.v3draw;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.I3dContext;
import com.irr310.i3d.scene.I3dCamera;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.element.TextureHandler;

/**
 *
 * @author fberto
 */
public class V3DrawReader {

    final private ByteBuffer buffer;
    private float maxX;
    private float maxY;
    private float maxZ;
    private float minX;
    private float minY;
    private float minZ;
    private String version = "";
    private List<String> metadataList = new ArrayList<String>();
    private int commandListOffset;
    private int initCommandListOffset;
    private I3dCamera camera;
    private List<Tesselation> concavePolygonList = new ArrayList<Tesselation>();
    private List<TextureHandler> textureList = new ArrayList<TextureHandler>();

    public V3DrawReader(ByteBuffer buffer) throws V3DrawBadFormatError {
        this.buffer = buffer;
        checkIntegrity();
        load();
    }

    private void checkIntegrity() throws V3DrawBadFormatError {
        // Prepare the buffer for reading
        buffer.clear();

        String magicNumber = "";
        for (int i = 0; i < 8; i++) {
            byte c = buffer.get();
            magicNumber += Character.valueOf((char) c);
        }
        if (!magicNumber.equals("V3Draw  ")) {
            throw new V3DrawBadFormatError("Magic number is '" + magicNumber + "' but 'V3Draw' is expected.");
        }
        int crc = buffer.getInt();

        byte[] digest = new byte[buffer.remaining()];
        buffer.get(digest);
        int computedCRC = computeCRC(digest);

        if (computedCRC != crc) {
            throw new V3DrawBadFormatError("CRC in file is '" + Integer.toHexString(crc) + "' but computed CRC is " + Integer.toHexString(computedCRC) + ".");
        }
    }

    private int computeCRC(byte[] digest) {
        CRC32 crc = new CRC32();
        crc.update(digest);
        return (int) crc.getValue();
    }

    private void load() {

        // Prepare the buffer for reading
        buffer.clear();

        // Skip Magic number and CRC
        buffer.position(buffer.position() + 12);

        int headerLenght = buffer.getInt();

        // Read version
        version = readString(buffer);

        // Read metadata
        int metadataCount = buffer.getInt();

        for (int i = 0; i < metadataCount; i++) {
            metadataList.add(readString(buffer));
        }
        // Read bound
        minX = buffer.getFloat();
        maxX = buffer.getFloat();
        minY = buffer.getFloat();
        maxY = buffer.getFloat();
        minZ = buffer.getFloat();
        maxZ = buffer.getFloat();

        int bodyLenght = buffer.getInt();
        int commandListLenght = buffer.getInt();

        buffer.position(buffer.position() + commandListLenght);

        // 24 = 12 + headerSize size + body size size + commandListSize
        commandListOffset = 24 + headerLenght;

        // 4 = size of init command list
        initCommandListOffset = commandListOffset + commandListLenght + 4;
    }

    public void init() {
        // Skip header and command list
        buffer.position(initCommandListOffset);

        int commandCount = buffer.getInt();

        for (int i = 0; i < commandCount; i++) {

            int commandCode = buffer.getInt();
            int commandSize = buffer.getInt();

            switch (commandCode) {
                case V3DrawSpec.REGISTER_3D__CONCAVE_POLYGON:
                    register3dConcavePolygon();
                    break;
                case V3DrawSpec.REGISTER_TEXTURE:
                    registerTexture();
                    break;
                default:
                    System.err.println("Warning: Unimplemented init command code '" + commandCode + "'");
                    // Consume command
                    buffer.position(buffer.position() + commandSize);
            }
        }
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public static class V3DrawBadFormatError extends Exception {

        public V3DrawBadFormatError(String message) {
            super(message);
        }
    }

    private String readString(ByteBuffer buffer) {
        int lenght = buffer.getInt();
        byte[] bytes = new byte[lenght];

        buffer.get(bytes);

        return new String(bytes);
    }

    public List<String> getMetadataList() {
        return metadataList;
    }

    public String getVersion() {
        return version;
    }

    public void draw(I3dCamera camera) {
        this.camera = camera;

        // Prepare the buffer for reading
        buffer.clear();

        // Skip header
        buffer.position(commandListOffset);

        int commandCount = buffer.getInt();

        for (int i = 0; i < commandCount; i++) {

            int commandCode = buffer.getInt();
            int commandSize = buffer.getInt();

            switch (commandCode) {
                case V3DrawSpec.DRAW_2D_LINE_STRIP_LIST:
                    draw2dLineStripList();
                    break;
                case V3DrawSpec.DRAW_3D_QUAD_LIST:
                    draw3dQuadList();
                    break;
                case V3DrawSpec.DRAW_3D_TRIANGLE_LIST:
                    draw3dTriangleList();
                    break;
                case V3DrawSpec.DRAW_3D_POLYGON_LIST:
                    draw3dPolygonList();
                    break;
                case V3DrawSpec.DRAW_REGISTRATED_3D_CONCAVE_POLYGON:
                    drawRegistrated3dConcavePolygon();
                    break;
                case V3DrawSpec.ENABLE_TEXTURE:
                    enableTexture();
                    break;
                case V3DrawSpec.DISABLE_TEXTURE:
                    disableTexture();
                    break;
                case V3DrawSpec.SET_LINE_THICKNESS:
                    setLineThickness();
                    break;
                case V3DrawSpec.SET_POINT_THICKNESS:
                    setPointThickness();
                    break;
                default:
                    System.err.println("Warning: Unimplemented command code '" + commandCode + "'");

                    // Consume command
                    buffer.position(buffer.position() + commandSize);
            }
        }
    }

    private void draw2dLineStripList() {
        int lineCount = buffer.getInt();

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            int lineLenght = buffer.getInt();
            GL11.glBegin(GL11.GL_LINE_STRIP);           	// Draw line
            for (int j = 0; j < lineLenght; j++) {
                GL11.glVertex3f(buffer.getFloat(), buffer.getFloat(), 0.0f);
            }
            GL11.glEnd();
        }
    }

    private void draw3dQuadList() {
        int quadCount = buffer.getInt();
        GL11.glBegin(GL11.GL_QUADS);

        for (int quadIndex = 0; quadIndex < quadCount; quadIndex++) {
            for (int i = 0; i < 4; i++) {
                GL11.glTexCoord2f(buffer.getFloat(), buffer.getFloat());
                GL11.glNormal3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
                GL11.glVertex3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());

            }
        }
        GL11.glEnd();
    }

    private void draw3dTriangleList() {
        int triangleCount = buffer.getInt();

        GL11.glBegin(GL11.GL_TRIANGLES);
        for (int triangleIndex = 0; triangleIndex < triangleCount; triangleIndex++) {

            for (int i = 0; i < 3; i++) {
                GL11.glTexCoord2f(buffer.getFloat(), buffer.getFloat());
                GL11.glNormal3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
                GL11.glVertex3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
            }
        }
        GL11.glEnd();
    }

    private void draw3dPolygonList() {
        int polygonCount = buffer.getInt();

        for (int polygonIndex = 0; polygonIndex < polygonCount; polygonIndex++) {
            GL11.glBegin(GL11.GL_POLYGON);
            int vertexCount = buffer.getInt();
            for (int i = 0; i < vertexCount; i++) {
                GL11.glTexCoord2f(buffer.getFloat(), buffer.getFloat());
                GL11.glNormal3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
                GL11.glVertex3f(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
            }
            GL11.glEnd();
        }
    }

    private void register3dConcavePolygon() {
        Tesselator tesselator = new Tesselator();

        tesselator.begin();

        int vertexCount = buffer.getInt();
        for (int i = 0; i < vertexCount; i++) {
            tesselator.addVertex(new V3DrawVertex(buffer));
        }
        tesselator.end();

        concavePolygonList.add(tesselator.getResult());

        tesselator.delete();
    }

    private void registerTexture() {
        int dataLenght = buffer.getInt();

        byte[] imageBuffer = new byte[dataLenght];
        buffer.get(imageBuffer);

        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageBuffer));
        } catch (IOException e) {
            return;
        }
        TextureHandler texture = new TextureHandler(image);

        textureList.add(texture);
    }

    private void drawRegistrated3dConcavePolygon() {
        int polygonIndex = buffer.getInt();
        Tesselation tesselation = concavePolygonList.get(polygonIndex);

        List<Integer> vertexTypeList = tesselation.getVertexTypeList();

        for (int i = 0; i < vertexTypeList.size(); i++) {

            List<V3DrawVertex> vertexList = tesselation.getVertexListList().get(i);

            GL11.glBegin(vertexTypeList.get(i));
            for (V3DrawVertex vertex : vertexList) {
                GL11.glTexCoord2f(vertex.texture.x, vertex.texture.y);
                GL11.glNormal3f(vertex.normal.x, vertex.normal.y, vertex.normal.z);
                GL11.glVertex3f(vertex.position.x, vertex.position.y, vertex.position.z);
            }
            GL11.glEnd();
        }
    }

    private void enableTexture() {
        int textureIndex = buffer.getInt();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureList.get(textureIndex).getID());
    }

    private void disableTexture() {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private void setLineThickness() {
        float thickness = buffer.getFloat();
        GL11.glLineWidth(thickness);
        // System.err.println("glLineWidth " + thickness);
    }

    private void setPointThickness() {
        float thickness = buffer.getFloat();
        GL11.glPointSize(thickness);
        // System.err.println("glPointSize " + thickness);
    }
}
