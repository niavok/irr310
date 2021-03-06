package com.irr310.common.world.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.Capacity;


public final class  Component extends SystemObject {

	
	
	private double quality;
	private double efficiency;
	private Ship ship;
	private Vec3 locationInShip;
	private Vec3 shipRotation;
	private List<Slot> slots;
	private List<Capacity> capacities;
	private Map<String, Capacity> capacityNameMap;
	private boolean attached;
    private String mKey;
	
	public Component(WorldSystem system, long id, String name, String key) {
	    super(system, id, name);
        mKey = key;
		slots = new ArrayList<Slot>();
		capacities = new ArrayList<Capacity>();
		capacityNameMap = new HashMap<String, Capacity>();
		shipRotation = Vec3.origin();
		locationInShip = Vec3.origin();
		quality = 1;
		computeEfficiency();
		attached = true;
	}

    public double getQuality() {
        return quality;
    }

    public String getKey() {
        return mKey;
    }
	
	public Slot getSlot(Vec3 position) {
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

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public List<Slot> getSlots() {
        return slots;
    }
	
	public Slot addSlot(long slotId, Part part, Vec3 position) {
		Slot slot = new Slot(getSystem(), slotId, position);
        slot.setParentComponent(this);
        slot.setPart(part);

		getSystem().addSlot(slot);
		slots.add(slot);
		return slot;
		
	}

    public Slot addSlot(Slot slot) {
        getSystem().addSlot(slot);
        slots.add(slot);
        return slot;
    }
	
	@Override
	public void setDurability(double durability) {
	    super.setDurability(durability);
	    computeEfficiency();
	}
	
	@Override
	public void setDurabilityMax(double durabilityMax) {
	    super.setDurabilityMax(durabilityMax);
	    computeEfficiency();
	}
	
	private void computeEfficiency() {
	    //efficiency =  1-((x -0.3 )/ 0.7 -1)^4
	    
		double durabilityFactor = (((getDurability()/ getDurabilityMax()) -0.3)/0.7) -1;
		efficiency = quality*(1-Math.pow(durabilityFactor,4));
		if(efficiency < 0) {
		    efficiency = 0;
		}
	}
	
	public double getEfficiency() {
		return efficiency;
	}

	public boolean isUnusable() {
		return efficiency <=0; 
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public void setLocationInShip(Vec3 locationInShip) {
		this.locationInShip = locationInShip;
	}

	public void setShipRotation(Vec3 shipRotation) {
		this.shipRotation = shipRotation;
	}

	public Vec3 getLocationInShip() {
		return locationInShip;
	}

	public Vec3 getShipRotation() {
		return shipRotation;
	}


    public void changeTranslation(Vec3 position) {
	    for(Part part: parts) {
	        part.getTransform().translate(position);    
	    }
        
    }

    public void changeLinearSpeed(Vec3 linearSpeed) {
        for(Part part: parts) {
            part.getLinearSpeed().set(linearSpeed);
        }
    }

    public void changeRotationSpeed(Vec3 rotationSpeed) {
        for(Part part: parts) {
            part.getAngularSpeed().set(rotationSpeed);
        }
    }
	
	
	public Vec3 getLocalShipPosition(Vec3 absolutePosition) {

		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.translate(locationInShip.negative());
		tmp.translate(absolutePosition);
		
		
		tmp.rotateX(Math.toRadians(-shipRotation.x));
		tmp.rotateY(Math.toRadians(-shipRotation.y));
		tmp.rotateZ(Math.toRadians(-shipRotation.z));
		
		return tmp.getTranslation();
	}

	public Vec3 getAbsoluteShipLocation(Vec3 position) {
		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.translate(position);
		
		tmp.rotateX(Math.toRadians(shipRotation.x));
        tmp.rotateY(Math.toRadians(shipRotation.y));
        tmp.rotateZ(Math.toRadians(shipRotation.z));
		
		tmp.translate(locationInShip);
        
		return tmp.getTranslation();
	}

    public void addCapacity(Capacity capacity) {
        capacities.add(capacity);
        capacityNameMap.put(capacity.getName(), capacity);
    }

    public List<Capacity> getCapacities() {
        return capacities;
    }

    @SuppressWarnings("unchecked")
    public <T extends Capacity> List<T> getCapacitiesByClass(Class<T> clazz) {
        List<T> matchCapacities = new ArrayList<T>();
        for(Capacity capacity: capacities) {
            if(capacity.getClass().equals(clazz)) {
                matchCapacities.add((T) capacity);
            }
        }
        return matchCapacities;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;        
    }

    public void initDurability(double durability) {
        setDurabilityMax(durability);
        setDurability(durability);
    }
    
    @Override
    public String toString() {
        return "Component "+getName()+" "+getId();
    }
	
}
