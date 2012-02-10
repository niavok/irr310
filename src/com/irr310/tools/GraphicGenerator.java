package com.irr310.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.def.iss.vd2.lib_v3d.v3draw.writers.DefaultV3DrawWriter;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.ObjToV3Draw;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.V3DrawWriter;

public class GraphicGenerator {

    public static void main(String[] args) throws IOException {

        // Propeller
        {
            List<String> objectsStator = new ArrayList<String>();
            objectsStator.add("fairing");
            objectsStator.add("support");

            generateObj("graphics/big_propeller.obj", "graphics/output/big_propeller_stator.v3draw", objectsStator);

            List<String> objectsRotor = new ArrayList<String>();
            objectsRotor.add("propeller");
            generateObj("graphics/big_propeller.obj", "graphics/output/big_propeller_rotor.v3draw", objectsRotor);
        }

        // Pv cell
        {
            List<String> objectsStructure = new ArrayList<String>();
            objectsStructure.add("structure");

            generateObj("graphics/pvcell.obj", "graphics/output/pvcell_structure.v3draw", objectsStructure);

            List<String> objectsPanel = new ArrayList<String>();
            objectsPanel.add("panel");
            generateObj("graphics/pvcell.obj", "graphics/output/pvcell_panel.v3draw", objectsPanel);

        }
        
        //Camera
        generateObj("graphics/camera.obj", "graphics/output/camera.v3draw", null);
        
        //Factory
        generateObj("graphics/factory.obj", "graphics/output/factory.v3draw", null);
        
       //Tank
        {
            List<String> objectsStructure = new ArrayList<String>();
            objectsStructure.add("structure");

            generateObj("graphics/tank.obj", "graphics/output/tank_structure.v3draw", objectsStructure);

            List<String> objectsTank = new ArrayList<String>();
            objectsTank.add("tank");
            generateObj("graphics/tank.obj", "graphics/output/tank_tank.v3draw", objectsTank);

        }
    }

    private static void generateObj(String source, String destination, List<String> objects) throws IOException {
        ObjToV3Draw objConverter = new ObjToV3Draw(new File(source));

        V3DrawWriter writer = new DefaultV3DrawWriter(new File(destination));

        writer.addMetadata("author: Frédéric Bertolus");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: https://github.com/fredb219/irr310");

        objConverter.setAcceptedObjects(objects);
        // objConverter.setConfig("graphics/big_propeller.v3dconf");
        objConverter.write(writer);
        writer.write();

    }
}
