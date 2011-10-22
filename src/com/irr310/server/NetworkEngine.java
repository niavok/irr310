package com.irr310.server;

import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.InitEngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;


public class NetworkEngine extends Engine {

	public NetworkEngine() {
	}

	@Override
	protected void frame() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(EngineEvent e) {
		e.accept(new NetworkEngineEventVisitor());
	}

	private final class NetworkEngineEventVisitor extends DefaultEngineEventVisitor {
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
