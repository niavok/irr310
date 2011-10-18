package com.irr310.server;

public class Irr310Server {

	public static void main(String[] args) {
		ParameterAnalyser parameterAnalyser = new ParameterAnalyser(args);
		Game game = new Game(parameterAnalyser);
		game.run();
	}
}
