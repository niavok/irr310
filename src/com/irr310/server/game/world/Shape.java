package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.Vect3;

public class Shape {

	List<List<Slot>> faces;
	
	
	public Shape(Vect3 size) {
		faces = new ArrayList<List<Slot>>(6);
		for(int i = 0; i < 6; i++) {
			faces.add(new ArrayList<Slot>());
		}
	}
	
	public void declareSlot(int face, int position) {
		faces.get(face).add(new Slot(position));
	}

	public Slot getSlot(int faceId, int slotId) {
		List<Slot> face = faces.get(faceId);
		for(Slot slot: face) {
			if(slot.getPosition() == slotId) {
				return slot;
			}
		}
		return null;
	}
	
}
