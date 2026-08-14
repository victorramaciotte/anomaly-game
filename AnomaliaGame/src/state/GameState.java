package state;

import java.awt.Graphics;

public interface GameState {
	void update();
	void render(Graphics g);
	void onKeyPressed(int keyCode);
	void onKeyReleased(int keyCode);
}
