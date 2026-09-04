package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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
import main.Background;
import main.Camera;
import main.CorruptionOverlay;
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
	private double elapsedSeconds;
	private int margin = GameConfig.HUD_MARGIN;
	int levelHeight, levelWidth;
	private List<Background> background;
	private CorruptionOverlay corruptionOverlay;
	
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
	    
	    corruptionOverlay.update();
	    
	    checkVoidDeath();
	    if(player.isDead()) {
	    	stateManager.showGameOver(false);
	    }
	    
	    if (core.onPLayerTouch(player)) {
	    	anomaly.freeze();
	    	stageCompleted = true;
	    	
	    	if (campaign.hasNextStage()) {
	            campaign.advance();
	            loadStage(campaign.getCurrentStage(), player);
	        }
	    	else {
	    		stateManager.setState(new GameOverState(stateManager, true)); //vitória
	    	}
	    }
		//levelWidth, levelHeight)
	    camera.follow(player, levelWidth, levelHeight, campaign.getCurrentStage().getCameraVerticalOffset());
	    
	    elapsedSeconds += GameConfig.FIXED_DELTA;
	}
	
	@Override
	public void render(Graphics g) {
		for (Background layer : background) {
	        layer.render(g, camera, anomaly.getAffectedArea());
	    }
		
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
		corruptionOverlay.render(g, camera, anomaly.getAffectedArea());
		renderHUD(g);
		
	/*	if (isStageComplete()) {
			g.setFont(new Font("Arial", Font.BOLD, 28));
	        g.setColor(Color.WHITE);
	        g.drawString("Estágio concluído!", GameConfig.SCREEN_WIDTH / 2 - 100, GameConfig.SCREEN_HEIGHT / 2);
	    } */
		
		
	}
	
	private void renderHUD(Graphics g) {
		FontMetrics metrics = g.getFontMetrics(new Font("SansSerif", Font.BOLD, 16));
		
		g.setFont(new Font("SansSerif", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		int width = metrics.stringWidth("Nível: " + campaign.getCurrentStageNumber() + "/" + campaign.getTotalStages());
        g.drawString("Nível: " + campaign.getCurrentStageNumber() + "/" + campaign.getTotalStages(), GameConfig.SCREEN_WIDTH - width - margin, margin + metrics.getAscent());
        
        
        width = metrics.stringWidth("Tempo: " + formatTime(elapsedSeconds));
        g.drawString("Tempo: " + formatTime(elapsedSeconds), (GameConfig.SCREEN_WIDTH - width)/2, margin + metrics.getAscent());
        
        renderLives(g);
        renderHpBar(g);
		
	}
	
	private String formatTime(double seconds) {
        int total = (int) seconds;
        return String.format("%02d:%02d", total / 60, total % 60);
    }
	
	private void renderLives(Graphics g) {
		int diameter = 16;
	    int spacing = 24; // espaço entre uma bolinha e a próxima
	    int startX = 20;
	    int y = margin + spacing;

	    for (int i = 0; i < player.getLives(); i++) {
	        g.setColor(Color.RED);
	        g.fillOval(startX + i * spacing, y, diameter, diameter);
	    }
	}
	
	private void renderHpBar(Graphics g) {
	    int x = margin;
	    int y = margin;
	    int width = 200;
	    int height = 16;

	    double hpRatio = player.getHp() / GameConfig.MAX_HP; // valor entre 0.0 e 1.0
	    int filledWidth = (int) (width * hpRatio);

	    g.setColor(Color.DARK_GRAY);
	    g.fillRect(x, y, width, height); // fundo, representa a barra "vazia"

	    g.setColor(Color.GREEN);
	    g.fillRect(x, y, filledWidth, height); // preenchimento, proporcional ao HP

	    g.setColor(Color.WHITE);
	    g.drawRect(x, y, width, height); // contorno
	}

	private void checkVoidDeath() {
		if (player.getY() > levelHeight + GameConfig.VOID_MARGIN) {
			if (player.takeLife()) {
	            stateManager.showGameOver(false);
	        }
		}
		
	}
	
	private void loadStage(StageConfig config, Player player) {
	    LevelData levelData = LevelBuilder.build(config);
	    levelWidth = levelData.getLevelWidth(); 
	    levelHeight = levelData.getLevelHeight();
	    background = new ArrayList<>();
	    
	    
	    List<String> layerPaths = config.getBackgroundLayers();
	    List<String> corruptedPaths = config.getCorruptedBackgroundLayers();
	    double[] scrollFactors = {0.1, 0.3, 0.5, 0.8}; // fatores padrão, do mais distante pro mais próximo
	    
	    Background.Orientation orientation = (config.getAnomalyDirection() == Direction.LEFT_TO_RIGHT
	            || config.getAnomalyDirection() == Direction.RIGHT_TO_LEFT)
	            ? Background.Orientation.HORIZONTAL
	            : Background.Orientation.VERTICAL;

	    for (int i = 0; i < layerPaths.size(); i++) {
	        BufferedImage normalImg = loadImage(layerPaths.get(i));
	        BufferedImage corruptedImg = (corruptedPaths.get(i) != null) ? loadImage(corruptedPaths.get(i)) : null;
	        double factor = (i < scrollFactors.length) ? scrollFactors[i] : 1.0;

	        background.add(new Background(normalImg, corruptedImg, factor, orientation));
	    }
	    
	    List<BufferedImage> overlayImages = new ArrayList<>();
	    for (String path : config.getOverlayFrames()) {
	        overlayImages.add(loadImage(path));
	    }
	    corruptionOverlay = new CorruptionOverlay(overlayImages, 20); // 20 ticks por frame, ajuste ao gosto
	    
	    player.setSpawnPoint(config.getStartX(), config.getStartY());
	    player.respawn();
	    blocks = levelData.getBlocks();
	    core = new Core(levelData.getCoreX(), levelData.getCoreY());
	    anomaly = new Anomaly(0, 0, config.getAnomalyDirection(), config.getAnomalySpeed(), levelWidth, levelHeight);
	    camera = new Camera();
	    stageCompleted = false; // reseta pro próximo estágio
	}
	
	private BufferedImage loadImage(String path) {
	    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
	        return ImageIO.read(is);
	    } catch (IOException e) {
	        throw new RuntimeException("Falha ao carregar imagem: " + path, e);
	    }
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
	        case KeyEvent.VK_W: player.requestJump(); break;
	        case KeyEvent.VK_SPACE: player.requestJump(); break;
	        case KeyEvent.VK_UP: player.requestJump(); break;
	        case KeyEvent.VK_LEFT: player.setLeft(true); break;
	        case KeyEvent.VK_RIGHT: player.setRight(true); break;
	        case KeyEvent.VK_P: stateManager.setState(new PauseState(stateManager, this)); break;
	    }
	}

	@Override
	public void onKeyReleased(int keyCode) {
	    switch (keyCode) {
	        case KeyEvent.VK_A: player.setLeft(false); break;
	        case KeyEvent.VK_D: player.setRight(false); break;
	        case KeyEvent.VK_LEFT: player.setLeft(false); break;
	        case KeyEvent.VK_RIGHT: player.setRight(false); break;
	    }
	}

}
