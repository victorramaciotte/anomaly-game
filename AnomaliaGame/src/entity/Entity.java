package entity;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
	
	protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected double velocityX;
    protected double velocityY;

    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
    }
    

	public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
