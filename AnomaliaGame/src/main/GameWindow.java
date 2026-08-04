package main;

import javax.swing.JFrame;

public class GameWindow {
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		jframe.setSize(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setVisible(true);
	}
}
