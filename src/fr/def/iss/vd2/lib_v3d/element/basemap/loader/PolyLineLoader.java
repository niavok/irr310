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

package fr.def.iss.vd2.lib_v3d.element.basemap.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fberto
 */
public class PolyLineLoader {

    //List<Float> vertices;
    //List<Integer> stripVertexCounts;
    FloatBuffer vertices;
    IntBuffer stripVertexCounts;

    public float minX = Integer.MAX_VALUE;
    public float maxX = Integer.MIN_VALUE;
    public float minY = Integer.MAX_VALUE;
    public float maxY = Integer.MIN_VALUE;


    public void load(File lineFile) throws FileNotFoundException {
        File dumpFile = new File(lineFile.getAbsolutePath()+".vertices.dump");
        File dumpLineFile = new File(lineFile.getAbsolutePath()+".lines.dump");
        File dumpInfoFile = new File(lineFile.getAbsolutePath()+".infos.dump");
        if(dumpFile.exists() && dumpLineFile.exists() && dumpInfoFile.exists())
        {
            try {
                loadDump(dumpFile, dumpLineFile, dumpInfoFile);
            } catch (IOException ex) {
                Logger.getLogger(PolyLineLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            loadFile(lineFile);
        }

    }

    public boolean isDump(File lineFile) {
        File dumpFile = new File(lineFile.getAbsolutePath()+".vertices.dump");
        File dumpLineFile = new File(lineFile.getAbsolutePath()+".lines.dump");
        File dumpInfoFile = new File(lineFile.getAbsolutePath()+".infos.dump");
        return dumpFile.exists() && dumpLineFile.exists() && dumpInfoFile.exists();
        
    }

    public void loadDump(File dumpFile, File lineFile, File dumpInfoFile) throws FileNotFoundException, IOException {

        FileInputStream vertexStream = new FileInputStream(dumpFile);
        MappedByteBuffer inVertex = vertexStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, dumpFile.length());
        vertices = inVertex.asFloatBuffer();

        FileInputStream vertexLineStream = new FileInputStream(lineFile);
        MappedByteBuffer inLine;
        inLine = vertexLineStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lineFile.length());
        stripVertexCounts = inLine.asIntBuffer();


        Scanner scanner=new Scanner(dumpInfoFile);

        String line;
        
        line = scanner.nextLine();
        minX = Float.parseFloat(line);
        line = scanner.nextLine();
        maxX = Float.parseFloat(line);
        line = scanner.nextLine();
        minY = Float.parseFloat(line);
        line = scanner.nextLine();
        maxY = Float.parseFloat(line);

        scanner.close();
        vertexLineStream.close();
        vertexStream.close();
    }

    public void loadFile(File lineFile) throws FileNotFoundException {


        Scanner scanner=new Scanner(lineFile);

        
        List<Integer> stripVertexCountsArray = new ArrayList<Integer>();
        List<Float> verticesArray = new ArrayList<Float>();
        

        // On boucle sur chaque champ detecté
        while (scanner.hasNextLine()) {
//            if (totalVertexCount > 50000) {
//                break;
//            }
            String line = scanner.nextLine();

            if(line.startsWith("#") || line.length() == 0) {
                continue;
            }

            int pointCount = 0;

            //System.out.println("Line");
            while(line.length() > 0) {
                //System.out.println("Point");
                int pointEndOffset = line.indexOf("/");
                if(pointEndOffset == -1 ) {
                    //No more separator
                    break;
                }
                String point = line.substring(0, line.indexOf("/"));
                line = line.substring(point.length()+1);

                //System.out.println("point : "+point+" Line : "+line +" "+ line.length());

                int sep =  point.indexOf(",");
                String pointX = point.substring(0, sep);
                String pointY = point.substring(sep+1);

                //System.out.println("pointX : "+pointX +" "+ pointX.length()+" pointY : "+pointY +" "+ pointY.length());

                float x = Float.parseFloat(pointX)/50.0f;
                float y = -Float.parseFloat(pointY)/50.0f;

                if(x < minX) {
                    minX = x;
                }

                if(y < minY) {
                    minY = y;
                }

                if(x > maxX) {
                    maxX = x;
                }

                if(y > maxY) {
                    maxY = y;
                }

                verticesArray.add(x);
                verticesArray.add(y);
                pointCount++;

                  //System.out.println("Trouvé :" +x0 + " " + y0 +" - " +x1 + " " + y1);
                /*} else
                {   //TODO traiter les lines vides et
                    System.out.println("Invalid line : "+line);
                    break;
                }*/
            }
            stripVertexCountsArray.add(pointCount);

        }

        
        scanner.close();



        stripVertexCounts = IntBuffer.allocate(stripVertexCountsArray.size());
        for(int i = 0; i< stripVertexCountsArray.size(); i++) {
            stripVertexCounts.put(stripVertexCountsArray.get(i));
        }
        stripVertexCounts.flip();

        vertices = FloatBuffer.allocate(verticesArray.size());
        for(int i = 0; i< verticesArray.size(); i++) {
            vertices.put(verticesArray.get(i));
        }
        vertices.flip();



    }

    public FloatBuffer getVertices() {
        return vertices;
    }

    public IntBuffer getStripVertexCounts() {
        return stripVertexCounts;
    }

    public void clear() {
        vertices = null;
        stripVertexCounts = null;
    }

    public void dump(File lineFile) throws FileNotFoundException, IOException {
        long start = System.currentTimeMillis();

        {
            File dumpFile = new File(lineFile.getAbsolutePath()+".vertices.dump");
            RandomAccessFile randomAccessFile = new RandomAccessFile(dumpFile,"rw");
            FileChannel fc = randomAccessFile.getChannel();
            dumpFile.createNewFile();
            FloatBuffer outVertex = fc.map(FileChannel.MapMode.READ_WRITE, 0, vertices.capacity()*4).asFloatBuffer();
            vertices.rewind();
            outVertex.put(vertices);
            fc.close();
            randomAccessFile.close();
        }

        {
            File dumpFile = new File(lineFile.getAbsolutePath()+".lines.dump");
            RandomAccessFile randomAccessFile = new RandomAccessFile(dumpFile,"rw");
            FileChannel fc =randomAccessFile.getChannel();
            dumpFile.createNewFile();
            IntBuffer outVertex = fc.map(FileChannel.MapMode.READ_WRITE, 0, stripVertexCounts.capacity()*4).asIntBuffer();
            outVertex.put(stripVertexCounts);
            fc.close();
            randomAccessFile.close();
        }

        {
            File dumpFile = new File(lineFile.getAbsolutePath()+".infos.dump");
            FileWriter writer = new FileWriter(dumpFile,false);
            
            writer.write(Float.toString(minX)+"\n");
            writer.write(Float.toString(maxX)+"\n");
            writer.write(Float.toString(minY)+"\n");
            writer.write(Float.toString(maxY)+"\n");

            writer.close();
        }

        long end = System.currentTimeMillis();

        
        System.out.println("Elapsed millis: " + (end - start));



    }

}
