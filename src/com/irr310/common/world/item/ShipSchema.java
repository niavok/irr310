package com.irr310.common.world.item;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.Ship;


public class ShipSchema {
    Ship ship;
    
    List<ItemSlot> itemSlotList = new ArrayList<ItemSlot>();
    
    public void addItemSlot(ItemSlot slot) {
        itemSlotList.add(slot);
    }

    public List<ItemSlot> getItemSlots() {
        return itemSlotList;
    }
    
}
