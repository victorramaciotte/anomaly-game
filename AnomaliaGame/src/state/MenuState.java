package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import input.KeyboardInputs;
import level.Campaign;
import level.Stages;
import main.GameConfig;

public class MenuState implements GameState {
	private GameStateManager stateManager;

	public MenuState(GameStateManager stateManager) {
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
	    g.drawString("Anomalia", GameConfig.SCREEN_WIDTH / 2 - 80, GameConfig.SCREEN_HEIGHT / 2 - 20);

	    g.setFont(new Font("Arial", Font.PLAIN, 18));
	    g.drawString("Pressione qualquer tecla para começar o jogo", GameConfig.SCREEN_WIDTH / 2 - 140, GameConfig.SCREEN_HEIGHT / 2 + 20);
		
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
