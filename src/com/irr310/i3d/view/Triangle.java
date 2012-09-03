package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.Color;
import com.irr310.i3d.I3dMesure;

public class Triangle extends View {

    private Color backgroundColor;

    
    private I3dMesure[] mesures;
    private Point[] points;
    
    
    public Triangle(Graphics g) {
        super(g);
        backgroundColor = Color.pink;
    }

    @Override
    public void draw() {
        g.setColor(backgroundColor);
        g.drawTriangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y, false);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    public View duplicate() {
        Triangle view = new Triangle(g);
        view.setBackgroundColor(backgroundColor);
        I3dMesure[] mesures = new I3dMesure[3];
        for(int i=0; i < 3; i++) {
            mesures[i] = this.mesures[i];
        }
        view.setPoints(mesures);
        
        return view;
    }

    public void setPoints(I3dMesure[] mesures) {
        this.mesures = mesures;
        /*size = new Dimension(Math.max(Math.max(x1, x2), x3) - Math.min(Math.min(x1, x2), x3), Math.max(Math.max(y1, y2), y3)
        - Math.min(Math.min(y1, y2), y3));*/
    }
    
    public boolean computeSize() {
        points = new Point[3];
        points[0] = computeMesure(mesures[0]);
        points[1] = computeMesure(mesures[1]);
        points[2] = computeMesure(mesures[2]);
        return true;
    }
}
