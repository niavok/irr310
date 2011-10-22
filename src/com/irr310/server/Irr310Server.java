package com.irr310.server;

public class Irr310Server {

	public static void main(String[] args) {
		ParameterAnalyser parameterAnalyser = new ParameterAnalyser(args);
		GameServer gameServer = new GameServer(parameterAnalyser);
		gameServer.run();
	}
}
