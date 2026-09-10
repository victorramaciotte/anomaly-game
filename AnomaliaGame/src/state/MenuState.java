package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import input.KeyboardInputs;
import level.Campaign;
import level.Stages;
import main.GameConfig;
import main.ImageLoader;

public class MenuState implements GameState {
	private GameStateManager stateManager;
	private boolean blinkVisible = true;
	private BufferedImage backgroundImage;
	private BufferedImage vignetteImage;
	private BufferedImage controlsImage;
	private BufferedImage warningOverlay;
	private BufferedImage startMsg;

	//parte que pisca
	private boolean overlayVisible = true;
	private int blinkTickCounter = 0;
	private static final int VISIBLE_DURATION = 90;
	private static final int HIDDEN_DURATION = 20; 

	public MenuState(GameStateManager stateManager) {
		this.stateManager = stateManager;
		backgroundImage = ImageLoader.load("images/ui/start_screen.png");
	    vignetteImage = ImageLoader.load("images/ui/overlay.png");
	    controlsImage = ImageLoader.load("images/ui/overlay_controls.png");
	    warningOverlay = ImageLoader.load("images/ui/warning.png");
	    startMsg = ImageLoader.load("images/ui/start_msg.png");
	}
	
	@Override
	public void update() {
	    blinkTickCounter++;

	    int currentDuration = overlayVisible ? VISIBLE_DURATION : HIDDEN_DURATION;

	    if (blinkTickCounter >= currentDuration) {
	        blinkTickCounter = 0;
	        overlayVisible = !overlayVisible;
	    }
	}

	@Override
	public void render(Graphics g) {
	    g.drawImage(backgroundImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);

	    if (overlayVisible) {
	        g.drawImage(warningOverlay, (GameConfig.SCREEN_WIDTH - warningOverlay.getWidth())/ 2, 100, null);
	    }

	    g.drawImage(vignetteImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);

	    g.drawImage(controlsImage, GameConfig.SCREEN_WIDTH - controlsImage.getWidth(), 0, null);
	    
	    if (overlayVisible) {
	        g.drawImage(startMsg, GameConfig.SCREEN_WIDTH - startMsg.getWidth() - 40, GameConfig.SCREEN_HEIGHT - 100, null);
	    }
	}

	@Override
	public void onKeyPressed(int keyCode) {
	    Campaign campaign = Stages.createDefaultCampaign();
	    stateManager.setState(new LoadingState(stateManager, campaign));
	}

	@Override
	public void onKeyReleased(int keyCode) {
		// TODO Auto-generated method stub
		
	}

}
