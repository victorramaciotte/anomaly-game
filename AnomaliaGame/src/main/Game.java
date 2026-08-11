package main;

import java.awt.Graphics;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import collision.CollisionSystem;
import entity.Anomaly;
import entity.Block;
import entity.BlockState;
import entity.BlockType;
import entity.Core;
import entity.Direction;
import entity.Player;
import state.GameStateManager;
import state.PlayingState;

public class Game implements Runnable {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private GameStateManager stateManager;
    private PlayingState playingState;
	
	public Game() {
		stateManager = new GameStateManager();
        playingState = new PlayingState();
        stateManager.setState(playingState);
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
		
	}

	private void startGameLoop () {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update() {
		stateManager.update();
	}
	

	public void render(Graphics g) {
		stateManager.render(g);
	}
	
	public Player getPlayer() {
        return playingState.getPlayer(); // só existe pra viabilizar o KeyboardInputs por enquanto
    }
	
	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / GameConfig.FPS;
		double timePerUpdate = 1000000000.0 / GameConfig.UPS;
		
		long previousTime = System.nanoTime();
		
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		
	}
	
}
