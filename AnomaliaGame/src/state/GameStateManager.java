package state;

import java.awt.Graphics;
import input.KeyboardInputs;

public class GameStateManager {
    private GameState currentState;
    private final KeyboardInputs keyboardInputs;
    
    public GameStateManager(KeyboardInputs keyboardInputs) {
		this.keyboardInputs = keyboardInputs;
		showMenu();
	}

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void update() {
        currentState.update();
    }

    public void render(Graphics g) {
        currentState.render(g);
    }
    
    public void onKeyPressed(int keyCode) {
        currentState.onKeyPressed(keyCode);
    }
    public void onKeyReleased(int keyCode) {
        currentState.onKeyReleased(keyCode);
    }
    
    public void showMenu() {
        currentState = new MenuState(this);
    }

    public void showGameOver(boolean victory) {
        currentState = new GameOverState(this, victory);
    }
}