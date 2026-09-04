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
        double idealY = player.getY() - GameConfig.SCREEN_HEIGHT / 2.0 - verticalOffset;
        
        x = clamp(idealX, 0, levelWidth - GameConfig.SCREEN_WIDTH);
        y = clamp(idealY, 0, levelHeight - GameConfig.SCREEN_HEIGHT);
    }
    
  
    public double clamp(double value, double min, double max) {
    	
    	double result = Math.max(min, Math.min(value, max));
    	
		return result;
    }
}