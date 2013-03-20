package com.irr310.client.graphics.ether.activities.worldmap;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.system.WorldSystem;

/* define scrollable zone for a given WorldMap*/
public class ScrollZone 
{
	/* basically, scrollzone is a rectangle define with 2 Point (max = Top Left corner / min = Bot Right corner) */
	private Vec2 min;
	private Vec2 max;
	
	public ScrollZone() 
	{
		min = new Vec2(0,0);
		max = new Vec2(1,1);
	};
	
	/* update zone */
	public void setZone(Vec2 zone)
	{
		min = min.min(zone);
		max = max.max(zone);
	}
	
	public Vec2 getMin(float zoom)
	{
		return min.multiply(zoom);
	}
	
	public Vec2 getMax(float zoom)
	{
		return max.multiply(zoom);
	}
	
	// return min zoom for width/2
	public float getxMin(float width)
	{
	    // if there is no max and min define...
	    if ( max.x == min.x ) { return 0.01f; }
	    // bug if max < 0 or min > 0 ...
	    float diff = (float) (max.x - min.x);
	    return width / ( 2 * diff ) ;
	}
	
	// return min zoom for height/2
	public float getyMin(float height)
    {
        // if there is no max and min define...
        if ( max.y == min.y ) { return 0.01f; }
        // bug if max < 0 or min > 0 ...
        float diff = (float) (max.y - min.y);
        return height / ( 2 * diff ) ;
    }
}