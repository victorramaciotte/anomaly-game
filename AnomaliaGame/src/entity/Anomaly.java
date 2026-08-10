package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.GameConfig;

public class Anomaly extends Entity {
	private Direction d;
	private double startX = 0, startY = 0;
	private int affectedWidth = 0, affectedHeight = 0;
	private boolean frozen = false;
	
	public Anomaly(double x, double y, Direction d) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.SCREEN_HEIGHT);
		this.d = d;
		this.startX = x;
		this.startY = y;
		
	}
	
	
	public void update() {
		if (frozen) return; 
	    if(d.dx != 0) {
	    	if(d.dx > 0 && x > GameConfig.SCREEN_WIDTH) { return; }
	    	else if(d.dx < 0 && x < 0) { return; }
	    }
	    else {
	    	if(d.dy > 0 && y > GameConfig.SCREEN_HEIGHT) { return; }
	    	else if(d.dy < 0 && y < 0) { return; }
	    }
	    velocityX = d.dx * GameConfig.ANOMALY_BASE_SPEED;
		velocityY = d.dy * GameConfig.ANOMALY_BASE_SPEED;
	    x += velocityX;
	    y += velocityY;
	}
	
	public void freeze() {
		frozen = true;
	}
	
	public void render(Graphics g) {
	/**	g.setColor(Color.RED);
		g.fillRect((int) x, (int) y, (int) width, (int) height); **/
		
		Rectangle2D.Double anomalyArea = new Rectangle2D.Double();
		anomalyArea = getAffectedArea();
		g.setColor(Color.getHSBColor(80f / 360f, 0.35f, 0.18f));
		g.fillRect((int) anomalyArea.getX(), (int) anomalyArea.getY(), (int) anomalyArea.getWidth(), (int) anomalyArea.getHeight());
		
	}
	
	public Rectangle2D.Double getAffectedArea() {
		double areaX, areaY;
		
		if(d.dx != 0) {
			affectedWidth = (int) Math.abs(startX - x);
			affectedHeight = GameConfig.SCREEN_HEIGHT;
			areaX = Math.min(startX, x);
	        areaY = 0;
		}
		else {
			affectedHeight = (int) Math.abs(startY - y);
			affectedWidth = GameConfig.SCREEN_WIDTH;
	        areaX = 0;
	        areaY = Math.min(startY, y);
		}
		
		return new Rectangle2D.Double(areaX, areaY, affectedWidth, affectedHeight);
	}
}
