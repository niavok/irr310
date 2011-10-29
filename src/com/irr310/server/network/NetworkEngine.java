package com.irr310.server.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.NetworkMessage;
import com.irr310.server.Engine;
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.InitEngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;


public class NetworkEngine extends Engine {

	public NetworkEngine() {
	    
	    try {
            NetworkWorker worker = new NetworkWorker(this);
            new Thread(worker).start();
            new Thread(new NioServer(null, 22310, worker)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
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

    public void pushMessage(SocketChannel socketChannel, NetworkMessage message) {
        System.out.println("Network engine receive a message");
        
        //TODO: make a asyncronus queue
        
        switch(message.getType()) {
            case LOGIN_REQUEST:
                LoginRequestMessage m = (LoginRequestMessage) message;
                System.out.println("Login request: login="+m.login+", password="+m.password);
                break;
        }
    }

}
