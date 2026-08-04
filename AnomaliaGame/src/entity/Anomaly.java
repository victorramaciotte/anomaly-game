package entity;

import java.awt.Color;
import java.awt.Graphics;

import main.GameConfig;

public class Anomaly extends Entity {
	private Direction d;
	
	public Anomaly(double x, double y, Direction d) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.SCREEN_HEIGHT);
		this.d = d;
		
	}
	
	
	public void update() {
		velocityX = d.dx * GameConfig.ANOMALY_BASE_SPEED;
		velocityY = d.dy * GameConfig.ANOMALY_BASE_SPEED;
		
		x += velocityX;
	    y += velocityY;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int) x, (int) y, (int) width, (int) height);
	}
}
