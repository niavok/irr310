package com.irr310.server.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.server.Engine;
import com.irr310.server.GameServer;
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.InitEngineEvent;
import com.irr310.server.event.NetworkEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.game.Player;


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
			isRunning = false;
			
		}

		@Override
		public void visit(NetworkEvent event) {
		    NetworkMessage message = event.getMessage();
		    switch(message.getType()) {
		        case LOGIN_REQUEST:
		            
		            if(event.getClient().isLogged()) {
		                event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "already logged as "+event.getClient().getPlayer().getLogin()));
                        break;
		            }
		            
		            LoginRequestMessage m = (LoginRequestMessage) message;
		            if(!GameServer.getInstance().getGame().isPlayerExist(m.login)) {
		                event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "unknown user"));
		                break;
		            }
		            
		            Player player = GameServer.getInstance().getGame().getPlayerByLogin(m.login);
		            
		            if(!player.checkPassword(m.password)) {
		                event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "bad password"));
		                break;
		            }
		            
		            // Ok, you can login.
		            
		            
		            event.getClient().setPlayer(player);
		            
		            event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), true, "success"));
		            
		            
		            break;
	            default:
	                System.err.println("Unsupported network type");
		    }
		}
		
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
