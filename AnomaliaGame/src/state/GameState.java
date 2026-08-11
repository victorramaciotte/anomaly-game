package state;

import java.awt.Graphics;

public interface GameState {
	void update();
	void render(Graphics g);
}
