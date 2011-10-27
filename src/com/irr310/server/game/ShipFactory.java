package com.irr310.server.game;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.BigPropeller;
import com.irr310.server.game.world.Camera;
import com.irr310.server.game.world.Factory;
import com.irr310.server.game.world.Hangar;
import com.irr310.server.game.world.Harvester;
import com.irr310.server.game.world.Kernel;
import com.irr310.server.game.world.PVCell;
import com.irr310.server.game.world.Refinery;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.Tank;

public class ShipFactory {
	public static Ship createSimpleShip() {
		Ship newShip = new Ship();

		// Kernel
		Kernel kernel = new Kernel();
		kernel.setShipPosition(new Vect3(0, 0, 0));
		newShip.assign(kernel);

		// Camera
		Camera camera = new Camera();
		camera.setShipPosition(new Vect3(0, -1, 0));
		newShip.assign(camera);

		// Factory
		Factory factory = new Factory();
		factory.setShipPosition(new Vect3(0, 0, -2));
		newShip.assign(factory);

		// Tank
		Tank tank = new Tank();
		tank.setShipPosition(new Vect3(0, 5.5, -2));
		newShip.assign(tank);

		// Harvester
		Harvester harvester = new Harvester();
		harvester.setShipPosition(new Vect3(0, 11, -2));
		newShip.assign(harvester);

		// Refinery
		Refinery rafinery = new Refinery();
		rafinery.setShipPosition(new Vect3(0, -4, -2));
		newShip.assign(rafinery);

		// Hangar
		Hangar hangar = new Hangar();
		hangar.setShipPosition(new Vect3(0, -8, -2));
		newShip.assign(hangar);

		// PVCells
		PVCell frontLeft = new PVCell();
		frontLeft.setShipPosition(new Vect3(-5, 0, -2));
		newShip.assign(frontLeft);
		
		PVCell backLeft = new PVCell();
		backLeft.setShipPosition(new Vect3(-5, -6, -2));
		newShip.assign(backLeft);
		
		PVCell frontRight = new PVCell();
		frontRight.setShipPosition(new Vect3(5, 0, -2));
		newShip.assign(frontRight);
		
		PVCell backRight = new PVCell();
		backRight.setShipPosition(new Vect3(5, -6, -2));
		newShip.assign(backRight);
		
		
		BigPropeller mainLeftPropeller = new BigPropeller();
		mainLeftPropeller.setShipPosition(new Vect3(-10, 0, -2));
		newShip.assign(mainLeftPropeller);
		
		BigPropeller mainRightPropeller = new BigPropeller();
		mainRightPropeller.setShipPosition(new Vect3(10, 0, -2));
		newShip.assign(mainRightPropeller);
		
		// Links
		newShip.link(kernel, camera, new Vect3(0., -0.5, 0.));
		newShip.link(kernel, factory, new Vect3(0, 0, -0.5));
		newShip.link(factory, tank, new Vect3(0, 2, -2));
		newShip.link(tank, harvester, new Vect3(0, 9, -2));
		newShip.link(factory, rafinery, new Vect3(0, -2, -2));
		newShip.link(rafinery, hangar, new Vect3(0, -6, -2));
		newShip.link(factory, frontLeft, new Vect3(-2, 0, -2));
		newShip.link(frontLeft, backLeft, new Vect3(-5, -3, -2));
		newShip.link(factory, frontRight, new Vect3(2, 0, -2));
		newShip.link(frontRight, backRight, new Vect3(5, -3, -2));
		newShip.link(frontLeft, mainLeftPropeller, new Vect3(-8, 0, -2));
		newShip.link(frontRight, mainRightPropeller, new Vect3(8, 0, -2));

		/*
		 * 
		 * 
		 * 
		 * // Engines LinearEngine mainLeftPropeller = new LinearEngine();
		 * LinearEngine mainRightPropeller = new LinearEngine(); LinearEngine
		 * leftPropeller = new LinearEngine(); LinearEngine rightPropeller = new
		 * LinearEngine(); LinearEngine topPropeller = new LinearEngine();
		 * LinearEngine bottomPropeller = new LinearEngine();
		 * 
		 * 
		 * newShip.assign(camera); newShip.assign(harvester);
		 * newShip.assign(factory); newShip.assign(rafinery);
		 * newShip.assign(hangar); newShip.assign(frontLeft);
		 * newShip.assign(backLeft); newShip.assign(frontRight);
		 * newShip.assign(backRight); newShip.assign(mainLeftPropeller);
		 * newShip.assign(mainRightPropeller); newShip.assign(leftPropeller);
		 * newShip.assign(rightPropeller); newShip.assign(topPropeller);
		 * newShip.assign(bottomPropeller);
		 * 
		 * 
		 * 
		 * 
		 * camera.setShipPosition(new Vect3(0, 0, 0));
		 * harvester.setShipPosition(new Vect3(0, 0, 0));
		 * factory.setShipPosition(new Vect3(0, 0, 0));
		 * rafinery.setShipPosition(new Vect3(0, 0, 0));
		 * frontLeft.setShipPosition(new Vect3(0, 0, 0));
		 * backLeft.setShipPosition(new Vect3(0, 0, 0));
		 * frontRight.setShipPosition(new Vect3(0, 0, 0));
		 * backRight.setShipPosition(new Vect3(0, 0, 0));
		 * mainLeftPropeller.setShipPosition(new Vect3(0, 0, 0));
		 * mainRightPropeller.setShipPosition(new Vect3(0, 0, 0));
		 * leftPropeller.setShipPosition(new Vect3(0, 0, 0));
		 * rightPropeller.setShipPosition(new Vect3(0, 0, 0));
		 * topPropeller.setShipPosition(new Vect3(0, 0, 0));
		 * bottomPropeller.setShipPosition(new Vect3(0, 0, 0));
		 */

		// newShip.link(harvester.getSlot(1,1), harvester.getSlot(2, 1), 0);

		return newShip;

	}
}
