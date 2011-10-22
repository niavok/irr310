package com.irr310.server.event;

import java.io.File;



public class UseScriptEvent extends EngineEvent {

	private final Type type;
	private final File script;

	public enum Type {
		DRIVER,
		BIND
	}

	public UseScriptEvent(Type type, File script) {
		this.type = type;
		this.script = script;
	}

	@Override
	public void accept(EngineEventVisitor visitor) {
		visitor.visit(this);
	}

	public Type getType() {
		return type;
	}

	public File getScript() {
		return script;
	}

	
}
