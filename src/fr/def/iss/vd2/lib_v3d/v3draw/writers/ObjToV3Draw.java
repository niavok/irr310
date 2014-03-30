/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.def.iss.vd2.lib_v3d.v3draw.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawPolygon;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawQuad;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawTriangle;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawVertex;

/**
 * @author fberto
 */
public class ObjToV3Draw {

    private final File objFile;
    private float minX = Float.POSITIVE_INFINITY;
    private float maxX = Float.NEGATIVE_INFINITY;
    private float minY = Float.POSITIVE_INFINITY;
    private float maxY = Float.NEGATIVE_INFINITY;
    private float minZ = Float.POSITIVE_INFINITY;
    private float maxZ = Float.NEGATIVE_INFINITY;
    private List<V3DVect3> vertexPositionList = new ArrayList<V3DVect3>();
    private List<V3DVect3> vertexTextureList = new ArrayList<V3DVect3>();
    private List<V3DVect3> vertexNormalList = new ArrayList<V3DVect3>();
    private List<V3DrawTriangle> triangleList = new ArrayList<V3DrawTriangle>();
    private List<V3DrawQuad> quadList = new ArrayList<V3DrawQuad>();
    private List<V3DrawPolygon> polygonList = new ArrayList<V3DrawPolygon>();
    private V3DVect3 nullVect = new V3DVect3(0, 0, 0);
    private V3DrawWriter writer;
    private String currentName = "";
    private List<String> textureMapName = new ArrayList<String>();
    private List<byte[]> textureMapData = new ArrayList<byte[]>();
    private List<String> acceptedObjects = null;
    private boolean isIgnoring = false;

    public ObjToV3Draw(File polyFile) {
        this.objFile = polyFile;
    }

    public void write(V3DrawWriter writer) throws FileNotFoundException, IOException {

        this.writer = writer;

        Scanner scanner = new Scanner(objFile);

        // On boucle sur chaque champ detecté
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            parseLine(line);

        }

        flush();

        writeTextures();
        writer.setBounds(minX, maxX, minY, maxY, minZ, maxZ);

