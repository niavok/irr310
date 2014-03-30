package com.irr310.i3d.view;

import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.MeasurePoint;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class Triangle extends View {

    private Color backgroundColor;

    
    private MeasurePoint[] mesures;
    private Point[] points;
    
    
    public Triangle() {
        backgroundColor = Color.pink;
    }

    @Override
    public void onDraw(Graphics g) {
        g.setColor(backgroundColor);
        g.drawTriangle(points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y, false);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    public View duplicate() {
        Triangle view = new Triangle();
        duplicateTo(view);
        return view;
    }
    
    @Override
    protected void duplicateTo(View view) {
        super.duplicateTo(view);
        Triangle myView = (Triangle) view;
        myView.setBackgroundColor(backgroundColor);
        MeasurePoint[] mesures = new MeasurePoint[3];
        for(int i=0; i < 3; i++) {
            mesures[i] = this.mesures[i];
        }
        myView.setPoints(mesures);
    }

    public void setPoints(MeasurePoint[] mesures) {
        this.mesures = mesures;
        /*size = new Dimension(Math.max(Math.max(x1, x2), x3) - Math.min(Math.min(x1, x2), x3), Math.max(Math.max(y1, y2), y3)
        - Math.min(Math.min(y1, y2), y3));*/
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        points = new Point[3];
        points[0] = new Point(mLayoutParams.computeMesure(mesures[0].getX()), mLayoutParams.computeMesure(mesures[0].getY()));
        points[1] = new Point(mLayoutParams.computeMesure(mesures[1].getX()), mLayoutParams.computeMesure(mesures[1].getY()));
        points[2] = new Point(mLayoutParams.computeMesure(mesures[2].getX()), mLayoutParams.computeMesure(mesures[2].getY()));
    }

    @Override
    public void onMeasure() {
    }
}
