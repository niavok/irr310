package com.irr310.server.event;

import com.irr310.server.Vect3;
import com.irr310.server.game.Player;

public class AddShipEvent extends ServerEngineEvent {

	private Type type;
	private Vect3 position;
	private Vect3 rotationSpeed;
	private Vect3 linearSpeed;
	private Vect3 rotation;
	private Player owner;

	public enum Type {
		SIMPLE,
	}

	@Override
	public void accept(ServerEngineEventVisitor visitor) {
		visitor.visit(this);
	}
	
	public AddShipEvent(Player owner) {
        this.owner = owner;
	    
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPosition(Vect3 position) {
		this.position = position;
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

	public Vect3 getPosition() {
		return position;
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

    public Player getOwner() {
        return owner;
    }

}
