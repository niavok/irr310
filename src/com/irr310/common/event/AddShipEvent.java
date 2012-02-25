package com.irr310.common.event;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Player;

public class AddShipEvent extends EngineEvent {

	private Type type;
	private Vec3 position;
	private Vec3 rotationSpeed;
	private Vec3 linearSpeed;
	private Vec3 rotation;
	private Player owner;

	public enum Type {
		SIMPLE, SIMPLE_FIGHTER,
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}
	
	public AddShipEvent(Player owner) {
        this.owner = owner;
	    
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
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

	public Vec3 getPosition() {
		return position;
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

    public Player getOwner() {
        return owner;
    }

}
