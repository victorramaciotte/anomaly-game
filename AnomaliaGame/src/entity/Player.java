package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameConfig;
import main.ImageLoader;

public class Player extends Entity {
	
	private boolean left, right, onGround, jumpRequested, facing_left;
	private int jumpsUsed = 0;
	private double hp = GameConfig.MAX_HP;
	private int lives = GameConfig.STARTING_LIVES;
	private double startingX, startingY, fallStartY;
	private boolean dead;
	private Animation idleAnimation;
	private Animation runAnimation;
	private Animation jumpAnimation;
	private Animation hurtAnimation;
	private Animation fallAnimation;
	private Animation deathAnimation;
	private Animation currentAnimation;
	private boolean dying;
	private int deathTicksRemaining;
	private static final int DEATH_ANIMATION_DURATION = 360; 
	private boolean hurt = false;
	private int hurtTicksRemaining = 0;
	private static final int HURT_DURATION = 20;
	private int flashTicks = 0;
	private BufferedImage redOverlay;
	
	public Player(double x, double y) {
		super(x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
		this.startingX = x;
		this.startingY = y;
		loadAnimations();
	}
	
	public void setLeft(boolean value) { 
		this.left = value;
		facing_left = true;
	}
	
    public void setRight(boolean value) { 
    	this.right = value; 
    	facing_left = false;
    	}
    
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
    
    public void updateAnimation() {
        Animation newAnimation;

        if (dying) {
            currentAnimation = deathAnimation;
            currentAnimation.update();
            return;
        }
        
        if (hurt) {
            newAnimation = hurtAnimation;
        } else if (!onGround) {
            newAnimation = (velocityY < 0) ? jumpAnimation : fallAnimation; // subindo vs caindo
        } else if (velocityX != 0) {
            newAnimation = runAnimation;
        } else {
            newAnimation = idleAnimation;
        }

        if (newAnimation != currentAnimation) {
            currentAnimation = newAnimation;
            currentAnimation.reset();
        }
        currentAnimation.update();
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage frame = currentAnimation.getCurrentFrame();

        int drawWidth = (int) (height * 1.3);
        int drawHeight = (int) (height * 1.3);
        int drawX = (int) (x + width / 2 - drawWidth / 2.0);
        int drawY = (int) (y + height - drawHeight);

        // Renderiza o personagem normal
        if (facing_left) {
            g2d.drawImage(frame, drawX + drawWidth, drawY, -drawWidth, drawHeight, null);
        } else {
            g2d.drawImage(frame, drawX, drawY, drawWidth, drawHeight, null);
        }
        
        // Aplica a cor vermelha piscante por cima do sprite (a cada 4 frames)
        if (flashTicks > 0 && (flashTicks / 4) % 2 == 0) {
            
            // 1. Cria a imagem apenas UMA VEZ (ou se o tamanho do sprite mudar)
            if (redOverlay == null || redOverlay.getWidth() != frame.getWidth() || redOverlay.getHeight() != frame.getHeight()) {
                redOverlay = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
            }

            Graphics2D gOverlay = redOverlay.createGraphics();
            
            // 2. "Apaga" a imagem antiga (essencial já que estamos reaproveitando)
            gOverlay.setComposite(java.awt.AlphaComposite.Clear);
            gOverlay.fillRect(0, 0, redOverlay.getWidth(), redOverlay.getHeight());
            
            // 3. Volta para o modo normal e desenha o frame atual do jogador
            gOverlay.setComposite(java.awt.AlphaComposite.SrcOver);
            gOverlay.drawImage(frame, 0, 0, null);
            
            // 4. Aplica a máscara vermelha (SrcAtop)
            gOverlay.setComposite(java.awt.AlphaComposite.SrcAtop);
            gOverlay.setColor(new Color(255, 0, 0, 160)); // Vermelho translúcido
            gOverlay.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            
            gOverlay.dispose(); // Descarta APENAS o pincel temporário, não o principal do jogo!

            // 5. Renderiza a camada vermelha por cima na mesma posição
            if (facing_left) {
                g2d.drawImage(redOverlay, drawX + drawWidth, drawY, -drawWidth, drawHeight, null);
            } else {
                g2d.drawImage(redOverlay, drawX, drawY, drawWidth, drawHeight, null);
            }
        }
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
    
    public void startDeath() {
        if (dying) return; // evita reiniciar se já está morrendo
        dying = true;
        deathTicksRemaining = DEATH_ANIMATION_DURATION;
    }
    
    public boolean isDying() {
        return dying;
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
    
    public void finishDeath() {
    	lives--;
    	
    	if(lives <= 0) dead = true;
    	hp = GameConfig.MAX_HP;
    	respawn();
    }
    
    public boolean isDead() {
    	return dead;
    }
    
    public boolean takeDamage(double damage) {
        if (hp <= 0 || isDying() || hurt) return false; // já está "morto" nesse ciclo, ignora dano repetido

        boolean fatalDamage = false;
        hp = Math.max(0, hp - damage);
        System.out.println("hp: " + hp);
        if (hp <= 0) {
            fatalDamage = true;
            startDeath();
        } else {
            // Ativa o estado de Hurt se sobreviveu ao dano
            hurt = true;
            hurtTicksRemaining = HURT_DURATION;
            flashTicks = HURT_DURATION;
        }

        return fatalDamage;
    }
    
    public void tickHurt() {
        if (hurt) {
            hurtTicksRemaining--;
            if (flashTicks > 0) flashTicks--;

            if (hurtTicksRemaining <= 0) {
                hurt = false;
            }
        }
    }

    public boolean isHurt() {
        return hurt;
    }
    
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean value) { this.onGround = value; }
    
    public void startFalling() {
        fallStartY = y;
    }

    public void checkFallDamage() {
    	if (isDying()) return;
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
	
	public void loadAnimations() {
	    BufferedImage idleSheet = ImageLoader.load("images/player/player_idle.png");
	    BufferedImage runSheet = ImageLoader.load("images/player/player_run.png");
	    BufferedImage jumpSheet = ImageLoader.load("images/player/player_jump.png");
	    BufferedImage hurtSheet = ImageLoader.load("images/player/player_hurt.png");
	    BufferedImage fallSheet = ImageLoader.load("images/player/player_fall.png");
	    BufferedImage deathSheet = ImageLoader.load("images/player/player_death.png");

	    idleAnimation = new Animation(idleSheet, 4, 30);  // ajusta frameCount e velocidade aos seus arquivos reais
	    runAnimation = new Animation(runSheet, 15, 8);
	    jumpAnimation = new Animation(jumpSheet, 9, 8);
	    hurtAnimation = new Animation(hurtSheet, 4, 8);
	    fallAnimation = new Animation(fallSheet, 3, 21);
	    deathAnimation = new Animation(deathSheet, 16, 27);

	    currentAnimation = idleAnimation;
	}

	public void tickDeath() {
		deathTicksRemaining--;
	    if (deathTicksRemaining <= 0) {
	        dying = false;
	        finishDeath(); // já reseta HP e faz respawn, como você já tinha
	    }
		
	}
}
