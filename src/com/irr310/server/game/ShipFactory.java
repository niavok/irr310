package com.irr310.server.game;

import com.irr310.server.game.world.Camera;
import com.irr310.server.game.world.Factory;
import com.irr310.server.game.world.Hangar;
import com.irr310.server.game.world.Harvester;
import com.irr310.server.game.world.Kernel;
import com.irr310.server.game.world.LightCaptor;
import com.irr310.server.game.world.LinearEngine;
import com.irr310.server.game.world.Refinery;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.Tank;

public class ShipFactory {
	public static Ship createSimpleShip() {
		Ship newShip = new Ship();
		
		Kernel kernel = new Kernel();
		Camera camera = new Camera();
		
		Harvester harvester = new Harvester();
		Tank tank = new Tank();
		Factory factory = new Factory();
		Refinery rafinery = new Refinery();
		Hangar hangar = new Hangar();
		
		// Energy generator
		LightCaptor frontLeft = new LightCaptor();
		LightCaptor backLeft = new LightCaptor();
		LightCaptor frontRight = new LightCaptor();
		LightCaptor backRight = new LightCaptor();
		
		// Engines
		LinearEngine mainLeftPropeller = new LinearEngine();
		LinearEngine mainRightPropeller = new LinearEngine();
		LinearEngine leftPropeller = new LinearEngine();
		LinearEngine rightPropeller = new LinearEngine();
		LinearEngine topPropeller = new LinearEngine();
		LinearEngine bottomPropeller = new LinearEngine();
		
		newShip.assign(kernel);
		newShip.assign(camera);
		newShip.assign(harvester);
		newShip.assign(factory);
		newShip.assign(rafinery);
		newShip.assign(hangar);
		newShip.assign(frontLeft);
		newShip.assign(backLeft);
		newShip.assign(frontRight);
		newShip.assign(backRight);
		newShip.assign(mainLeftPropeller);
		newShip.assign(mainRightPropeller);
		newShip.assign(leftPropeller);
		newShip.assign(rightPropeller);
		newShip.assign(topPropeller);
		newShip.assign(bottomPropeller);
		
		newShip.link(harvester.getSlot(1,1), harvester.getSlot(2, 1));
		
		
		return newShip;
		
	}
}
