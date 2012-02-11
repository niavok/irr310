package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.Capacity.CapacityType;
import com.irr310.common.world.view.CapacityView;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.PartView;
import com.irr310.common.world.view.SlotView;


public final class  Component extends WorldObject {

	
	private double durabilityMax;
	private double durability;
	private double quality;
	private double efficiency;
	private Container container;
	private Vect3 shipPosition;
	private Vect3 shipRotation;
	private List<Slot> slots;
	private List<Capacity> capacities;
	private Map<String, Capacity> capacityNameMap;
	
	public Component(long id, String name) {
	    super(id, name);
		slots = new ArrayList<Slot>();
		capacities = new ArrayList<Capacity>();
		capacityNameMap = new HashMap<String, Capacity>();
		shipRotation = Vect3.origin();
		shipPosition = Vect3.origin();
		durability = 1;
		durabilityMax = 1;
		quality = 1;
		computeEfficiency();
	}
	
	public Slot getSlot(Vect3 position) {
		Slot minSlot = null;
		double minLenght = Double.MAX_VALUE;
		for(Slot slot: slots) {
			Double distanceTo = slot.getPosition().distanceTo(position);
			if(minLenght > distanceTo) {
				minLenght = distanceTo;
				minSlot = slot;
			}
		}
		return minSlot;
	}
	
	public Slot addSlot(long slotId, Part part, Vect3 position) {
		Slot slot = new Slot(slotId, this,part, position);
		Game.getInstance().getWorld().addSlot(slot);
		slots.add(slot);
		return slot;
		
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

	public Vect3 getShipPosition() {
		return shipPosition;
	}

	public Vect3 getShipRotation() {
		return shipRotation;
	}


    public void changeTranslation(Vect3 position) {
	    for(Part part: parts) {
	        part.getTransform().translate(position);    
	    }
        
    }

    public void changeLinearSpeed(Vect3 linearSpeed) {
        for(Part part: parts) {
            part.getLinearSpeed().set(linearSpeed);
        }
    }

    public void changeRotationSpeed(Vect3 rotationSpeed) {
        for(Part part: parts) {
            part.getRotationSpeed().set(rotationSpeed);
        }
    }
	
	
	public Vect3 getLocalShipPosition(Vect3 absolutePosition) {

		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.translate(shipPosition.negative());
		tmp.translate(absolutePosition);
		
		
		tmp.rotateX(Math.toRadians(-shipRotation.x));
		tmp.rotateY(Math.toRadians(-shipRotation.y));
		tmp.rotateZ(Math.toRadians(-shipRotation.z));
		
		return tmp.getTranslation();
	}

	public Vect3 getAbsoluteShipPosition(Vect3 position) {
		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.translate(position);
		
		tmp.rotateX(Math.toRadians(shipRotation.x));
        tmp.rotateY(Math.toRadians(shipRotation.y));
        tmp.rotateZ(Math.toRadians(shipRotation.z));
		
		tmp.translate(shipPosition);
        
		return tmp.getTranslation();
	}

    public ComponentView toView() {
        ComponentView componentView = new ComponentView();
        
        componentView.id = getId();
        componentView.name = getName();
        componentView.shipPosition = shipPosition;
        componentView.shipRotation = shipRotation;
        componentView.skin = getSkin();
        componentView.durabilityMax = durabilityMax;
        componentView.durability = durability;
        componentView.quality = quality;
        
        for(Part part: parts) {
            componentView.parts.add(part.toView());    
        }
        
        for(Slot slot: slots) {
            componentView.slots.add(slot.toView());    
        }
        
        for(Capacity capacity: capacities) {
            componentView.capacities.add(capacity.toView());    
        }
        
        return componentView;
    }

    public void fromView(ComponentView componentView) {
        World world = Game.getInstance().getWorld();
        shipPosition = componentView.shipPosition;
        shipRotation = componentView.shipRotation;
        durabilityMax = componentView.durabilityMax;
        durability = componentView.durability;
        quality = componentView.quality;
        setSkin(componentView.skin);
        computeEfficiency();
        
        for(PartView part: componentView.parts) {
            addPart(world.loadPart(part));
        }
        
        for(SlotView slot: componentView.slots) {
            addSlot(slot.id, world.getPartById(slot.partId), slot.position);
        }
        
        for(CapacityView capacityView: componentView.capacities) {
            Capacity capacity = Capacity.createFromType(capacityView.id,  CapacityType.values()[capacityView.type]);
            capacity.fromView(capacityView);
            addCapacity(capacity);
        }
        
    }

    public void addCapacity(Capacity capacity) {
        capacities.add(capacity);
        capacityNameMap.put(capacity.getName(), capacity);
    }

    public List<Capacity> getCapacities() {
        return capacities;
    }

    
    public Capacity getCapacitiesByName(String name) {
        return capacityNameMap.get(name);
    }

    
	
}
