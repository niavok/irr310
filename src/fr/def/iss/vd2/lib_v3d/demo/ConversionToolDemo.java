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
package fr.def.iss.vd2.lib_v3d.demo;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.DefaultV3DrawWriter;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.ObjToV3Draw;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.PolysToV3Draw;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.V3DrawWriter;

/**
 *
 * @author fberto
 */
public class ConversionToolDemo extends JFrame {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DSimple2DCamera activeCamera;
    V3DCanvas canvas;

    public ConversionToolDemo() {


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());


        mainPanel.add(new JPanel(), BorderLayout.CENTER);


        setContentPane(mainPanel);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        //JFrame frame = new ConversionTool();

        //frame.setVisible(true);

        new File("graphics/output").mkdirs();


        //writeFromPoly();

        writeFromObj();


    }

    private static void writeFromPoly() throws FileNotFoundException, IOException {
        V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/big_propeller.v3draw"));

        writer.addMetadata("date: " + new Date().toString());
        writer.addMetadata("author: Frédéric Bertolus");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: http://www.def-online.com");


        PolysToV3Draw polysConverter = new PolysToV3Draw(new File("/home/fberto/test.polys"));

        polysConverter.write(writer);

        writer.write();
    }

    private static void writeFromObj() throws FileNotFoundException, IOException {

        generateBuilding();
        generateGround();
        generateBoule();

        {
            V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/cube.v3draw"));

            writer.addMetadata("date: " + new Date().toString());
            writer.addMetadata("author: DEF ISS");
            writer.addMetadata("licence: CC-BY-SA");
            writer.addMetadata("url: http://www.def-online.com");


            ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/cube.obj"));

            ObjConverter.write(writer);

            writer.write();

        }

        {
            V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/cube2.v3draw"));

            writer.addMetadata("date: " + new Date().toString());
            writer.addMetadata("author: DEF ISS");
            writer.addMetadata("licence: CC-BY-SA");
            writer.addMetadata("url: http://www.def-online.com");


            ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/cube2.obj"));

            ObjConverter.write(writer);

            writer.write();

        }

        {
            V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/polygon.v3draw"));

            writer.addMetadata("date: " + new Date().toString());
            writer.addMetadata("author: DEF ISS");
            writer.addMetadata("licence: CC-BY-SA");
            writer.addMetadata("url: http://www.def-online.com");


            ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/polygon.obj"));

            ObjConverter.write(writer);

            writer.write();

        }



    }

    private static void generateBuilding() throws IOException {
        V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/building.v3draw"));

        writer.addMetadata("date: " + new Date().toString());
        writer.addMetadata("author: DEF ISS");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: http://www.def-online.com");

        ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/building.obj"));

        ObjConverter.setTexture("", new File("graphics/obj_demo/baked_building_2048.png"));
        ObjConverter.write(writer);

        writer.write();
    }

    private static void generateGround() throws IOException {
        V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/ground.v3draw"));

        writer.addMetadata("date: " + new Date().toString());
        writer.addMetadata("author: DEF ISS");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: http://www.def-online.com");


        ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/ground.obj"));

        ObjConverter.setTexture("", new File("graphics/obj_demo/baked_ground.png"));
        ObjConverter.write(writer);

        writer.write();
    }

    private static void generateBoule() throws IOException {
        V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/boule.v3draw"));

        writer.addMetadata("date: " + new Date().toString());
        writer.addMetadata("author: DEF ISS");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: http://www.def-online.com");


        ObjToV3Draw ObjConverter = new ObjToV3Draw(new File("graphics/obj_demo/boule.obj"));

        ObjConverter.write(writer);

        writer.write();
    }
}
