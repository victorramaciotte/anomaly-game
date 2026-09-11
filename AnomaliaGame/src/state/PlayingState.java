package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
import main.HighScore;
import main.ImageLoader;

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
	private BufferedImage levelBadge;      
	private BufferedImage hpFrame;         
	private BufferedImage lifeIcon;    
	private boolean showingStageComplete = false;
	private int stageCompleteTicksRemaining;
	private static final int STAGE_COMPLETE_DURATION = 350; 
	private BufferedImage stageCompleteImage;
	private BufferedImage lastStageCompleteImage;
	
	private Font fontTime;
	private Font fontLevelNum;
	private Font fontLevelInfo;
	private Font fontLevelTitle;
	
	public PlayingState(Campaign campaign, GameStateManager stateManager) {
	    this.campaign = campaign;
	    this.stateManager = stateManager;
	    collision = new CollisionSystem();
	    player = new Player(0, 0);
	    loadStage(campaign.getCurrentStage(), player);
	}

	@Override
	public void update() {
		if (showingStageComplete) {
	        stageCompleteTicksRemaining--;
	        if (stageCompleteTicksRemaining <= 0) {
	            showingStageComplete = false;
	            advanceOrFinish(); // só agora troca de estágio de verdade
	        }
	        return; // pula todo o resto do update enquanto o popup está na tela
	    }
		
		if (player.isDying()) {
		    player.tickDeath();
		} else {
			
			if (player.isHurt()) {
	            player.tickHurt();
	        }
		    player.updateX();
		    collision.resolveX(player, blocks);
		    player.updateY();
		    collision.resolveY(player, blocks, anomaly);
		}
		player.updateAnimation();
	    anomaly.update();
	    collision.checkAnomalyDamage(player, anomaly);
	    collision.affectBlocks(blocks, anomaly);
	    
	    corruptionOverlay.update();
	    
	    checkVoidDeath();
	    if (player.isDying()) return;
	    if(player.isDead()) {
	    	stateManager.showGameOver(false);
	    }
	    
	    if (core.onPLayerTouch(player)) {
	    	anomaly.freeze();
	    	stageCompleted = true;
	    	showingStageComplete = true;
	        stageCompleteTicksRemaining = STAGE_COMPLETE_DURATION;
	    }
		
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
		
		for (Block block : blocks) {
	        block.render(g);
	    }
		
		player.render(g);
		
		core.render(g);
		
		
		anomaly.render(g);
		
		g.translate(camX, camY);
		corruptionOverlay.render(g, camera, anomaly.getAffectedArea());
		renderHUD(g);
		
		if (showingStageComplete) {
        int x = (GameConfig.SCREEN_WIDTH - stageCompleteImage.getWidth()) / 2;
        int y = (GameConfig.SCREEN_HEIGHT - stageCompleteImage.getHeight()) / 2;
        if(campaign.hasNextStage()) { g.drawImage(stageCompleteImage, x, y, null); } 
        else { g.drawImage(lastStageCompleteImage, x, y, null); }
        
		}
	}
	
	private void advanceOrFinish() {
	    if (campaign.hasNextStage()) {
	        campaign.advance();
	        loadStage(campaign.getCurrentStage(), player);
	    } else {
	        double bestTime = HighScore.load();
	        boolean isNewRecord = elapsedSeconds < bestTime;
	        if (isNewRecord) {
	            HighScore.save(elapsedSeconds);
	        }
	        stateManager.setState(new GameOverState(stateManager, true, elapsedSeconds, isNewRecord));
	    }
	}
	
	public void loadHudAssets() {
	    levelBadge = ImageLoader.load("images/ui/level_bg.png");
	    hpFrame = ImageLoader.load("images/ui/life_frame.png");
	    lifeIcon = ImageLoader.load("images/ui/life.png");
	    fontTime = ImageLoader.loadFont("fonts/Oxanium-SemiBold.ttf", 16f);
		fontLevelNum = ImageLoader.loadFont("fonts/Oxanium-SemiBold.ttf", 24f);
		fontLevelInfo = ImageLoader.loadFont("fonts/Oxanium-Regular.ttf", 14f);
		fontLevelTitle = ImageLoader.loadFont("fonts/Oxanium-SemiBold.ttf", 14f);
	}
	
	private void renderHUD(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

	    // 2. Remove serrilhado de textos e formas geométricas
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		loadHudAssets();
	    renderTime(g);
	    renderLevelInfo(g);
	    renderHpAndLives(g);
	}
	
	private void renderTime(Graphics g) {
	    g.setFont(fontTime);
	    g.setColor(Color.WHITE);
	    FontMetrics metrics = g.getFontMetrics(fontTime);
	    
	    String timeText = "Tempo: " + formatTime(elapsedSeconds);
	    int width = metrics.stringWidth(timeText);
	    
	    g.drawString(timeText, (GameConfig.SCREEN_WIDTH - width) / 2, margin + metrics.getAscent());
	}
	
	private String formatTime(double seconds) {
        int total = (int) seconds;
        return String.format("%02d:%02d", total / 60, total % 60);
    }
	
	private void renderLevelInfo(Graphics g) {
	    if (levelBadge == null) return;

	    // Posiciona o fundo da fase no canto superior direito
	    int bgX = GameConfig.SCREEN_WIDTH - (int) (levelBadge.getWidth() * 0.6);
	    int bgY = margin;
	    
	    int drawWidth = (int) (levelBadge.getWidth() * 0.6);
        int drawHeight = (int) (levelBadge.getHeight() * 0.6);

	    // 1. Desenha a imagem de fundo da informação da fase
	    g.drawImage(levelBadge, bgX, bgY, drawWidth, drawHeight, null);

	    // 2. Textos do nível
	    String levelNumStr = String.valueOf(campaign.getCurrentStageNumber());
	    // Substitua pelo nome/informação real da sua fase se houver no seu StageConfig:
	    String levelTitle = campaign.getCurrentStage().getLevelTitle(); 
	    String levelInfoStr = campaign.getCurrentStageNumber() - 1 + " de " + campaign.getTotalStages() + " núcleos contidos"; 

	    FontMetrics numMetrics = g.getFontMetrics(fontLevelNum);
	    
	    // Alinhamento vertical e margem interna (padding) no PNG
	    int paddingX = 16;
	    int textY = bgY + (int) (drawHeight / 2) + (numMetrics.getAscent() / 3);

	    // Número do Nível (Em PRETO)
	    g.setFont(fontLevelNum);
	    g.setColor(Color.BLACK);
	    g.drawString(levelNumStr, bgX + paddingX, textY);
	    
	    textY = bgY + (int) (drawHeight / 3) + (numMetrics.getAscent() / 3);

	    // Informação/Nome ao lado (Em BRANCO, menor)
	    int numWidth = numMetrics.stringWidth(levelNumStr);
	    paddingX *= 5;
	    g.setFont(fontLevelTitle);
	    g.setColor(Color.WHITE);
	    g.drawString(levelTitle, bgX + paddingX + numWidth, textY - 2);
	    g.setFont(fontLevelInfo);
	    g.drawString(levelInfoStr, bgX + paddingX + numWidth, textY + 15);
	}
	
	private void renderHpAndLives(Graphics g) {
	    int frameX = margin/2;
	    int frameY = margin;

	    // =========================================================================
	    // CONFIGURAÇÃO DOS OFFSETS (Ajuste esses pixels conforme a sua imagem PNG)
	    // =========================================================================
	    int hpOffsetX = frameX;      // Distância X onde começa a barra de vida dentro do PNG
	    int hpOffsetY = frameY - 8;      // Distância Y onde começa a barra de vida dentro do PNG
	    int maxBarWidth = (int) (hpFrame.getWidth() * 0.5) - 10;   // Largura máxima da área transparente/vazada do HP
	    int barHeight = (int) (hpFrame.getHeight() * 0.5) - 20;      // Altura da barra de HP

	    int livesOffsetX = 16;   // Posição X onde as vidas começam na imagem
	    int livesOffsetY = 32;   // Posição Y onde as vidas ficam sobre a imagem
	    int lifeDiameter = 12;   // Tamanho de cada círculo de vida
	    int spacing = 20;        // Espaçamento entre as vidas
	    // =========================================================================

	    // 1. DESENHA A BARRA DE HP (Por BAIXO do PNG)
	    double hpRatio = player.getHp() / GameConfig.MAX_HP;
	    int filledWidth = (int) (maxBarWidth * hpRatio);

	    // Fundo escuro (Barra vazia)
	    g.setColor(Color.DARK_GRAY);
	    int arcRadius = 10;
	    g.fillRoundRect(frameX + hpOffsetX, frameY + hpOffsetY, maxBarWidth, barHeight, arcRadius, arcRadius);

	    // Preenchimento do HP
	    g.setColor(Color.WHITE);
	    if(filledWidth < (GameConfig.MAX_HP * 0.4)) { g.setColor(Color.RED); }
	    g.fillRoundRect(frameX + hpOffsetX, frameY + hpOffsetY, filledWidth, barHeight, arcRadius, arcRadius);

	    // 2. DESENHA A MOLDURA PNG (Por CIMA da barra de HP)
	    if (hpFrame != null) {
	    	int drawWidth = (int) (hpFrame.getWidth() * 0.5);
	        int drawHeight = (int) (hpFrame.getHeight() * 0.5);
	        
	        g.drawImage(hpFrame, frameX, frameY, drawWidth, drawHeight, null);
	    }

	    // 3. DESENHA AS VIDAS (Por CIMA do PNG)
	    int startX = margin * 2;
	    int y = (int) (hpFrame.getHeight() * 0.5) - 5; 
	    
	    int drawWidth = (int) (lifeIcon.getWidth() * 0.4);
        int drawHeight = (int) (lifeIcon.getHeight() * 0.4);
        
	    for (int i = 0; i < player.getLives(); i++) {
	        g.drawImage(lifeIcon, startX + i * spacing, y, drawWidth, drawHeight, null);
	    }
	    
	}
	
	

	private void checkVoidDeath() {
		if (player.getY() > levelHeight + GameConfig.VOID_MARGIN) {
			if (player.isDead()) {
	            stateManager.showGameOver(false);
	        }
			
			player.finishDeath();
		}
		
	}
	
	private void loadStage(StageConfig config, Player player) {
	    LevelData levelData = LevelBuilder.build(config);
	    levelWidth = levelData.getLevelWidth(); 
	    levelHeight = levelData.getLevelHeight();
	    background = new ArrayList<>();
	    stageCompleteImage = ImageLoader.load("images/ui/stage_complete.png");
	    lastStageCompleteImage = ImageLoader.load("images/ui/last_stage_complete.png");
	    
	    List<String> layerPaths = config.getBackgroundLayers();
	    List<String> corruptedPaths = config.getCorruptedBackgroundLayers();
	    double[] scrollFactors = {0.1, 0.3, 0.5, 0.8}; // fatores padrão, do mais distante pro mais próximo
	    
	    Background.Orientation orientation = (config.getAnomalyDirection() == Direction.LEFT_TO_RIGHT
	            || config.getAnomalyDirection() == Direction.RIGHT_TO_LEFT)
	            ? Background.Orientation.HORIZONTAL
	            : Background.Orientation.VERTICAL;

	    for (int i = 0; i < layerPaths.size(); i++) {
	        BufferedImage normalImg = ImageLoader.load(layerPaths.get(i));
	        BufferedImage corruptedImg = (corruptedPaths.get(i) != null) ? ImageLoader.load(corruptedPaths.get(i)) : null;
	        double factor = (i < scrollFactors.length) ? scrollFactors[i] : 1.0;

	        background.add(new Background(normalImg, corruptedImg, factor, orientation));
	    }
	    
	    List<BufferedImage> overlayImages = new ArrayList<>();
	    for (String path : config.getOverlayFrames()) {
	        overlayImages.add(ImageLoader.load(path));
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
