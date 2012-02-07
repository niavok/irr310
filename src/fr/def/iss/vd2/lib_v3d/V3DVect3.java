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

package fr.def.iss.vd2.lib_v3d;

/**
 * Vector of 3 float representing a distance, a point, ...
 * 
 * @author fberto
 */
public class V3DVect3 {
    public static final V3DVect3 ONE = new V3DVect3(1, 1, 1);
    public static final V3DVect3 ZERO = new V3DVect3(0, 0, 0);
    public float x;
    public float y;
    public float z;

    /**
     * Initialise from float
     * 
     * @param x
     * @param y
     * @param z
     */
    public V3DVect3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy another vector
     * 
     * @param vector copied vector
     */
    public V3DVect3(V3DVect3 vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    /**
     * Display the vector values
     * 
     * @return
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    /**
     * Return a new vector with each value mutiplied by a factor
     * 
     * @param factor multiplication factor
     * @return new multiplied vector
     */
    public V3DVect3 multiply(float factor) {
        return new V3DVect3(x * factor, y * factor, z * factor);
    }

    /**
     * Return a new vector with each value multiplied by the corresponding value
     * of the input factor vector
     * 
     * @param factor input factor vector
     * @return new multiplied vector
     */
    public V3DVect3 multiply(V3DVect3 factor) {

        return new V3DVect3(x * factor.x, y * factor.y, z * factor.z);
    }

    /**
     * Return a new vector with each value divided by the corresponding value of
     * the input divider vector
     * 
     * @param divider input divider vector
     * @return new divided vector
     */
    public V3DVect3 divideBy(V3DVect3 divider) {
        if (divider.z == 0 && z == 0) {
            return new V3DVect3(x / divider.x, y / divider.y, 1);
        }
        return new V3DVect3(x / divider.x, y / divider.y, z / divider.z);
    }

    /**
     * Replace the current values with the values of the source vector
     * 
     * @param source source vector
     */
    public void copy(V3DVect3 source) {
        x = source.x;
        y = source.y;
        z = source.z;
    }

    /**
     * Return a new vector with each value substrated by the corresponding value
     * of the input subtract vector
     * 
     * @param divider input substract vector
     * @return new substrated vector
     */
    public V3DVect3 substract(V3DVect3 substrater) {
        return new V3DVect3(x - substrater.x, y - substrater.y, z - substrater.z);
    }

    /**
     * Return a new vector with each value incremented by the corresponding
     * value of the input increment vector
     * 
     * @param divider input increment vector
     * @return new incremented vector
     */
    public V3DVect3 add(V3DVect3 incrementer) {
        return new V3DVect3(x + incrementer.x, y + incrementer.y, z + incrementer.z);
    }

    /**
     * Return a new vector with each value divided by a factor
     * 
     * @param factor division factor
     * @return new divided vector
     */
    public V3DVect3 divideBy(float factor) {
        return new V3DVect3(x / factor, y / factor, z / factor);
    }

    /**
     * Return the values as a double array
     * 
     * @return dump array, size is 3
     */
    public double[] toDoubleArray() {
        double[] array = new double[3];

        array[0] = x;
        array[1] = y;
        array[2] = z;

        return array;

    }

    /**
     * Return the values as a float array
     * 
     * @return dump array, size is 3
     */
    public float[] toFloatArray() {
        float[] array = new float[3];

        array[0] = x;
        array[1] = y;
        array[2] = z;

        return array;
    }

    /**
     * Return the distance between the two point represented by the current
     * vector and the target vector
     * 
     * @param target target vector
     * @return distance. Always positive
     */
    public float distanceTo(V3DVect3 target) {
        V3DVect3 delta = substract(target);
        float factor = delta.x * delta.x + delta.y * delta.y + delta.z * delta.z;
        return (float) Math.sqrt(factor);
    }

    /**
     * Return the norm of the vector
     * 
     * @return norm
     */
    public float getNorm() {
        float factor = x * x + y * y + z * z;
        return (float) Math.sqrt(factor);
    }

    /**
     * Return a new vector oposite to the current vector
     * 
     * @return new oposite vector
     */
    public V3DVect3 negatif() {
        return new V3DVect3(-x, -y, -z);
    }

    /**
     * Return the angle in radian between the current vector in plan x,y and the
     * vector (1,0,0)
     * 
     * @return return angle
     */
    public float getAngle() {
        return (float) Math.atan2(y, x);
    }

    /**
     * Returns true if the other vector has the same values as the others,
     * otherwise returns false
     * 
     * @param other other vector
     * @return
     */
    public boolean isSame(V3DVect3 other) {

        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

}
