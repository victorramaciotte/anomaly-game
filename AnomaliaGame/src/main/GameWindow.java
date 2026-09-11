package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameWindow {
	private JFrame jframe;
	private Game game;
	
	public GameWindow(GamePanel gamePanel, Game game) {
		jframe = new JFrame("SOLUTUS");
		setWindowIcon(jframe, "images/ui/icon.png");
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jframe.setResizable(false);
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
	
	private void setWindowIcon(JFrame frame, String path) {
	    try {
	        BufferedImage icon = ImageLoader.load(path);
	        if (icon != null) {
	            frame.setIconImage(icon);
	        }
	    } catch (Exception e) {
	        System.err.println("Falha ao carregar ícone da janela: " + e.getMessage());
	    }
	}

}
