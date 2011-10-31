package com.irr310.common.world;

import com.irr310.common.tools.Vect3;


public final  class SimpleComponent extends Component {

	protected Part uniquePart;

	public SimpleComponent(long id, long partId) {
	    super(id);
		uniquePart = new Part(partId);
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

    public Part getPart() {
        return uniquePart;
    }

	

}
