package entity;

import java.awt.Color;
import java.awt.Graphics;

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
		g.setColor(Color.DARK_GRAY);
		if(state == BlockState.CORRUPTED) { g.setColor(Color.GRAY); }
		if(state == BlockState.CRACKED) { g.setColor(Color.LIGHT_GRAY); }
		g.fillRect((int) x, (int) y, (int) GameConfig.TILE_SIZE, (int) GameConfig.TILE_SIZE);
		
		g.setColor(Color.WHITE);
		g.drawRect((int) x, (int) y, (int) GameConfig.TILE_SIZE, (int) GameConfig.TILE_SIZE);
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
}
