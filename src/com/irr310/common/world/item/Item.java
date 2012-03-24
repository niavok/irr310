package com.irr310.common.world.item;

import com.irr310.common.Game;
import com.irr310.common.world.Component;
import com.irr310.common.world.GameEntity;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.view.ItemView;


public class  Item extends GameEntity {

	
	
	private String name;
    private boolean used;
    private Component component;
    private final Player owner;

    public Item(long id, String name, Player owner) {
	    super(id);
        this.name = name;
        this.owner = owner;
        this.used = false;
	}

    public ItemView toView() {
        ItemView itemView = new ItemView();
        
        itemView.id = getId();
        itemView.name = getName();
        itemView.used = used;
        // WorldObject properties    
        
        return itemView;
    }

    public void fromView(ItemView itemView) {
        World world = Game.getInstance().getWorld();

        // World objectProperties
        used = itemView.used;
    }
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
    
    public Component getComponent() {
        return component;
    }

    public Player getOwner() {
        return owner;
    }
}
