package com.irr310.server;


public class NetworkEngine extends Engine {

	public NetworkEngine(ServerGame game) {
		super(game);
	}

	@Override
	protected void frame() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(EngineEvent e) {
		e.accept(new NetworkEngineEventVisitor());
	}

	private final class NetworkEngineEventVisitor extends EngineEventVisitor {
		@Override
		public void visit(QuitGameEvent event) {
			System.out.println("stopping network engine");
			stopAcceptor();
			isRunning = false;
			
		}

		@Override
		public void visit(StartEngineEvent event) {
			startAcceptor();
			pause(false);
			
		}

		@Override
		public void visit(InitEngineEvent event) {
		}

		@Override
		public void visit(PauseEngineEvent event) {
			pause(true);
			stopAcceptor();
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

	public void stopAcceptor() {
		// TODO Auto-generated method stub
		
	}

	public void startAcceptor() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

}
