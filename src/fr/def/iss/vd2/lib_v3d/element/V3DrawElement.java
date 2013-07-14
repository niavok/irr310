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
package fr.def.iss.vd2.lib_v3d.element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.scene.I3dCamera;
import com.irr310.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawReader;
import fr.def.iss.vd2.lib_v3d.v3draw.V3DrawReader.V3DrawBadFormatError;

/**
 * @author fberto
 */
public class V3DrawElement extends I3dElement {

    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    private final V3DrawReader v3drawReader;
    private final float dx;
    private final float dy;
    private final float dz;
    private boolean enableLighting = false;

    public V3DrawElement(ByteBuffer buffer) throws V3DrawBadFormatError {
        super();


        v3drawReader = new V3DrawReader(buffer);

        float w = (v3drawReader.getMaxX() - v3drawReader.getMinX());
        float h = (v3drawReader.getMaxY() - v3drawReader.getMinY());
        float p = (v3drawReader.getMaxZ() - v3drawReader.getMinZ());

        boundingBox.setPosition(0, 0, 0);
        boundingBox.setSize(w, h, p);
        if (p == 0) {
            boundingBox.setFlat(true);
        }
        dx = -(v3drawReader.getMinX() / 2 + v3drawReader.getMaxX() / 2);
        dy = -(v3drawReader.getMinY() / 2 + v3drawReader.getMaxY() / 2);
        dz = -(v3drawReader.getMinZ() / 2 + v3drawReader.getMaxZ() / 2);
    }

    @Override
    protected void doInit() {
        v3drawReader.init();

    }

    @Override
    protected void doDisplay(I3dCamera camera) {

        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);

        /*
         * if (enableLighting) { GL11.glEnable(GL11.GL_LIGHTING); }
         */
        // TODO remove lighting

        

        v3drawReader.draw(camera);

        

        /*
         * if (enableLighting) { GL11.glDisable(GL11.GL_LIGHTING); }
         */
        GL11.glPopMatrix();

    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public V3DrawReader getV3drawReader() {
        return v3drawReader;
    }

    public static V3DrawElement LoadFromFile(File v3drawFile) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(v3drawFile);
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            V3DrawElement v3DrawElement = new V3DrawElement(buffer);
            fileInputStream.close();
            return v3DrawElement;
        } catch (V3DrawBadFormatError ex) {
            Logger.getLogger(V3DrawElement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(V3DrawElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean isEnableLighting() {
        return enableLighting;
    }

    public void setEnableLighting(boolean enableLighting) {
        this.enableLighting = enableLighting;
    }
}
