package com.irr310.server.game.world;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;
import com.irr310.server.game.world.Shape.Face;

public class Slot extends GameEntity {

	private final int positionX;
	private final int positionY;
	private final Component parentComponent;
	private final Face face;
	private Shape shape;

	public Slot(Component parentComponent, Face face, int positionX,
			int positionY) {
		this.parentComponent = parentComponent;
		this.positionX = positionX;
		this.positionY = positionY;
		this.face = face;
		shape = parentComponent.getShape();
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public Component getComponent() {
		return parentComponent;
	}

	public Face getFace() {
		return face;
	}

	public double getFacePositionX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Vect3 getLocalPosition() {
		switch (face) {

		case FRONT:
			return new Vect3(positionX - 0.5, 0., positionY - 0.5);
		case BACK:
			return new Vect3(positionX - 0.5, shape.getSize().y,
					positionY - 0.5);
		case BOTTOM:
			return new Vect3(positionX - 0.5, positionY - 0.5, 0.);
		case TOP:
			return new Vect3(positionX - 0.5, positionY - 0.5,
					shape.getSize().z);
		case RIGHT:
			return new Vect3(0., positionX - 0.5, positionY - 0.5);
		case LEFT:
			return new Vect3(shape.getSize().x, positionX - 0.5,
					positionY - 0.5);
		
		}
		return null;
	}
	
	public Vect3 getLocalNormal() {
		switch (face) {

		case FRONT:
			return new Vect3(0.,-1.,0.);
		case BACK:
			return new Vect3(0.,1.,0.);
		case BOTTOM:
			return new Vect3(0.,0.,-1.);
		case TOP:
			return new Vect3(0.,0.,1.);
		case RIGHT:
			return new Vect3(-1.,0.,0.);
		case LEFT:
			return new Vect3(1.,0.,0.);
		
		}
		return null;
	}

}
