/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.def.iss.vd2.lib_v3d.v3draw.writers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;

import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawPolygon;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawQuad;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawSpec;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawTriangle;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawVertex;

/**
 *
 * @author fberto
 */
public class DefaultV3DrawWriter implements V3DrawWriter {

    final private DataOutputStream output;
    ByteArrayOutputStream contentOutputStream = new ByteArrayOutputStream();
    DataOutputStream contentWriter = new DataOutputStream(contentOutputStream);
    ByteArrayOutputStream initialisationCommandListStream = new ByteArrayOutputStream();
    DataOutputStream initialisationCommandListWriter = new DataOutputStream(initialisationCommandListStream);
    ByteArrayOutputStream commandListStream = new ByteArrayOutputStream();
    DataOutputStream commandListWriter = new DataOutputStream(commandListStream);
    private float minX = 0;
    private float maxX = 0;
    private float minY = 0;
    private float maxY = 0;
    private float minZ = 0;
    private float maxZ = 0;
    private List<String> metadataList = new ArrayList<String>();
    private int commandCount = 0;
    private int initialisationCommandCount = 0;
    private int registation3dConcavePolygon = 0;

    public DefaultV3DrawWriter(File outputFile) throws FileNotFoundException {
        output = new DataOutputStream(new FileOutputStream(outputFile));
    }

    public DefaultV3DrawWriter(OutputStream outputstream) {
        output = new DataOutputStream(outputstream);
    }

    @Override
    public void addMetadata(String metadata) {
        metadataList.add(metadata);
    }

    @Override
    public void write() throws IOException {
        generateContent();
        writeMagicNumber();
        writeCRC32();
        output.write(contentOutputStream.toByteArray());
    }

    private void writeMagicNumber() throws IOException {
        output.writeBytes("V3Draw  ");
    }

    private void generateContent() throws IOException {
        generateHeader();
        generateBody();
        generateRessources();
    }

    private void writeCRC32() throws IOException {
        CRC32 crc = new CRC32();
        crc.update(contentOutputStream.toByteArray());
        output.writeInt((int) crc.getValue());
    }

    private void generateHeader() throws IOException {
        ByteArrayOutputStream headerOutputStream = new ByteArrayOutputStream();
        DataOutputStream headerWriter = new DataOutputStream(headerOutputStream);

        // Build Header
        writeString(headerWriter, "1.0.0");

        headerWriter.writeInt(metadataList.size());
        for (String metadata : metadataList) {
            writeString(headerWriter, metadata);
        }
        // Write bounds
        headerWriter.writeFloat(minX);
        headerWriter.writeFloat(maxX);
        headerWriter.writeFloat(minY);
        headerWriter.writeFloat(maxY);
        headerWriter.writeFloat(minZ);
        headerWriter.writeFloat(maxZ);

        // Copy out Header in main Output Stream
        contentWriter.writeInt(headerOutputStream.size());
        contentWriter.write(headerOutputStream.toByteArray());
    }

    private void generateBody() throws IOException {
        ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
        DataOutputStream bodyWriter = new DataOutputStream(bodyOutputStream);

        // Draw Body Construction
        bodyWriter.writeInt(commandListStream.size() + 4);
        bodyWriter.writeInt(commandCount);
        bodyWriter.write(commandListStream.toByteArray());

        // Init Body Construction
        bodyWriter.writeInt(initialisationCommandListStream.size() + 4);
        bodyWriter.writeInt(initialisationCommandCount);
        bodyWriter.write(initialisationCommandListStream.toByteArray());

        // Copy out Draw Body and Init Body in main Output Stream
        contentWriter.writeInt(bodyOutputStream.size());
        contentWriter.write(bodyOutputStream.toByteArray());
    }

    private void generateRessources() throws IOException {
        ByteArrayOutputStream resourcesOutputStream = new ByteArrayOutputStream();
        DataOutputStream resourcesWriter = new DataOutputStream(resourcesOutputStream);

        // Copy out Resources in main Output Stream
        contentWriter.writeInt(resourcesOutputStream.size());
        contentWriter.write(resourcesOutputStream.toByteArray());
    }

