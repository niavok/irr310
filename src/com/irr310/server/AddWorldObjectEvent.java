package com.irr310.server;

public class AddWorldObjectEvent extends EngineEvent {

	private Type type;
	private String name;
	private WorldObject linkedObject;
	private Vect3 position;

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
		// TODO Auto-generated method stub
		
	}

	public void setRotationSpeed(Vect3 vect3) {
		// TODO Auto-generated method stub
		
	}

	public void setLinearSpeed(Vect3 vect3) {
		// TODO Auto-generated method stub
		
	}

	public void setRotation(Vect3 vect3) {
		// TODO Auto-generated method stub
		
	}

	
}
