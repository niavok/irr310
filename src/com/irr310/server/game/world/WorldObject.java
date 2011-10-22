package com.irr310.server.game.world;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public abstract class WorldObject extends GameEntity {

	private Vect3 position;
	private Double mass;
	private Vect3 rotationSpeed;
	private Vect3 linearSpeed;
	private Vect3 rotation;
	private String name;
	private Shape shape;
	private Shape shape2;
	
	public WorldObject() {
	}

	
	public void setPosition(Vect3 position) {
		this.position = position;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public void setRotationSpeed(Vect3 rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public void setLinearSpeed(Vect3 linearSpeed) {
		this.linearSpeed = linearSpeed;
	}

	public void setRotation(Vect3 rotation) {
		this.rotation = rotation;
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
		shape2 = shape;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void init() {
		
	}

}
