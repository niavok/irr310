package com.irr310.server;

public abstract class EngineEventVisitor {

	public abstract void visit(QuitGameEvent event);

	public abstract void visit(StartEngineEvent event);
	
	public abstract void visit(InitEngineEvent event);
	
	public abstract void visit(PauseEngineEvent event);

	public abstract void visit(UseScriptEvent event);

	public abstract void visit(AddWorldObjectEvent event);
}
