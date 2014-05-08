package com.irr310.common.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformMatrix {

    public double[] transform;

    public TransformMatrix() {
        transform = new double[16];
    }

    public TransformMatrix(TransformMatrix transform) {
        set(transform.getData());
    }

    public DoubleBuffer toDoubleBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(transform.length * 8);
        bb.order(ByteOrder.nativeOrder());
        DoubleBuffer fb = bb.asDoubleBuffer();
        fb.put(transform);
        fb.position(0);
        return fb;

    }
    
    public FloatBuffer toFloatBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(transform.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        for (int j = 0; j < 16; j++) {
            fb.put((float) transform[j]);
        }
        fb.position(0);
        return fb;

    }

    public void set(double[] transform) {
        for (int j = 0; j < 16; j++) {
            assert(transform[j] != Double.NaN);
        }
        this.transform = transform;

    }

    private void set(int i, int j, double val) {
        assert(val != Double.NaN);
        transform[j * 4 + i] = val;
    }

    public interface TransformMatrixChangeListener {

        public void valueChanged();
    }

    public double[] getData() {
        return transform;
    }

    public TransformMatrix translate(Vec3 vect) {
        translate(vect.x, vect.y, vect.z);
        return this;
    }

    public void translate(double x, double y, double z) {
        TransformMatrix tmp = TransformMatrix.identity();

        tmp.set(0, 3, x);
        tmp.set(1, 3, y);
        tmp.set(2, 3, z);

        preMultiply(tmp);

    }

    public void setTranslation(double x, double y, double z) {
        set(0, 3, x);
        set(1, 3, y);
        set(2, 3, z);
    }
    
    public void setTranslation(Vec3 vect) {
        set(0, 3, vect.x);
        set(1, 3, vect.y);
        set(2, 3, vect.z);
    }

    public TransformMatrix preMultiply(TransformMatrix mat) { // mat × this
        TransformMatrix tmp = new TransformMatrix();
        double f;

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                f = 0;
                for (int k = 0; k < 4; k++)
                    f += mat.get(i, k) * get(k, j);
                tmp.set(i, j, f);
            }
        }
        set(tmp.getData());
        return this;
    }

    public double get(int i, int j) {
        return transform[j * 4 + i];
    }

    public Vec3 getTranslation() {
        return new Vec3(get(0, 3), get(1, 3), get(2, 3));
    }

    public static TransformMatrix identity() {
        TransformMatrix mat = new TransformMatrix();
        mat.set(0, 0, 1);
        mat.set(1, 1, 1);
        mat.set(2, 2, 1);
        mat.set(3, 3, 1);

        return mat;
    }

    public void rotateX(double theta) { // rotate transformation about the X
                                        // axis

        TransformMatrix tmp = TransformMatrix.identity();
        double c = Math.cos(theta);
        double s = Math.sin(theta);

        tmp.set(1, 1, (float) c);
        tmp.set(1, 2, (float) -s);
        tmp.set(2, 1, (float) s);
        tmp.set(2, 2, (float) c);

        preMultiply(tmp);
    }

    public void rotateY(double theta) { // rotate transformation about the Y
                                        // axis

        TransformMatrix tmp = TransformMatrix.identity();
        double c = Math.cos(theta);
        double s = Math.sin(theta);

        tmp.set(2, 2, (float) c);
        tmp.set(2, 0, (float) -s);
        tmp.set(0, 2, (float) s);
        tmp.set(0, 0, (float) c);

        preMultiply(tmp);
    }

    public void rotateZ(double theta) { // rotate transformation about the Z
                                        // axis

        TransformMatrix tmp = TransformMatrix.identity();
        double c = Math.cos(theta);
        double s = Math.sin(theta);

        tmp.set(0, 0, (float) c);
        tmp.set(0, 1, (float) -s);
        tmp.set(1, 0, (float) s);
        tmp.set(1, 1, (float) c);

        preMultiply(tmp);
    }

    public void rotate(Vec3 rotation) {
        rotateX(Math.toRadians(rotation.x));
        rotateY(Math.toRadians(rotation.y));
        rotateZ(Math.toRadians(rotation.z));
    }

    public Vec3 getRotation() {

        return null;
    }

    public TransformMatrix inverse() {

        TransformMatrix tmp = TransformMatrix.identity();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                tmp.set(i, j, get(j, i));
            }
        }

        Vec3 translation = getTranslation().negative();
        Vec3 rotatedTranslation = translation.rotate(tmp);
        tmp.setTranslation(rotatedTranslation);

        
        // TODO Auto-generated method stub
        return tmp;
    }

    public TransformMatrix scale(double scale) {
        setTranslation(getTranslation().multiply(scale));
        return this;
    }

    /*
     * public void getLocalVectorToGlobalVector(Vect3 vect3) { TransformMatrix
     * force = TransformMatrix.identity(); force.translate(new Vect3(0,
     * linearEngine.getLeft().getCurrentThrust(), 0)); TransformMatrix rotation
     * = new TransformMatrix(); t.getOpenGLMatrix(getData());
     * rotation.setTranslation(0, 0, 0); force.preMultiply(rotation);
     * rotation.getLocalVectorToGlobalVector(new Vect3(0,
     * linearEngine.getLeft().getCurrentThrust(), 0));
     * body.applyCentralForce(force.getTranslation().toVector3d()); }
     */

    @Override
    public String toString() {
        return "[" +
                transform[0] + "," +
                transform[1] + "," +
                transform[2] + "," +
                transform[3] + "," +
                transform[4] + "," +
                transform[5] + "," +
                transform[6] + "," +
                transform[7] + "," +
                transform[8] + "," +
                transform[9] + "," +
                transform[10] + "," +
                transform[11] + "," +
                transform[12] + "," +
                transform[13] + "," +
                transform[14] + "," +
                transform[15] + "]";
    }

    // TODO keep reference only during parsing
    public static Pattern transformMatrixPattern = Pattern.compile("^\\[([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+),([-0-9]+\\.[-0-9E]+)\\]$");


    public static TransformMatrix parseTransformMatrix(String vec3String) {
        Matcher matcherTransformMatrix = transformMatrixPattern.matcher(vec3String);
        if(matcherTransformMatrix.matches()) {
            TransformMatrix transformMatrix = new TransformMatrix();

            double[] values = new double[16];
            for(int i = 0; i < 16; i++) {
                values[i] = Double.parseDouble(matcherTransformMatrix.group(i+1));
            }
            transformMatrix.set(values);
            return transformMatrix;
        }
        return null;
    }

}