        scanner.close();

    }

    private void parseLine(String line) throws IOException {

        if (line.startsWith("#") || line.length() == 0) {
            // Comments
            return;
        } else if (line.startsWith("v ")) {
            // Position definition
            parseVextexPositionDefinition(line.substring(2).trim());
        } else if (line.startsWith("vt ")) {
            // Texture definition
            parseVextexTextureDefinition(line.substring(3).trim());
        } else if (line.startsWith("vn ")) {
            // Normal definition
            parseVextexNormalDefinition(line.substring(3).trim());
        } else if (line.startsWith("f ")) {

            if (!isIgnoring) {
                parseFace(line.substring(2).trim());
            }

        } else if (line.startsWith("g ") || line.startsWith("o ")) {
            // New group or new object
            flush();

            String oName = line.substring(2).trim();
            if (acceptedObjects != null && !acceptedObjects.contains(oName)) {
                isIgnoring = true;
            } else {
                isIgnoring = false;
            }
        }

    }

    private void parseVextexPositionDefinition(String line) {
        String[] coordList = line.split(" +");

        if (coordList.length == 3) {
            float x = Float.parseFloat(coordList[0]);
            float y = Float.parseFloat(coordList[1]);
            float z = Float.parseFloat(coordList[2]);

            updateBounds(x, y, z);

            vertexPositionList.add(new V3DVect3(x, y, z));

        } else {
            for (String a : coordList) {
                System.err.println("'" + a + "'");
            }
            System.err.println("Warning: ObjToV3Draw - bad obj position format. " + line);
        }
    }

    private void parseVextexTextureDefinition(String line) {
        String[] coordList = line.split(" +");

        if (coordList.length >= 2) {
            float x = Float.parseFloat(coordList[0]);
            float y = Float.parseFloat(coordList[1]);

            vertexTextureList.add(new V3DVect3(x, y, 0));

        } else {
            System.err.println("Warning: ObjToV3Draw - bad obj texture format. " + line);
        }
    }

    private void parseVextexNormalDefinition(String line) {
        String[] coordList = line.split(" +");

        if (coordList.length == 3) {
            float x = Float.parseFloat(coordList[0]);
            float y = Float.parseFloat(coordList[1]);
            float z = Float.parseFloat(coordList[2]);

            vertexNormalList.add(new V3DVect3(x, y, z));

        } else {
            System.err.println("Warning: ObjToV3Draw - bad obj normal format. " + line);
        }
    }

    private void parseFace(String line) {

        String[] vertexStringList = line.split(" +");
        String vertexString;

        if (vertexStringList.length == 3) {

            V3DrawTriangle triangle = new V3DrawTriangle();

            for (int i = 0; i < 3; i++) {
                vertexString = vertexStringList[i];
                V3DrawVertex vertex = new V3DrawVertex();

                String[] vertexPartsList = vertexString.split("/");
                if (vertexPartsList.length == 1) {
                    if (vertexPartsList[0].length() != 0) {

                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    triangle.add(vertex);

                } else if (vertexPartsList.length == 2) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    triangle.add(vertex);

                } else if (vertexPartsList.length == 3) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[2].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[2]);
                        vertex.normal = vertexNormalList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    triangle.add(vertex);

                } else {
                    System.err.println("Warning: ObjToV3Draw - bad obj format. " + vertexString);
                    return;
                }
            }

            triangleList.add(triangle);

        } else if (vertexStringList.length == 4) {

            V3DrawQuad quad = new V3DrawQuad();

            for (int i = 0; i < 4; i++) {

                vertexString = vertexStringList[i];
                V3DrawVertex vertex = new V3DrawVertex();

                String[] vertexPartsList = vertexString.split("/");

                if (vertexPartsList.length == 1) {
                    if (vertexPartsList[0].length() != 0) {

                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    quad.add(vertex);

                } else if (vertexPartsList.length == 2) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    quad.add(vertex);

                } else if (vertexPartsList.length == 3) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[2].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[2]);
                        vertex.normal = vertexNormalList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {

                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    quad.add(vertex);

                } else {
                    System.err.println("Warning: ObjToV3Draw - bad obj format. " + vertexString);
                    return;
                }
            }

            quadList.add(quad);

        } else if (vertexStringList.length > 4) {

            V3DrawPolygon polygon = new V3DrawPolygon();

            for (int i = 0; i < vertexStringList.length; i++) {

                vertexString = vertexStringList[i];
                V3DrawVertex vertex = new V3DrawVertex();

                String[] vertexPartsList = vertexString.split("/");
                if (vertexPartsList.length == 1) {
                    if (vertexPartsList[0].length() != 0) {

                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    polygon.add(vertex);

                } else if (vertexPartsList.length == 2) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    polygon.add(vertex);

                } else if (vertexPartsList.length == 3) {

                    if (vertexPartsList[1].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[1]);
                        vertex.texture = vertexTextureList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[2].length() != 0) {
                        int positionIndex = Integer.parseInt(vertexPartsList[2]);
                        vertex.normal = vertexNormalList.get(positionIndex - 1);
                    }

                    if (vertexPartsList[0].length() != 0) {

                        int positionIndex = Integer.parseInt(vertexPartsList[0]);
                        vertex.position = vertexPositionList.get(positionIndex - 1);
                    }

                    polygon.add(vertex);

                } else {
                    System.err.println("Warning: ObjToV3Draw - bad obj format. " + vertexString);
                    return;
                }
            }

            polygonList.add(polygon);

        } else {
            System.err.println("Warning: ObjToV3Draw - bad obj format. " + line);
            return;
        }

    }

    private void flush() throws IOException {
        // vertexPositionList.clear();
        // vertexTextureList.clear();
        // vertexNormalList.clear();

        if (textureMapName.contains(currentName)) {

            writer.enableTexture(textureMapName.indexOf(currentName));
        }

        if (!triangleList.isEmpty()) {
            writer.draw3dTriangleList(triangleList);
        }

        if (!quadList.isEmpty()) {
            writer.draw3dQuadList(quadList);
        }

        for (V3DrawPolygon polygon : polygonList) {
            writer.drawRegistered3dConcavePolygon(writer.register3dConcavePolygon(polygon));
        }

        if (textureMapName.contains(currentName)) {
            writer.disableTexture();
        }

        triangleList.clear();
        quadList.clear();
        polygonList.clear();

    }

    public void setTexture(String string, File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] image = new byte[inputStream.available()];
        inputStream.read(image);
        textureMapName.add(string);
        textureMapData.add(image);
        inputStream.close();
    }

    private void writeTextures() throws IOException {
        for (byte[] image : textureMapData) {
            writer.registerTexture(image);
        }
    }

    private void updateBounds(float x, float y, float z) {
        if (x < minX) {
            minX = x;
        }

        if (x > maxX) {
            maxX = x;
        }

        if (y < minY) {
            minY = y;
        }

        if (y > maxY) {
            maxY = y;
        }

        if (z < minZ) {
            minZ = z;
        }

        if (z > maxZ) {
            maxZ = z;
        }
    }

    public void setAcceptedObjects(List<String> acceptedObjects) {
        this.acceptedObjects = acceptedObjects;
    }
}
