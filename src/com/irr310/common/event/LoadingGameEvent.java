package com.irr310.common.event;


public class LoadingGameEvent extends EngineEvent {

	private final String message;

    public LoadingGameEvent(String message) {
        this.message = message;
    }

    @Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

    public String getMessage() {
        return message;
    }
}
