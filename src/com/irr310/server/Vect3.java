package com.irr310.server;

public class Vect3 {

	public final Double x;
	public final Double y;
	public final Double z;

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

}
