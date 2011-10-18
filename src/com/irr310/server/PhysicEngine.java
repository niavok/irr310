package com.irr310.server;

public class PhysicEngine extends Engine {

	public PhysicEngine(Game game) {
		super(game);
	}

	@Override
	protected void frame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processEvent(EngineEvent e) {
		e.accept(new PhysicEngineEventVisitor());
	}
	
	private final class PhysicEngineEventVisitor extends
	EngineEventVisitor {
	@Override
	public void visit(QuitGameEvent event) {
		System.out.println("stopping physic engine");
		isRunning = false;
	}
}

}
