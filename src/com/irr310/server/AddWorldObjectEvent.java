package com.irr310.server;

import com.irr310.server.game.world.WorldObject;


public class AddWorldObjectEvent extends EngineEvent {

	private Type type;
	private String name;
	private WorldObject linkedObject;
	private Vect3 position;
	private Double mass;
	private Vect3 rotationSpeed;
	private Vect3 linearSpeed;
	private Vect3 rotation;

	public enum Type {
		REFERENCE,
		STAR,
		PLANET,
		PART,
		CAMERA,
		LINEAR_MOTOR,
		COLLECTION,
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLinkedObject(WorldObject linkedObject) {
		this.linkedObject = linkedObject;
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

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public WorldObject getLinkedObject() {
		return linkedObject;
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
