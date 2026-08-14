package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import state.GameStateManager;

public class KeyboardInputs implements KeyListener {

	private GameStateManager stateManager;
	
	public KeyboardInputs(GameStateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		stateManager.onKeyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		stateManager.onKeyReleased(e.getKeyCode());
	}

}
