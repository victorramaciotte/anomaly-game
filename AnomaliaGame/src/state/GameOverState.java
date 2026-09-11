package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.GameConfig;
import main.HighScore;
import main.ImageLoader;

public class GameOverState implements GameState {
	private GameStateManager stateManager;
	private boolean victory;
	private double finalTime;
    private boolean isNewRecord;
	private BufferedImage gameOverImage;
	private BufferedImage winImage;
	private BufferedImage restartMsg;
	private Font fontRecordTime;
	private Font fontTime;
	private BufferedImage recordBg;
	
	private boolean overlayVisible = true;
	private int blinkTickCounter = 0;
	private static final int VISIBLE_DURATION = 90;
	private static final int HIDDEN_DURATION = 20;

	public GameOverState(GameStateManager stateManager, boolean victory, double finalTime, boolean isNewRecord) {
		this.stateManager = stateManager;
		this.victory = victory;
		gameOverImage = ImageLoader.load("images/ui/gameover_screen.png");
		winImage = ImageLoader.load("images/ui/win_screen.png");
		restartMsg = ImageLoader.load("images/ui/restart_msg.png");
		recordBg = ImageLoader.load("images/ui/record_bg.png");
		this.finalTime = finalTime;
        this.isNewRecord = isNewRecord;
	}
	
	public GameOverState(GameStateManager stateManager, boolean victory) {
	    this(stateManager, victory, 0, false);
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
		
		if (victory) {
		    String timeText = "Tempo: " + formatTime(finalTime);
		    String recordText = isNewRecord ? "Novo recorde!" : "Recorde: " + formatTime(HighScore.load());
		    
		    int x = GameConfig.SCREEN_WIDTH - recordBg.getWidth();
	        int margin = 20;
	        g.drawImage(recordBg, x - margin, margin, null); 
		    
			fontRecordTime = ImageLoader.loadFont("fonts/Oxanium-SemiBold.ttf", 24f);

		    g.setFont(fontRecordTime);
		    g.setColor(Color.WHITE);
		    FontMetrics metrics = g.getFontMetrics(fontRecordTime);

		    int timeWidth = metrics.stringWidth(timeText);
		    int recordWidth = metrics.stringWidth(recordText);

		    int baseY = margin * 4;
		    int baseX = (GameConfig.SCREEN_WIDTH - recordBg.getWidth()) + (margin * 2);
		    g.drawString(timeText, baseX, baseY);
		    g.drawString(recordText, baseX, baseY + metrics.getHeight());
		}
	}
	
	private String formatTime(double seconds) {
        int total = (int) seconds;
        return String.format("%02d:%02d", total / 60, total % 60);
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
