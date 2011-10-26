package com.irr310.server.game;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.Camera;
import com.irr310.server.game.world.Kernel;
import com.irr310.server.game.world.Ship;

public class ShipFactory {
	public static Ship createSimpleShip() {
		Ship newShip = new Ship();
		
		// Kernel
		Kernel kernel = new Kernel();
		kernel.setMass(1d);
		kernel.setShipPosition(new Vect3(0, 0, 0));
		kernel.setShape(new Vect3(1, 1, 1));
		kernel.addSlot(new Vect3(0.5, 0., 0.));
		kernel.addSlot(new Vect3(-0.5, 0., 0.));
		kernel.addSlot(new Vect3(0., 0.5, 0.));
		kernel.addSlot(new Vect3(0., -0.5, 0.));
		kernel.addSlot(new Vect3(0., 0., 0.5));
		kernel.addSlot(new Vect3(0., 0., -0.5));
		newShip.assign(kernel);
		
		// Camera
		Camera camera = new Camera();
		camera.setMass(1d);
		camera.setShipPosition(new Vect3(0, -1, 0));
		camera.setShape(new Vect3(1, 1, 1));
		camera.addSlot(new Vect3(0.5, 0., 0.));
		camera.addSlot(new Vect3(-0.5, 0., 0.));
		camera.addSlot(new Vect3(0., 0.5, 0.));
		camera.addSlot(new Vect3(0., -0.5, 0.));
		camera.addSlot(new Vect3(0., 0., 0.5));
		camera.addSlot(new Vect3(0., 0., -0.5));
		newShip.assign(camera);
		
		
		newShip.link(kernel, camera, new Vect3(0.,-0.5,0.));
		
		
		/*Harvester harvester = new Harvester();
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
		
		
		
		
		camera.setShipPosition(new Vect3(0, 0, 0));
		harvester.setShipPosition(new Vect3(0, 0, 0));
		factory.setShipPosition(new Vect3(0, 0, 0));
		rafinery.setShipPosition(new Vect3(0, 0, 0));
		frontLeft.setShipPosition(new Vect3(0, 0, 0));
		backLeft.setShipPosition(new Vect3(0, 0, 0));
		frontRight.setShipPosition(new Vect3(0, 0, 0));
		backRight.setShipPosition(new Vect3(0, 0, 0));
		mainLeftPropeller.setShipPosition(new Vect3(0, 0, 0));
		mainRightPropeller.setShipPosition(new Vect3(0, 0, 0));
		leftPropeller.setShipPosition(new Vect3(0, 0, 0));
		rightPropeller.setShipPosition(new Vect3(0, 0, 0));
		topPropeller.setShipPosition(new Vect3(0, 0, 0));
		bottomPropeller.setShipPosition(new Vect3(0, 0, 0));
		*/
		
		
		//newShip.link(harvester.getSlot(1,1), harvester.getSlot(2, 1), 0);
		
		
		return newShip;
		
	}
}
