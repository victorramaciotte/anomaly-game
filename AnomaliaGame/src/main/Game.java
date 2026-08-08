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
import entity.Direction;
import entity.Player;

public class Game implements Runnable {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private CollisionSystem collision;
	private Thread gameThread;
	private Player player;
	private List<Block> blocks;
	private Anomaly anomaly;
	
	public Game() {
		initClasses();
		
		collision = new CollisionSystem();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		
		gamePanel.requestFocus();
		
		startGameLoop();
		
	}

	private void initClasses() {
		player = new Player(200, 200);
		blocks = new ArrayList<>();
		
		for(int x = 0; x < GameConfig.SCREEN_WIDTH; x += GameConfig.TILE_SIZE) {
			blocks.add(new Block(x, 492, BlockState.NORMAL, BlockType.NORMAL));
			
		}
		
		blocks.add(new Block(400, 444, BlockState.NORMAL, BlockType.BREAKABLE));
		blocks.add(new Block(50, 444, BlockState.NORMAL, BlockType.BREAKABLE));
		blocks.add(new Block(450, 300, BlockState.NORMAL, BlockType.NORMAL));
		blocks.add(new Block(550, 200, BlockState.NORMAL, BlockType.NORMAL));
		
		for(int y = 0; y < GameConfig.SCREEN_WIDTH; y += GameConfig.TILE_SIZE) {
			
			Block leftwall = new Block(-48, y, BlockState.NORMAL, BlockType.NORMAL);
			leftwall.setVisible(false);
			blocks.add(leftwall);
			
			Block rightwall = new Block(960, y, BlockState.NORMAL, BlockType.NORMAL);
			rightwall.setVisible(false);
			blocks.add(rightwall);
		}
		
		anomaly = new Anomaly(0, 0, Direction.LEFT_TO_RIGHT);
	}

	private void startGameLoop () {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update() {
		player.updateX();
	    collision.resolveX(player, blocks);

	    player.updateY();
	    collision.resolveY(player, blocks);
	    
	    anomaly.update();
	    collision.checkAnomalyDamage(player, anomaly);
	    collision.affectBlocks(blocks, anomaly);
	}
	
	public void render(Graphics g) {
		player.render(g);
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
	
	public Player getPlayer() {
		return player;
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	public Anomaly getAnomaly() {
		return anomaly;
	}
}
