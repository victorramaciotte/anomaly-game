package main;

import entity.Player;

public class Camera {
    private double x;
    private double y;
    
    public Camera() {
    	
    }

    public double getX() { return x; }
    public double getY() { return y; }
    
    public void follow(Player player, int levelWidth, int levelHeight) {
        double idealX = player.getX() - GameConfig.SCREEN_WIDTH / 2.0;
        double idealY = player.getY() - GameConfig.SCREEN_HEIGHT / 2.0;
        
        x = clamp(0, idealX, levelWidth - GameConfig.SCREEN_WIDTH);
        y = clamp(0, idealY, levelHeight - GameConfig.SCREEN_HEIGHT);
    }
    
  
    public double clamp(double value, double min, double max) {
    	
    	double result = Math.max(min, Math.min(value, max));
    	
		return result;
    }
}