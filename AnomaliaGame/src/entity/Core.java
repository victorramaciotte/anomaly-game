package entity;

import java.awt.Color;
import java.awt.Graphics;

public class Core extends Entity {
	
	boolean triggered = false;

	protected Core(double x, double y, double width, double height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
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
		g.setColor(Color.getHSBColor(200f / 360f, 0.55f, 0.98f));
		g.fillRect((int) x, (int) y, (int) width, (int) height);
	}
}
