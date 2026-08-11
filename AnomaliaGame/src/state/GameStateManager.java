package state;

import java.awt.Graphics;

public class GameStateManager {
    private GameState currentState;

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void update() {
        currentState.update();
    }

    public void render(Graphics g) {
        currentState.render(g);
    }
}