    @Override
    public void setBounds(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    private void writeString(DataOutputStream headerWriter, String string) throws IOException {
        try {
            byte[] bytes = string.getBytes("UTF-8");
            headerWriter.writeInt(bytes.length);
            headerWriter.write(bytes);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DefaultV3DrawWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draw2dLineStripList(final List<List<V3DVect3>> lines) throws IOException {

        new CommandWriter(V3DrawSpec.DRAW_2D_LINE_STRIP_LIST, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeInt(lines.size());
                for (List<V3DVect3> line : lines) {
                    writeInt(line.size());
                    for (V3DVect3 point : line) {
                        writeFloat(point.x);
                        writeFloat(point.y);
                    }
                }
            }
        };

    }

    @Override
    public void draw3dTriangleList(final List<V3DrawTriangle> triangleList) throws IOException {

        new CommandWriter(V3DrawSpec.DRAW_3D_TRIANGLE_LIST, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {

                writeInt(triangleList.size());
                for (List<V3DrawVertex> triangle : triangleList) {
                    for (V3DrawVertex vertex : triangle) {
                        // Texture
                        writeFloat(vertex.texture.x);
                        writeFloat(vertex.texture.y);
                        // Normal
                        writeFloat(vertex.normal.x);
                        writeFloat(vertex.normal.y);
                        writeFloat(vertex.normal.z);
                        // Position
                        writeFloat(vertex.position.x);
                        writeFloat(vertex.position.y);
                        writeFloat(vertex.position.z);
                    }
                }
            }
        };
    }

    @Override
    public void draw3dQuadList(final List<V3DrawQuad> quadList) throws IOException {

        new CommandWriter(V3DrawSpec.DRAW_3D_QUAD_LIST, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeInt(quadList.size());
                for (List<V3DrawVertex> quad : quadList) {
                    for (V3DrawVertex vertex : quad) {

                        // Texture
                        writeFloat(vertex.texture.x);
                        writeFloat(vertex.texture.y);
                        // Normal
                        writeFloat(vertex.normal.x);
                        writeFloat(vertex.normal.y);
                        writeFloat(vertex.normal.z);
                        // Position
                        writeFloat(vertex.position.x);
                        writeFloat(vertex.position.y);
                        writeFloat(vertex.position.z);
                    }
                }
            }
        };
    }

    @Override
    public void draw3dPolygonList(final List<V3DrawPolygon> polygonList) throws IOException {

        new CommandWriter(V3DrawSpec.DRAW_3D_POLYGON_LIST, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeInt(polygonList.size());
                for (List<V3DrawVertex> polygon : polygonList) {

                    writeInt(polygon.size());

                    for (V3DrawVertex vertex : polygon) {

                        // Texture
                        writeFloat(vertex.texture.x);
                        writeFloat(vertex.texture.y);
                        // Normal
                        writeFloat(vertex.normal.x);
                        writeFloat(vertex.normal.y);
                        writeFloat(vertex.normal.z);
                        // Position
                        writeFloat(vertex.position.x);
                        writeFloat(vertex.position.y);
                        writeFloat(vertex.position.z);
                    }
                }
            }
        };
    }

    @Override
    public int register3dConcavePolygon(final V3DrawPolygon polygon) throws IOException {

        new CommandWriter(V3DrawSpec.REGISTER_3D__CONCAVE_POLYGON, CommandType.INIT) {

            @Override
            void generate() throws IOException {
                writeInt(polygon.size());

                for (V3DrawVertex vertex : polygon) {
                    // Texture
                    writeFloat(vertex.texture.x);
                    writeFloat(vertex.texture.y);
                    // Normal
                    writeFloat(vertex.normal.x);
                    writeFloat(vertex.normal.y);
                    writeFloat(vertex.normal.z);
                    // Position
                    writeFloat(vertex.position.x);
                    writeFloat(vertex.position.y);
                    writeFloat(vertex.position.z);
                }
            }
        };

        int index = registation3dConcavePolygon;
        registation3dConcavePolygon++;

        return index;
    }

    @Override
    public void drawRegistered3dConcavePolygon(final int polygonIndex) throws IOException {

        new CommandWriter(V3DrawSpec.DRAW_REGISTRATED_3D_CONCAVE_POLYGON, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeInt(polygonIndex);
            }
        };
    }

    @Override
    public void enableTexture(final int imageIndex) throws IOException {

        new CommandWriter(V3DrawSpec.ENABLE_TEXTURE, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeInt(imageIndex);
            }
        };
    }

    @Override
    public void disableTexture() throws IOException {

        new CommandWriter(V3DrawSpec.DISABLE_TEXTURE, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
            }
        };
    }

    @Override
    public void registerTexture(final byte[] image) throws IOException {

        new CommandWriter(V3DrawSpec.REGISTER_TEXTURE, CommandType.INIT) {

            @Override
            void generate() throws IOException {
                writeInt(image.length);
                write(image);
            }
        };
    }

    @Override
    public void setLineThickness(final float thickness) throws IOException {

        new CommandWriter(V3DrawSpec.SET_LINE_THICKNESS, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeFloat(thickness);
            }
        };
    }

    @Override
    public void setPointThickness(final float thickness) throws IOException {

        new CommandWriter(V3DrawSpec.SET_POINT_THICKNESS, CommandType.DISPLAY) {

            @Override
            void generate() throws IOException {
                writeFloat(thickness);
            }
        };
    }

    private abstract class CommandWriter {

        private ByteArrayOutputStream commandStream = new ByteArrayOutputStream();
        private DataOutputStream commandWriter = new DataOutputStream(commandStream);

        public CommandWriter(int commandCode, CommandType type) throws IOException {

            generate();

            DataOutputStream activeCommandList;

            if (type == CommandType.DISPLAY) {
                activeCommandList = commandListWriter;
                commandCount++;
            } else {
                activeCommandList = initialisationCommandListWriter;
                initialisationCommandCount++;
            }
            // System.err.println("commandWriter.commandcode " + commandCode);
            activeCommandList.writeInt(commandCode);
            // System.err.println("commandWriter.size() " + commandWriter.size());
            activeCommandList.writeInt(commandWriter.size());
            activeCommandList.write(commandStream.toByteArray());
        }

        protected void writeInt(int value) throws IOException {
            commandWriter.writeInt(value);
        }

        protected void writeFloat(float value) throws IOException {
            commandWriter.writeFloat(value);
        }

        abstract void generate() throws IOException;

        protected void write(byte[] image) throws IOException {
            commandWriter.write(image);
        }
    }

    private enum CommandType {

        DISPLAY,
        INIT,
    }
}
