package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main.GameConfig;

public class Block extends Entity {
	private boolean visible = true;
	private BlockState state;
	private BlockType type;
	
	public Block(double x, double y, BlockState state, BlockType type) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
		this.type = type;
		this.state = state;
	}
	
	public void setVisible(boolean visible) { this.visible = visible; }
	
	public void render(Graphics g) {
		if (!visible || !isSolid()) return;
		
		g.setColor(Color.getHSBColor(200f / 360f, 0.20f, 0.35f));
		if(state == BlockState.CORRUPTED) { g.setColor(Color.getHSBColor(80f / 360f, 0.45f, 0.40f)); }
		if(state == BlockState.CRACKED) { g.setColor(Color.getHSBColor(80f / 360f, 0.65f, 0.55f)); }
		g.fillRect((int) x, (int) y, (int) GameConfig.TILE_SIZE, (int) GameConfig.TILE_SIZE);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(Color.getHSBColor(200f / 360f, 0.25f, 0.12f));
		if(state == BlockState.CORRUPTED) { g2d.setColor(Color.getHSBColor(80f / 360f, 0.35f, 0.18f)); }
		g2d.drawRect((int) x, (int) y, (int) GameConfig.TILE_SIZE, (int) GameConfig.TILE_SIZE);
		g2d.setStroke(new BasicStroke(0));
	}
	
	public void corrupt() {
		if (state != BlockState.NORMAL) return;
		state = BlockState.CORRUPTED;
	}
	
	public void crack() {
		if (type != BlockType.BREAKABLE || state != BlockState.CORRUPTED) return;
		state = BlockState.CRACKED;
	}
	
	public void destroy() {
		if (state != BlockState.CRACKED) return;
		state = BlockState.DESTROID;
	}
	
	public boolean isSolid() {
		return state != BlockState.DESTROID;
	}
	
	public void stepOn() {
		if(state == BlockState.CORRUPTED) {
			crack();
		}
		else if(state == BlockState.CRACKED) {
			destroy();
		}
	}
	
	public BlockState getState() {
		return state;
	}
}
