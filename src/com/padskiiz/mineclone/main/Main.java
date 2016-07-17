package com.padskiiz.mineclone.main;

import com.padskiiz.engine.*;

public class Main {
	public static void main(String[] args) {
		try {
			boolean vSync = true;
			IGameLogic gameLogic = new Game();
			GameEngine gameEng = new GameEngine("Sample Text", 600, 400, vSync, gameLogic);
			gameEng.start();
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
