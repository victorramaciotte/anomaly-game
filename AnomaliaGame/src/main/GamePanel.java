package main;

import javax.swing.JPanel;

import entity.Anomaly;
import entity.Block;
import input.KeyboardInputs;
import input.MouseInputs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Game game;
	private MouseInputs mouseInputs;
	private int xDelta = 100, yDelta = 100;
	
	public GamePanel(Game game) {
		
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
		addKeyListener(new KeyboardInputs(game.getPlayer()));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		setBackground(Color.getHSBColor(200f / 360f, 0.25f, 0.12f));
	}	
	
	public void changeXDelta(int value) {
		this.xDelta += value;
	}
	
	public void changeYDelta(int value) {
		this.yDelta += value;
	}
	
	public void setRectPos(int x, int y) {
		this.xDelta = x;
		this.yDelta = y;
	}
	
	public void updateGame() {
		updateRectangle();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.getAnomaly().render(g);
		
		game.getPlayer().render(g);
		
		game.getCore().render(g);
		
		for (Block block : game.getBlocks()) {
	        block.render(g);
	    }
		
		if (game.isStageComplete()) {
			g.setFont(new Font("Arial", Font.BOLD, 28));
	        g.setColor(Color.WHITE);
	        g.drawString("Estágio concluído!", GameConfig.SCREEN_WIDTH / 2 - 100, GameConfig.SCREEN_HEIGHT / 2);
	    }
	}

	private void updateRectangle() {
		xDelta++;
		yDelta++;
		
	}
}
