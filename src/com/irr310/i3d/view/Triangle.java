package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.Color;
import com.irr310.i3d.Measure;
import com.irr310.i3d.MeasurePoint;

public class Triangle extends View {

    private Color backgroundColor;

    
    private MeasurePoint[] mesures;
    private Point[] points;
    
    
    public Triangle(Graphics g) {
        super(g);
        backgroundColor = Color.pink;
    }

    @Override
    public void doDraw() {
        g.setColor(backgroundColor);
        g.drawTriangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y, false);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    public View duplicate() {
        Triangle view = new Triangle(g);
        view.setId(getId());
        view.setBackgroundColor(backgroundColor);
        MeasurePoint[] mesures = new MeasurePoint[3];
        for(int i=0; i < 3; i++) {
            mesures[i] = this.mesures[i];
        }
        view.setPoints(mesures);
        view.setLayout(getLayout().duplicate());
        
        return view;
    }

    public void setPoints(MeasurePoint[] mesures) {
        this.mesures = mesures;
        /*size = new Dimension(Math.max(Math.max(x1, x2), x3) - Math.min(Math.min(x1, x2), x3), Math.max(Math.max(y1, y2), y3)
        - Math.min(Math.min(y1, y2), y3));*/
    }
    
    public boolean doLayout(Layout parentLayout) {
        points = new Point[3];
        points[0] = new Point(layout.computeMesure(mesures[0].getX()), layout.computeMesure(mesures[0].getY()));
        points[1] = new Point(layout.computeMesure(mesures[1].getX()), layout.computeMesure(mesures[1].getY()));
        points[2] = new Point(layout.computeMesure(mesures[2].getX()), layout.computeMesure(mesures[2].getY()));
        return true;
    }
}
