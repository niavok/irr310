package com.irr310.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import fr.def.iss.vd2.lib_v3d.V3DVect3;

public class RotationMatrix {

	public float[] rotation;

	private List<RotationMatrixChangeListener> changeListeners;

	public RotationMatrix() {
		rotation = new float[16];
		rotation[0] = 1;
		rotation[5] = 1;
		rotation[10] = 1;
		rotation[15] = 1;

		changeListeners = new ArrayList<RotationMatrix.RotationMatrixChangeListener>();
	}

	public FloatBuffer toFloatBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(rotation.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(rotation);
		fb.position(0);
		return fb;

	}

	public void set(float[] rotation) {
		this.rotation = rotation;
		fireChanged();
	}

	public void addListener(RotationMatrixChangeListener listener) {
		changeListeners.add(listener);
	}

	public void fireChanged() {
		for (RotationMatrixChangeListener listener : changeListeners) {
			listener.valueChanged();
		}
	}

	public interface RotationMatrixChangeListener {

		public void valueChanged();
	}

	public float[] getData() {
		return rotation;
	}

}
