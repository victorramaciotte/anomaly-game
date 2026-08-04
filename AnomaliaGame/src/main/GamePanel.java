package main;

import javax.swing.JPanel;

import entity.Block;
import input.KeyboardInputs;
import input.MouseInputs;

import java.awt.Graphics;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Game game;
	private MouseInputs mouseInputs;
	private int xDelta = 100, yDelta = 100;
	
	public GamePanel(Game game) {
		
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		addKeyListener(new KeyboardInputs(game.getPlayer()));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
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
		
		game.getPlayer().render(g);
		
		for (Block block : game.getBlocks()) {
	        block.render(g);
	    }
	}

	private void updateRectangle() {
		xDelta++;
		yDelta++;
		
	}
}
