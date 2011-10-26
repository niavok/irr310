package com.irr310.server.game.world;

import com.irr310.server.Vect3;


public abstract  class SimpleComponent extends Component {

	protected Part uniquePart;

	public SimpleComponent() {
		uniquePart = new Part();
		parts.add(uniquePart);
		
	}

	@Override
	public void changeTranslation(Vect3 position) {
		uniquePart.getTransform().setTranslation(position);
	}

	@Override
	public void changeLinearSpeed(Vect3 linearSpeed) {
		uniquePart.getLinearSpeed().set(linearSpeed);
	}

	@Override
	public void changeRotationSpeed(Vect3 rotationSpeed) {
		uniquePart.getRotationSpeed().set(rotationSpeed);
	}

	

}
