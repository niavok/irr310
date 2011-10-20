package com.irr310.server;


public abstract class WorldObject {

	protected final World world;
	private Vect3 position;
	private Double mass;
	private Vect3 rotationSpeed;
	private Vect3 linearSpeed;
	private Vect3 rotation;
	private String name;
	private WorldObject parent;

	public WorldObject(World world) {
		this.world = world;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public WorldObject getLinkedObject() {
		return this.parent;
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

}
