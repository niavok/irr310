package com.irr310.common.world.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.World;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.Capacity.CapacityType;
import com.irr310.common.world.view.CapacityView;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.PartView;
import com.irr310.common.world.view.SlotView;


public final class  Component extends WorldObject {

	
	
	private double quality;
	private double efficiency;
	private Ship ship;
	private Vec3 shipPosition;
	private Vec3 shipRotation;
	private List<Slot> slots;
	private List<Capacity> capacities;
	private Map<String, Capacity> capacityNameMap;
	private boolean attached;
	
	public Component(long id, String name) {
	    super(id, name);
		slots = new ArrayList<Slot>();
		capacities = new ArrayList<Capacity>();
		capacityNameMap = new HashMap<String, Capacity>();
		shipRotation = Vec3.origin();
		shipPosition = Vec3.origin();
		quality = 1;
		computeEfficiency();
		attached = true;
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
	
	public Slot addSlot(long slotId, Part part, Vec3 position) {
		Slot slot = new Slot(slotId, this,part, position);
		Game.getInstance().getWorld().addSlot(slot);
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

	public void setShipPosition(Vec3 shipPosition) {
		this.shipPosition = shipPosition;
	}

	public void setShipRotation(Vec3 shipRotation) {
		this.shipRotation = shipRotation;
	}

	public Vec3 getShipPosition() {
		return shipPosition;
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
            part.getRotationSpeed().set(rotationSpeed);
        }
    }
	
	
	public Vec3 getLocalShipPosition(Vec3 absolutePosition) {

		TransformMatrix tmp = TransformMatrix.identity();
		
		tmp.translate(shipPosition.negative());
		tmp.translate(absolutePosition);
		
		
		tmp.rotateX(Math.toRadians(-shipRotation.x));
		tmp.rotateY(Math.toRadians(-shipRotation.y));
		tmp.rotateZ(Math.toRadians(-shipRotation.z));
		
		return tmp.getTranslation();
	}

	public Vec3 getAbsoluteShipPosition(Vec3 position) {
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
        
        // WorldObject properties    
        componentView.skin = getSkin();
        componentView.durabilityMax = getDurabilityMax();
        componentView.durability = getDurability();
        componentView.physicalResistance = getPhysicalResistance();
        componentView.heatResistance = getHeatResistance();
        
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
        quality = componentView.quality;

        // WorldObject properties
        setSkin(componentView.skin);
        setDurabilityMax(componentView.durabilityMax);
        setDurability(componentView.durability);
        setPhysicalResistance(componentView.physicalResistance);
        setHeatResistance(componentView.heatResistance);
        
        
        computeEfficiency();
        
        for(PartView part: componentView.parts) {
            addPart(world.loadPart(part, this));
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
	
}
