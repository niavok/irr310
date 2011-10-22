package com.irr310.server;

import fr.def.iss.vd2.lib_v3d.V3DVect3;

public class Vect3 {

	public final Double x;
	public final Double y;
	public final Double z;
	
	public static Vect3 ORIGIN = new Vect3(0, 0, 0);
	public static Vect3 ONE = new Vect3(1, 1, 1);

	public Vect3(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vect3(int x, int y, int z) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}

	public V3DVect3 toV3DVect3() {
		return new V3DVect3(x.floatValue(), y.floatValue(), z.floatValue());
	}

}
