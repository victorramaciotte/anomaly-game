package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import collision.CollisionSystem;
import entity.Anomaly;
import entity.Block;
import entity.BlockState;
import entity.BlockType;
import entity.Core;
import entity.Direction;
import entity.Player;
import main.Camera;
import main.GameConfig;

public class PlayingState implements GameState {
	
	private Player player;
	private List<Block> blocks;
	private Anomaly anomaly;
	private Core core;
	private CollisionSystem collision;
	private Camera cam;
	private boolean stageCompleted = false;
	
	public PlayingState() {
		initClasses();
	}
	
	private void initClasses() {
		collision = new CollisionSystem();
		player = new Player(200, 200);
		blocks = new ArrayList<>();
		core = new Core(GameConfig.SCREEN_WIDTH - 24, 220);
		cam = new Camera();
		
		for(int x = 0; x < GameConfig.SCREEN_WIDTH - 48; x += GameConfig.TILE_SIZE) {
			blocks.add(new Block(x, 492, BlockState.NORMAL, BlockType.NORMAL));
			
		}
		
		blocks.add(new Block(400, 444, BlockState.NORMAL, BlockType.BREAKABLE));
		blocks.add(new Block(48, 444, BlockState.NORMAL, BlockType.BREAKABLE));
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

	@Override
	public void update() {
		player.updateX();
	    collision.resolveX(player, blocks);

	    player.updateY();
	    collision.resolveY(player, blocks);
	    
	    anomaly.update();
	    collision.checkAnomalyDamage(player, anomaly);
	    collision.affectBlocks(blocks, anomaly);
	    
	    checkVoidDeath();
	    
	    if (core.onPLayerTouch(player)) {
	    	anomaly.freeze();
	    	stageCompleted = true;
	    }
		//levelWidth, levelHeight)
	    cam.follow(player, 2000, GameConfig.SCREEN_HEIGHT);
	}
	
	@Override
	public void render(Graphics g) {
		
		int camX = (int) cam.getX();
		int camY = (int)cam.getY();
		g.translate(-camX, -camY);
		
		anomaly.render(g);
		
		player.render(g);
		
		core.render(g);
		
		for (Block block : blocks) {
	        block.render(g);
	    }
		
		g.translate(camX, camY);
		
		if (isStageComplete()) {
			g.setFont(new Font("Arial", Font.BOLD, 28));
	        g.setColor(Color.WHITE);
	        g.drawString("Estágio concluído!", GameConfig.SCREEN_WIDTH / 2 - 100, GameConfig.SCREEN_HEIGHT / 2);
	    }
		
	}
	
	private void checkVoidDeath() {
		if (player.getY() > GameConfig.VOID_Y) {
			player.takeLife();
		}
		
	}
	
	public boolean isStageComplete() {
		return stageCompleted;
	}
	
	public Player getPlayer() {
	    return player;
	}

}
