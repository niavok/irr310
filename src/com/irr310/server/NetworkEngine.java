package com.irr310.server;


public class NetworkEngine extends Engine {

	public NetworkEngine(Game game) {
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
			isRunning = false;
		}
	}

}
