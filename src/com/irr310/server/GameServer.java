package com.irr310.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.irr310.server.event.AddShipEvent;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.game.Game;
import com.irr310.server.game.world.World;
import com.irr310.server.ui.DebugGraphicEngine;


public class GameServer {
	private GameEngine gameEngine;
	private NetworkEngine networkEngine;
	private PhysicEngine physicEngine;
	private ParameterAnalyser parameterAnalyser;
	private boolean stillRunning;
	private Game game;
	private CommandManager commandManager;
	private DebugGraphicEngine debugGraphicEngine;

	public static GameServer instance = null;
	public static GameServer getInstance() {
		return instance;
	}
	
	public GameServer(ParameterAnalyser parameterAnalyser) {
		this.parameterAnalyser = parameterAnalyser;
		instance = this;
		stillRunning = true;

		game = new Game();

		gameEngine = new GameEngine();
		physicEngine = new PhysicEngine();
		networkEngine = new NetworkEngine();
		debugGraphicEngine = new DebugGraphicEngine();

		commandManager = new CommandManager();

	}

	public void run() {
		// boolean currentStillRunning = stillRunning;

		gameEngine.start();
		physicEngine.start();
		networkEngine.start();
		debugGraphicEngine.start();

		// currentStillRunning = stillRunning;
		// std::string i;
		// std::string o;

		
		//Wait engines started
		while ((Engine.getRunningEngineCount()) < 4) {
			new Duration(100000000).sleep();
		}
		
		
		sendToAll(new StartEngineEvent());
		AddShipEvent addShipEvent = new AddShipEvent();
		addShipEvent.setType(AddShipEvent.Type.SIMPLE);
		sendToAll(addShipEvent);
		
		
		
		
		System.out.println("Irr310 - v0.1a");
		
		Reader reader = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(reader);

		try {
			while (true) {
				System.out.print("> ");

				String command = input.readLine();

				String output = commandManager.execute(command);

				if (output != null) {
					if(!output.isEmpty()) {
						System.out.println(output);
					}
				} else {
					System.out.println("Game : Exiting...");
					break;
				}

			}

		} catch (IOException e) {
			// Todo handle exception
		}

		int count;
		System.out.println("Game : Stopping");
		sendToAll(new QuitGameEvent());

		while ((count = Engine.getRunningEngineCount()) > 0) {
			System.out.println("Game : Wait for engine stop, still " + count
					+ " engines");
			Duration.ONE_SECOND.sleep();
		}
		System.out.println("Game : Stopped");

	}

	public void stop() {

	}

	public Game getGame() {
		return game;
	}

	public void sendToAll(EngineEvent e) {
		gameEngine.pushEvent(e);
		physicEngine.pushEvent(e);
		networkEngine.pushEvent(e);
		debugGraphicEngine.pushEvent(e);
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public PhysicEngine getPhysicEngine() {
		return physicEngine;
	}

	public NetworkEngine GetNetworkEngine() {
		return networkEngine;
	}

	// static const int ENGINE_COUNT = 3;

	// boost::mutex stillRunningMutex;

}
