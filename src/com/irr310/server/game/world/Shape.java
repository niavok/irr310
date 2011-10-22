package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.server.Vect3;

public class Shape {

	Map<Face, List<Slot>> faces;
	private final Component parentComponent;
	private final Vect3 size;
	
	
	public enum Face {
		FRONT,
		BACK,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}
	
	public Shape(Component parentComponent, Vect3 size) {
		this.parentComponent = parentComponent;
		this.size = size;
		faces = new HashMap<Face, List<Slot>>();
		for(Face face: Face.values()) {
			faces.put(face, new ArrayList<Slot>());
		}
	}
	
	public void declareSlot(Face face, int positionX, int positionY) {
		faces.get(face).add(new Slot(parentComponent, face, positionX, positionY));
	}

	public Slot getSlot(Face face, int positionX, int positionY) {
		List<Slot> slotList = faces.get(face);
		for(Slot slot: slotList) {
			if(slot.getPositionX() == positionX && slot.getPositionY() == positionY) {
				return slot;
			}
		}
		return null;
	}

	public Vect3 getSize() {
		return size;
	}
	
}
