package state;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import main.GameConfig;

public class PauseState implements GameState {
    private GameStateManager stateManager;
    private PlayingState playingState; // o estado "congelado" por trás

    public PauseState(GameStateManager stateManager, PlayingState playingState) {
        this.stateManager = stateManager;
        this.playingState = playingState;
    }

    @Override
    public void update() {
        // vazio de propósito — não chama playingState.update(),
        // é isso que "congela" o jogo
    }

    @Override
    public void render(Graphics g) {
        playingState.render(g); // desenha o jogo como estava, "por baixo"
        renderPauseOverlay(g);  // desenha "PAUSADO" por cima
    }

    private void renderPauseOverlay(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	
    	Composite og = g2d.getComposite();
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    	
    	g.setColor(Color.BLACK);
	    g.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
	    g2d.setComposite(og);

	    g.setFont(new Font("Arial", Font.BOLD, 32));
	    g.setColor(Color.WHITE);
	    FontMetrics metrics = g.getFontMetrics(new Font("Arial", Font.BOLD, 32));
	    int width = metrics.stringWidth("Jogo Pausado");
	    g.drawString("Jogo Pausado", (GameConfig.SCREEN_WIDTH - width) / 2, GameConfig.SCREEN_HEIGHT / 2 - 20);
	}

	@Override
    public void onKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_P) { 
            stateManager.setState(playingState); // volta pro MESMO objeto, intacto
        }
    }

    @Override
    public void onKeyReleased(int keyCode) {}
}