package com.irr310.common.world.view;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.world.capacity.Capacity.CapacityType;

@NetworkClass
public class CapacityView {
    
    @NetworkField
    public long id;
    
    @NetworkField
    public int type;
    
    @NetworkField
    public String name;
    
    @NetworkListField(Double.class)
    public List<Double> doubles = new ArrayList<Double>();

    private int popIndex = 0;

    

    public void pushDouble(double d) {
        doubles.add(d);
    }

    public double popDouble() {
        return doubles.get(popIndex ++);
    }

    
}
