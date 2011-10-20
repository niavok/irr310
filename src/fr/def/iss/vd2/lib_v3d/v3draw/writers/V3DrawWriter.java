package fr.def.iss.vd2.lib_v3d.v3draw.writers;

import java.io.IOException;
import java.util.List;

import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawPolygon;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawQuad;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawTriangle;

/**
 *
 * @author fberto
 */
public interface V3DrawWriter {

    public void write() throws IOException;

    public void addMetadata(String metadata);

    public void setBounds(float minX, float maxX, float minY, float maxY, float minZ, float maxZ);

    /**
     * Draw a polyline : list of joined lines
     * @param list of lines
     */
    public void draw2dLineStripList(List<List<V3DVect3>> lines) throws IOException;

    /**
     * Register a concave polygon for future draw
     * @param polygon
     * @return index of registered polygon
     */
    public int register3dConcavePolygon(V3DrawPolygon polygon) throws IOException;

    /**
     * Draw a list of triangle.
     * @param triangleList list of triangle. A triangle is a list of 3 vertex
     * description. A vertex description is a list of point representing the
     * the texture coord, the normal coord and the position.
     */
    public void draw3dTriangleList(List<V3DrawTriangle> triangleList) throws IOException;

    /**
     * Draw a list of quad.
     * @param quadList list of triangle. A quad is a list of 4 vertex
     * description. A vertex description is a list of point representing the
     * the texture coord, the normal coord and the position.
     */
    public void draw3dQuadList(List<V3DrawQuad> quadList) throws IOException;

    /**
     * Draw a list of polygon.
     * @param polygon list of triangle. A quad is a list of n vertex
     * description. A vertex description is a list of point representing the
     * the texture coord, the normal coord and the position.
     */
    public void draw3dPolygonList(List<V3DrawPolygon> polygonList) throws IOException;

    public void drawRegistered3dConcavePolygon(int polygonIndex) throws IOException;

    public void enableTexture(int imageIndex) throws IOException;

    public void disableTexture() throws IOException;

    public void registerTexture(byte[] image) throws IOException;

    public void setLineThickness(float thickness) throws IOException;

    public void setPointThickness(float thickness) throws IOException;
}
