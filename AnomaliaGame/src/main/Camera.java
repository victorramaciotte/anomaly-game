package main;

import entity.Player;

public class Camera {
    private double x;
    private double y;
    
    public Camera() {
    	
    }

    public double getX() { return x; }
    public double getY() { return y; }
    
    public void follow(Player player, int levelWidth, int levelHeight, double verticalOffset) {
        double idealX = player.getX() - GameConfig.SCREEN_WIDTH / 2.0;
        
        x = clamp(idealX, 0, levelWidth - GameConfig.SCREEN_WIDTH);
        y = verticalOffset;
    }
    
  
    public double clamp(double value, double min, double max) {
    	
    	double result = Math.max(min, Math.min(value, max));
    	
		return result;
    }
}