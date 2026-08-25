package entity;

import java.awt.Color;
import java.awt.Graphics;

import main.GameConfig;

public class Player extends Entity {
	
	private boolean left, right, onGround, jumpRequested;
	private int jumpsUsed = 0;
	private double hp = GameConfig.MAX_HP;
	private int lives = GameConfig.STARTING_LIVES;
	private double startingX, startingY, fallStartY;
	private boolean dead;
	
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
		int arc = 20;
		g.setColor(Color.getHSBColor(15f / 360f, 0.65f, 0.70f));
		g.fillRoundRect((int) x, (int) y, (int) width, (int) height, arc, arc);
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
    	fallStartY = startingY;
    	setOnGround(false);
    	resetJumps();
    }
    
    public void setSpawnPoint(double x, double y) {
        this.startingX = x;
        this.startingY = y;
    }
    
    public boolean takeLife() {
    	lives--;
    	
    	if(lives <= 0) dead = true;
    	
    	hp = GameConfig.MAX_HP;
    	respawn();
    	return dead;
    }
    
    public boolean isDead() {
    	return dead;
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
    
    public void startFalling() {
        fallStartY = y;
    }

    public void checkFallDamage() {
        double fallDistance = y - fallStartY;
        if (fallDistance >= GameConfig.FALL_DAMAGE_MIN_HEIGHT) {
            takeDamage(GameConfig.FALL_DAMAGE_AMOUNT);
        }
    }

	public int getLives() {
		return lives;
	}

	public double getHp() {
		return hp;
	}
}
