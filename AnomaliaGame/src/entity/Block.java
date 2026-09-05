package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import level.LevelBuilder;
import main.GameConfig;

public class Block extends Entity {
	private boolean visible = true;
	private BlockState state;
	private BlockType type;
	private BufferedImage currentSprite;
	private int stageIndex;
	private int spriteVariant;
	
	public Block(double x, double y, BlockType type, BlockState state, int stageIndex, int spriteVariant) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
		this.type = type;
		this.state = state;
		this.stageIndex = stageIndex;
		this.spriteVariant = spriteVariant;
		refreshSprite();
	}
	
	public Block(double x, double y, BlockType type, BlockState state, int stageIndex) {
		super(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
		this.type = type;
		this.state = state;
		this.stageIndex = stageIndex;
		this.spriteVariant = 0;
		refreshSprite();
	}
	
	private void refreshSprite() {
	    currentSprite = LevelBuilder.resolveSprite(stageIndex, type, state, spriteVariant);
	}
	
	public void setVisible(boolean visible) { this.visible = visible; }

	public void setSprite(BufferedImage sprite) {
	    this.currentSprite = sprite;
	}

	public void render(Graphics g) {
		if (!isVisible() || !isSolid()) return;

	    if (currentSprite != null) {
	    	int spriteWidth = currentSprite.getWidth();
	        int spriteHeight = currentSprite.getHeight();

	        // centraliza horizontalmente sobre o bloco, ancora a base do sprite na base do bloco
	        int drawX = (int) (x + width / 2 - spriteWidth / 2.0) - 1;
	        int drawY = (int) (y + height - spriteHeight) - 1; // sprite "cresce pra cima" a partir da base do tile

	        g.drawImage(currentSprite, drawX, drawY, (int) spriteWidth + 2 , (int) spriteHeight + 2, null);
	        return;
	    } else {
	        // fallback: desenho atual com formas/cores, pra blocos sem sprite ainda
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setColor(Color.gray);
	        g2d.fillRoundRect((int) x, (int) y, (int) width, (int) height, 8, 8);
	    }
	}
	
	public void corrupt() {
		if (state != BlockState.NORMAL) return;
		state = BlockState.CORRUPTED;
		refreshSprite();
	}
	
	public void crack() {
		if (type != BlockType.BREAKABLE || state != BlockState.CORRUPTED) return;
		state = BlockState.CRACKED;
		refreshSprite();
	}
	
	public void destroy() {
		if (state != BlockState.CRACKED) return;
		state = BlockState.DESTROID;
		refreshSprite();
	}
	
	public boolean isSolid() {
		return state != BlockState.DESTROID;
	}
	
	public boolean isVisible() {
		return type != BlockType.INVISIBLE;
	}
	
	public void stepOn() {
		if(state == BlockState.CORRUPTED) {
			crack();
		}
		else if(state == BlockState.CRACKED) {
			destroy();
		}
	}
	
	public BlockState getState() {
		return state;
	}
	
	public String getSpriteKey() {
	    if (type == BlockType.BREAKABLE && state == BlockState.CRACKED) {
	        return "breakable_cracked";
	    }
	    switch (type) {
	        case NORMAL: return "normal";
	        case BREAKABLE: return "breakable";
	        case FILL: return "fill";
	        default: return null; // INVISIBLE não tem sprite
	    }
	}
}
