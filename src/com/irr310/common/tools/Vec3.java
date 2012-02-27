package com.irr310.common.tools;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import fr.def.iss.vd2.lib_v3d.V3DVect3;

public class Vec3 {

    public Double x;
    public Double y;
    public Double z;

    //TODO: try to remove
    private List<Vect3ChangeListener> changeListeners;
    private V3DVect3 v3dVect3;

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        changeListeners = new ArrayList<Vec3.Vect3ChangeListener>();
    }

    public Vec3(int x, int y, int z) {
        this((double) x, (double) y, (double) z);
    }

    public Vec3(float x, float y, float z) {
        this((double) x, (double) y, (double) z);
    }

    public Vec3(Vector3f vect) {
        this(vect.x, vect.y, vect.z);
    }

    public V3DVect3 toV3DVect3() {
        return new V3DVect3(x.floatValue(), y.floatValue(), z.floatValue());
    }

    public Vector3f toVector3f() {
        return new Vector3f(x.floatValue(), y.floatValue(), z.floatValue());
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public void set(float x, float y, float z) {
        this.x = (double) x;
        this.y = (double) y;
        this.z = (double) z;
        fireChanged();
    }

    public void set(Vec3 vect) {
        this.x = vect.x;
        this.y = vect.y;
        this.z = vect.z;
        fireChanged();

    }

    public void set(Vector3f vect) {
        this.x = (double) vect.x;
        this.y = (double) vect.y;
        this.z = (double) vect.z;
        fireChanged();
    }

    public static Vec3 origin() {
        return new Vec3(0, 0, 0);
    }

    public static Vec3 one() {
        return new Vec3(1, 1, 1);
    }

    public void addListener(Vect3ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void fireChanged() {
        for (Vect3ChangeListener listener : changeListeners) {
            listener.valueChanged();
        }
    }

    public interface Vect3ChangeListener {

        public void valueChanged();
    }

    public Vec3 divide(double i) {
        return new Vec3(x / i, y / i, z / i);
    }
    
    public Vec3 multiply(double i) {
        return new Vec3(x * i, y * i, z * i);
    }
    

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double distanceTo(Vec3 vect) {
        return this.diff(vect).length();
    }

    public Vec3 diff(Vec3 v) {
        return new Vec3(v.x - x, v.y - y, v.z - z);
    }

    public Vec3 minus(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }
    
    public Vec3 plus(Vec3 v) {
        return new Vec3(v.x + x, v.y + y, v.z + z);
    }

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + ", z=" + z + "]";
    }

    public Vec3 negative() {
        return new Vec3(-x, -y, -z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vec3 rotate(Vec3 rotation) {
        TransformMatrix tmp = TransformMatrix.identity();

        tmp.translate(this);

        tmp.rotateX(Math.toRadians(rotation.x));
        tmp.rotateY(Math.toRadians(rotation.y));
        tmp.rotateZ(Math.toRadians(rotation.z));

        return tmp.getTranslation();
    }

    public Vec3 transform(TransformMatrix transform) {
        Vec3 out = new Vec3();

        out.x = x * transform.get(0, 0) + y * transform.get(0, 1) + z * transform.get(0, 2) + transform.get(0, 3);
        out.y = x * transform.get(1, 0) + y * transform.get(1, 1) + z * transform.get(1, 2) + transform.get(1, 3);
        out.z = x * transform.get(2, 0) + y * transform.get(2, 1) + z * transform.get(2, 2) + transform.get(2, 3);

        return out;
    }
    
    public Vec3 rotate(TransformMatrix transform) {
        Vec3 out = new Vec3();

        out.x = x * transform.get(0, 0) + y * transform.get(0, 1) + z * transform.get(0, 2);
        out.y = x * transform.get(1, 0) + y * transform.get(1, 1) + z * transform.get(1, 2);
        out.z = x * transform.get(2, 0) + y * transform.get(2, 1) + z * transform.get(2, 2);

        return out;
    }
    

    public double dot(Vec3 vect) {
        return x * vect.x + y * vect.y + z * vect.z;
    }

    public Vec3 normalize() {
        return this.divide(length());
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(y*v.z - z*v.y, z*v.x - x*v.z, x*y - y*v.x);
    }

    


}
