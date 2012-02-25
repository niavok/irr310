package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.WorldObject;


public class AddWorldObjectEvent extends EngineEvent {

	private Type type;
	private String name;
	private WorldObject linkedObject;
	private Vec3 position;
	private Double mass;
	private Vec3 rotationSpeed;
	private Vec3 linearSpeed;
	private Vec3 rotation;

	public enum Type {
		LINEAR_ENGINE, CAMERA,
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

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public void setRotationSpeed(Vec3 rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public void setLinearSpeed(Vec3 linearSpeed) {
		this.linearSpeed = linearSpeed;
	}

	public void setRotation(Vec3 rotation) {
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

	public Vec3 getPosition() {
		return position;
	}

	public Double getMass() {
		return mass;
	}

	public Vec3 getRotationSpeed() {
		return rotationSpeed;
	}

	public Vec3 getLinearSpeed() {
		return linearSpeed;
	}

	public Vec3 getRotation() {
		return rotation;
	}

	
}
