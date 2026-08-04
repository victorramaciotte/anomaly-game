package entity;

import java.awt.Graphics;

import main.GameConfig;

public class Block extends Entity {
	private boolean visible = true;
	
	public Block(double x, double y) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
	}
	
	public void setVisible(boolean visible) { this.visible = visible; }
	
	public void render(Graphics g) {
		if (!visible) return;
		g.fillRect((int) x, (int) y, (int) GameConfig.TILE_SIZE, (int) GameConfig.TILE_SIZE);
	}
}
