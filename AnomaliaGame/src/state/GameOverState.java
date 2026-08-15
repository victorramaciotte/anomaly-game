package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import main.GameConfig;

public class GameOverState implements GameState {
	private GameStateManager stateManager;

	public GameOverState(GameStateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

	    g.setFont(new Font("Arial", Font.BOLD, 32));
	    g.setColor(Color.WHITE);
	    FontMetrics metrics = g.getFontMetrics(new Font("Arial", Font.BOLD, 32));
	    int width = metrics.stringWidth("Game Over");
	    g.drawString("Game Over", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 - 20);

	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    metrics = g.getFontMetrics(new Font("Arial", Font.PLAIN, 18));
	    width = metrics.stringWidth("Pressione qualquer tecla para reiniciar");
	    g.drawString("Pressione qualquer tecla para reiniciar", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 + 20);
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
