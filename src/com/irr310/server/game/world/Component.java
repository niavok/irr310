package com.irr310.server.game.world;

import java.util.List;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.Shape.Face;


public class Component extends WorldObject {

	
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
		
		return getShape().getSlot(face, slotX, slotY);
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

	
	
}
