package com.irr310.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Game {
	private GameEngine gameEngine;
	private NetworkEngine networkEngine;
	private PhysicEngine physicEngine;
	private ParameterAnalyser parameterAnalyser;
	private boolean stillRunning;
	private World world;
	private CommandManager commandManager;

	public Game(ParameterAnalyser parameterAnalyser) {
		this.parameterAnalyser = parameterAnalyser;
		stillRunning = true;

		world = new World();

		gameEngine = new GameEngine(this);
		physicEngine = new PhysicEngine(this);
		networkEngine = new NetworkEngine(this);

		commandManager = new CommandManager(this);

	}

	public void run() {
		// boolean currentStillRunning = stillRunning;

		gameEngine.start();
		physicEngine.start();
		networkEngine.start();

		// currentStillRunning = stillRunning;
		// std::string i;
		// std::string o;

		
		//Wait engines started
		while ((Engine.getRunningEngineCount()) < 3) {
			new Duration(100000000).sleep();
		}
		
		
		System.out.println("Irr310 - v0.1a");
		
		Reader reader = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(reader);

		try {
			while (true) {
				System.out.print("> ");

				String command = input.readLine();

				String output = commandManager.execute(command);

				if (output != null) {
					System.out.println(output);
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

	public World getWorld() {
		return world;
	}

	public void sendToAll(EngineEvent e) {
		gameEngine.pushEvent(e);
		physicEngine.pushEvent(e);
		networkEngine.pushEvent(e);
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
