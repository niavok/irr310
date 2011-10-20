/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.def.iss.vd2.lib_v3d.v3draw.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.def.iss.vd2.lib_v3d.V3DVect3;

/**
 *
 * @author fberto
 */
public class PolysToV3Draw {

    private final File polyFile;
    private float minX = Float.POSITIVE_INFINITY;
    private float maxX = Float.NEGATIVE_INFINITY;
    private float minY = Float.POSITIVE_INFINITY;
    private float maxY = Float.NEGATIVE_INFINITY;

    public PolysToV3Draw(File polyFile) {
        this.polyFile = polyFile;
    }

    public void write(V3DrawWriter writer) throws FileNotFoundException, IOException {

        Scanner scanner = new Scanner(polyFile);

        List<List<V3DVect3>> lines = new ArrayList<List<V3DVect3>>();

        // On boucle sur chaque champ detecté
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }

            List<V3DVect3> points = new ArrayList<V3DVect3>();

            while (line.length() > 0) {
                //System.out.println("Point");
                int pointEndOffset = line.indexOf("/");
                if (pointEndOffset == -1) {
                    //No more separator
                    break;
                }
                String point = line.substring(0, line.indexOf("/"));
                line = line.substring(point.length() + 1);

                int sep = point.indexOf(",");
                String pointX = point.substring(0, sep);
                String pointY = point.substring(sep + 1);

                float x = Float.parseFloat(pointX);
                float y = -Float.parseFloat(pointY);

                if (x < minX) {
                    minX = x;
                }

                if (y < minY) {
                    minY = y;
                }

                if (x > maxX) {
                    maxX = x;
                }

                if (y > maxY) {
                    maxY = y;
                }


                points.add(new V3DVect3(x, y, 0));

            }

            lines.add(points);

        }

        writer.draw2dLineStripList(lines);

        if(minX <= maxX && minY <= maxY ) {
            writer.setBounds(minX, maxX, minY, maxY, 0, 0);
        }

        scanner.close();

    }
}
