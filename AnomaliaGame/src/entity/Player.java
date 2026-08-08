package entity;

import java.awt.Color;
import java.awt.Graphics;

import main.GameConfig;

public class Player extends Entity {
	
	private boolean left, right, onGround, jumpRequested;
	private int jumpsUsed = 0;
	private double hp = GameConfig.MAX_HP;
	private int lives = GameConfig.STARTING_LIVES;
	private double startingX, startingY;
	
	public Player(double x, double y) {
		super(x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
		this.startingX = x;
		this.startingY = y;
	}
	
	public void setLeft(boolean value) { this.left = value; }
    public void setRight(boolean value) { this.right = value; }
    public void requestJump() { this.jumpRequested = true; }
	
    public void updateX() {
        updateHorizontalMovement();
        x += velocityX;
    }
    
    public void updateY() {
        updateJump();
        applyGravity();
        y += velocityY;
    }

    private void updateHorizontalMovement() {
        velocityX = 0;
        if (left) velocityX -= GameConfig.MOVE_SPEED;
        if (right) velocityX += GameConfig.MOVE_SPEED;
    }

    private void updateJump() {
        if (jumpRequested && jumpsUsed < GameConfig.MAX_JUMPS) {
            velocityY = GameConfig.JUMP_FORCE;
            jumpsUsed++;
        }
        jumpRequested = false; // consome o pedido, seja executado ou não
    }

	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int) x, (int) y, (int) width, (int) height);
	}
	
	private void applyGravity() {
        velocityY += GameConfig.GRAVITY;
    }

    // Vai ser chamado pelo CollisionSystem quando o player tocar o chão
    public void resetJumps() {
        jumpsUsed = 0;
    }
    
    public double getVelocityY() {
    	return velocityY;
    }
    
    public double getVelocityX() {
    	return velocityX;
    }
    
    
    public void landOn(double newY) {
    	this.y = newY;
    	velocityY = 0;
    	resetJumps();
    }
    
    public void setY(double newY) {
    	this.y = newY;
    	velocityY = 0;
    }
    
    public void setX(double newX) {
    	this.x = newX;
    	velocityX = 0;
    }
    
    public void respawn() {
    	setX(startingX);
    	setY(startingY);
    }
    
    public void takeLife() {
    	lives--;
    	hp = GameConfig.MAX_HP;
    	respawn();
    }
    
    public boolean takeDamage(double damage) {
    	boolean fatalDamage = false;
    	
    	hp = Math.max(0, (hp - damage));
    	
    	if(hp <= 0) { fatalDamage = true; }
    	System.out.println(hp);
    	
    	return fatalDamage;
    }
    
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean value) { this.onGround = value; }
}
