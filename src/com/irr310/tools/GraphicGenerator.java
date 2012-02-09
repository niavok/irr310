package com.irr310.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import fr.def.iss.vd2.lib_v3d.v3draw.writers.DefaultV3DrawWriter;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.ObjToV3Draw;
import fr.def.iss.vd2.lib_v3d.v3draw.writers.V3DrawWriter;

public class GraphicGenerator {

    public static void main(String[] args) throws IOException {
        V3DrawWriter writer = new DefaultV3DrawWriter(new File("graphics/output/big_propeller.v3draw"));

        writer.addMetadata("date: " + new Date().toString());
        writer.addMetadata("author: Frédéric Bertolus");
        writer.addMetadata("licence: CC-BY-SA");
        writer.addMetadata("url: https://github.com/fredb219/irr310");


        ObjToV3Draw objConverter = new ObjToV3Draw(new File("graphics/big_propeller.obj"));

        objConverter.write(writer);

        writer.write();
    }
}
