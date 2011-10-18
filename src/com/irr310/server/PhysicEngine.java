package com.irr310.server;

public class PhysicEngine extends Engine {

	public PhysicEngine(ServerGame game) {
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

	private final class PhysicEngineEventVisitor extends EngineEventVisitor {
		@Override
		public void visit(QuitGameEvent event) {
			System.out.println("stopping physic engine");
			isRunning = false;
		}

		@Override
		public void visit(StartEngineEvent event) {
			pause(false);
		}

		@Override
		public void visit(InitEngineEvent event) {
		}

		@Override
		public void visit(PauseEngineEvent event) {
			pause(true);

		}

		@Override
		public void visit(UseScriptEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visit(AddWorldObjectEvent event) {
			// TODO Auto-generated method stub
			
		}
	}

}
