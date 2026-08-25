package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import input.KeyboardInputs;
import level.Campaign;
import level.Stages;
import main.GameConfig;

public class MenuState implements GameState {
	private GameStateManager stateManager;
	private boolean blinkVisible = true;
	private double blinkTimer = 0;

	public MenuState(GameStateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	@Override
	public void update() {
		updateBlink();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
	    g.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

	    g.setFont(new Font("Arial", Font.BOLD, 52));
	    g.setColor(Color.WHITE);
	    FontMetrics metrics = g.getFontMetrics(new Font("Arial", Font.BOLD, 52));
	    int width = metrics.stringWidth("ANOMALIA");
	    g.drawString("ANOMALIA", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 - 20);

	    g.setColor(Color.LIGHT_GRAY);
	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    metrics = g.getFontMetrics(new Font("Arial", Font.PLAIN, 18));
	    
	    width = metrics.stringWidth("A / D / setas: mover     ESPAÇO / W / seta pra cima: pulo / pulo duplo   P: pause");
	    g.drawString("A / D / setas: mover     ESPAÇO / W / seta pra cima: pulo / pulo duplo   P: pause", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 + 30);
	    
	    width = metrics.stringWidth("Chegue ao núcleo antes que a anomalia consuma o nível");
	    g.drawString("Chegue ao núcleo antes que a anomalia consuma o nível", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 + 100);
	    
	    g.setColor(Color.getHSBColor(200f / 360f, 0.55f, 0.98f));
	    g.setFont(new Font("Arial", Font.BOLD, 22));
	    metrics = g.getFontMetrics(new Font("Arial", Font.BOLD, 22));
	    width = metrics.stringWidth("Pressione qualquer tecla para começar o jogo");
	    if (blinkVisible) {
	    	g.drawString("Pressione qualquer tecla para começar o jogo", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 + 150);
	    }
	}
	
	public void updateBlink() {
	    blinkTimer += GameConfig.FIXED_DELTA;
	    if (blinkTimer >= 0.95) {
	        blinkVisible = !blinkVisible;
	        blinkTimer = 0;
	    }
	}

	@Override
	public void onKeyPressed(int keyCode) {
		Campaign campaign = Stages.createDefaultCampaign();
	    stateManager.setState(new PlayingState(campaign, stateManager));
		
	}

	@Override
	public void onKeyReleased(int keyCode) {
		// TODO Auto-generated method stub
		
	}

}
