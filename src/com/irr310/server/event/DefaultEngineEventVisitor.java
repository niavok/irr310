package com.irr310.server.event;

public abstract class DefaultEngineEventVisitor implements EngineEventVisitor {

	public void visit(QuitGameEvent event) {
	}

	public void visit(StartEngineEvent event) {
	}

	public void visit(InitEngineEvent event) {
	}

	public void visit(PauseEngineEvent event) {
	}

	public void visit(UseScriptEvent event) {
	}

	public void visit(AddWorldObjectEvent event) {
	}

	public void visit(WorldObjectAddedEvent worldObjectAddedEvent) {
	}
}
