package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
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
import input.KeyboardInputs;
import level.Campaign;
import level.LevelBuilder;
import level.LevelData;
import level.StageConfig;
import main.Camera;
import main.GameConfig;

public class PlayingState implements GameState {
	
	private Player player;
	private List<Block> blocks;
	private Anomaly anomaly;
	private Core core;
	private CollisionSystem collision;
	private Camera camera;
	private boolean stageCompleted = false;
	private Campaign campaign;
	private GameStateManager stateManager;
	private GameOverState gameOver;
	
	public PlayingState(Campaign campaign, GameStateManager stateManager) {
	    this.campaign = campaign;
	    this.stateManager = stateManager;
	    collision = new CollisionSystem();
	    player = new Player(0, 0);
	    loadStage(campaign.getCurrentStage(), player);
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
	    if(player.isDead()) {
	    	stateManager.showGameOver();
	    }
	    
	    if (core.onPLayerTouch(player)) {
	    	anomaly.freeze();
	    	stageCompleted = true;
	    	
	    	if (campaign.hasNextStage()) {
	            campaign.advance();
	            loadStage(campaign.getCurrentStage(), player);
	        }
	    }
		//levelWidth, levelHeight)
	    camera.follow(player, 2000, GameConfig.SCREEN_HEIGHT);
	}
	
	@Override
	public void render(Graphics g) {
		
		int camX = (int) camera.getX();
		int camY = (int)camera.getY();
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
	
	private void loadStage(StageConfig config, Player player) {
	    LevelData levelData = LevelBuilder.build(config);
	    player.setSpawnPoint(config.getStartX(), config.getStartY());
	    player.respawn();
	    blocks = levelData.getBlocks();
	    core = new Core(levelData.getCoreX(), levelData.getCoreY());
	    anomaly = new Anomaly(0, 0, config.getAnomalyDirection(), config.getAnomalySpeed());
	    camera = new Camera();
	    stageCompleted = false; // reseta pro próximo estágio
	}
	
	public boolean isStageComplete() {
		return stageCompleted;
	}
	
	public Player getPlayer() {
	    return player;
	}
	
	@Override
	public void onKeyPressed(int keyCode) {
	    switch (keyCode) {
	        case KeyEvent.VK_A: player.setLeft(true); break;
	        case KeyEvent.VK_D: player.setRight(true); break;
	        case KeyEvent.VK_SPACE: player.requestJump(); break;
	    }
	}

	@Override
	public void onKeyReleased(int keyCode) {
	    switch (keyCode) {
	        case KeyEvent.VK_A: player.setLeft(false); break;
	        case KeyEvent.VK_D: player.setRight(false); break;
	    }
	}

}
