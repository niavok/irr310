package com.irr310.server.game.world;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public abstract class WorldObject extends GameEntity {

	private final Vect3 position;
	private Double mass;
	private final Vect3 rotationSpeed;
	private final Vect3 linearSpeed;
	private final Vect3 rotation;
	private String name;
	private Shape shape;
	
	public WorldObject() {
		position = Vect3.origin();
		rotationSpeed = Vect3.origin();
		linearSpeed = Vect3.origin();
		rotation = Vect3.origin();
		mass = 0.;
		name = "unamed object";
		shape = new Shape(this, Vect3.one());
	}


	public void setMass(Double mass) {
		this.mass = mass;
	}

	public String getName() {
		return getName();
	}

	public Vect3 getPosition() {
		return position;
	}

	public Double getMass() {
		return mass;
	}

	public Vect3 getRotationSpeed() {
		return rotationSpeed;
	}

	public Vect3 getLinearSpeed() {
		return linearSpeed;
	}

	public Vect3 getRotation() {
		return rotation;
	}
	
	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void init() {
		
	}

}
