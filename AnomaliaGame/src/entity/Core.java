package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.ImageLoader;

public class Core extends Entity {
	
	boolean triggered = false;
	private BufferedImage coreSprite = ImageLoader.load("images/core/core.png");

	protected Core(double x, double y, double width, double height) {
		super(x, y, width, height);
	}
	
	public Core(double x, double y) {
		super(x, y, 24, 48);
	}
	
	public boolean onPLayerTouch(Player p) {
		if (triggered) return false; 
		if (!getBounds().intersects(p.getBounds())) return false;
		
		triggered = true;
		System.out.println("Estágio concluído!");
		return true;
	}

	public void render(Graphics g) {
		if (coreSprite != null) {
	    	int spriteWidth = (int) (coreSprite.getWidth() * 0.2);
	        int spriteHeight = (int) (coreSprite.getHeight() * 0.2);

	        // centraliza horizontalmente sobre o bloco, ancora a base do sprite na base do bloco
	        int drawX = (int) (x + width / 2 - spriteWidth / 2.0) - 1;
	        int drawY = (int) (y + height - spriteHeight) - 1; // sprite "cresce pra cima" a partir da base do tile

	        g.drawImage(coreSprite, drawX, drawY, (int) spriteWidth + 2 , (int) spriteHeight + 25, null);
	        return;
	    } else {
	        // fallback: desenho atual com formas/cores, pra blocos sem sprite ainda
	        Graphics2D g2d = (Graphics2D) g;
	        g.setColor(Color.getHSBColor(200f / 360f, 0.55f, 0.98f));
			g.fillRect((int) x, (int) y, (int) width, (int) height);
	    }
		
	}
}
