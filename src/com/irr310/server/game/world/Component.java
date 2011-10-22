package com.irr310.server.game.world;

import java.util.List;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.Shape.Face;


public class Component extends WorldObject {

	private Vect3 position;
	private Double mass;
	private Vect3 rotationSpeed;
	private Vect3 linearSpeed;
	private Vect3 rotation;
	private String name;
	private Shape shape;
	private double durabilityMax;
	private double durability;
	private double quality;
	private double efficiency;
	private Container container;
	private Vect3 shipPosition;
	private Vect3 shipRotation;
	
	public Component() {
		
	}
	
	public Slot getSlot(Face face, int slotX, int slotY) {
		
		return shape.getSlot(face, slotX, slotY);
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
	
	private void computeEfficiency() {
		double durabilityFactor = (((durability/ durabilityMax) -0.3)/0.7) -1;
		efficiency = quality*(1-durabilityFactor*durabilityFactor);
	}
	
	public double getEfficiency() {
		return efficiency;
	}

	public boolean isUnusable() {
		return efficiency <=0; 
	}

	public Container getContainer() {
		
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public void setShipPosition(Vect3 shipPosition) {
		this.shipPosition = shipPosition;
	}

	public void setShipRotation(Vect3 shipRotation) {
		this.shipRotation = shipRotation;
	}

	public Shape getShape() {
		return shape;
	}
	
}
