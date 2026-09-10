package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GameConfig;
import main.ImageLoader;

public class GameOverState implements GameState {
	private GameStateManager stateManager;
	private boolean victory;
	private BufferedImage gameOverImage;
	private BufferedImage winImage;
	private BufferedImage restartMsg;
	
	private boolean overlayVisible = true;
	private int blinkTickCounter = 0;
	private static final int VISIBLE_DURATION = 90;
	private static final int HIDDEN_DURATION = 20;

	public GameOverState(GameStateManager stateManager, boolean victory) {
		this.stateManager = stateManager;
		this.victory = victory;
		gameOverImage = ImageLoader.load("images/ui/gameover_screen.png");
		winImage = ImageLoader.load("images/ui/win_screen.png");
		restartMsg = ImageLoader.load("images/ui/restart_msg.png");
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
		BufferedImage backgroundImage = victory ? winImage : gameOverImage;
		g.drawImage(backgroundImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);
		
		if (overlayVisible) {
			g.drawImage(restartMsg, GameConfig.SCREEN_WIDTH - restartMsg.getWidth() - 40, GameConfig.SCREEN_HEIGHT - 100, null);
		}
	}

	@Override
	public void onKeyPressed(int keyCode) {
		stateManager.showMenu();
		
	}

	@Override
	public void onKeyReleased(int keyCode) {
		// TODO Auto-generated method stub
		
	}

}
