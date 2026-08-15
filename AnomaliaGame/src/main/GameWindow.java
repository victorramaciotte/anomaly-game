package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameWindow {
	private JFrame jframe;
	private Game game;
	
	public GameWindow(GamePanel gamePanel, Game game) {
		jframe = new JFrame("Anomalia - Protótipo do Jogo");
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		this.game = game;
		
		jframe.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        game.stop();
		    }
		});
		
		jframe.setVisible(true);
		
	}
	
	

}
