package com.irr310.common.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TransformMatrix {

    public float[] transform;

    public TransformMatrix() {
        transform = new float[16];
    }

    public FloatBuffer toFloatBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(transform.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(transform);
        fb.position(0);
        return fb;

    }

    public void set(float[] transform) {
        this.transform = transform;

    }

    private void set(int i, int j, float val) {
        transform[j * 4 + i] = val;
    }

    public interface TransformMatrixChangeListener {

        public void valueChanged();
    }

    public float[] getData() {
        return transform;
    }

    public void translate(Vec3 vect) {
        translate(vect.x.floatValue(), vect.y.floatValue(), vect.z.floatValue());
    }

    public void translate(float x, float y, float z) {
        TransformMatrix tmp = TransformMatrix.identity();

        tmp.set(0, 3, x);
        tmp.set(1, 3, y);
        tmp.set(2, 3, z);

        preMultiply(tmp);

    }

    public void setTranslation(float x, float y, float z) {
        set(0, 3, x);
        set(1, 3, y);
        set(2, 3, z);
    }
    
    public void setTranslation(Vec3 vect) {
        set(0, 3, vect.x.floatValue());
        set(1, 3, vect.y.floatValue());
        set(2, 3, vect.z.floatValue());
    }

    public void preMultiply(TransformMatrix mat) { // mat × this
        TransformMatrix tmp = new TransformMatrix();
        float f;

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                f = 0;
                for (int k = 0; k < 4; k++)
                    f += mat.get(i, k) * get(k, j);
                tmp.set(i, j, f);
            }
        }
        set(tmp.getData());
    }

    public float get(int i, int j) {
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

    /*
     * public void getLocalVectorToGlobalVector(Vect3 vect3) { TransformMatrix
     * force = TransformMatrix.identity(); force.translate(new Vect3(0,
     * linearEngine.getLeft().getCurrentThrust(), 0)); TransformMatrix rotation
     * = new TransformMatrix(); t.getOpenGLMatrix(getData());
     * rotation.setTranslation(0, 0, 0); force.preMultiply(rotation);
     * rotation.getLocalVectorToGlobalVector(new Vect3(0,
     * linearEngine.getLeft().getCurrentThrust(), 0));
     * body.applyCentralForce(force.getTranslation().toVector3f()); }
     */

}
