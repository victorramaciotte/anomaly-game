package collision;

import java.util.List;

import entity.Block;
import entity.Entity;
import entity.Player;

public class CollisionSystem {
	
	public boolean intersects(Entity a, Entity b) {
		return a.getBounds().intersects(b.getBounds());
	}
	
	public void resolveY(Player p, List<Block> blocks) {
		
		for (Block block : blocks) {
			if (!intersects(p, block)) continue;
			
			if (p.getVelocityY() > 0) {
				p.setY(block.getY() - p.getHeight());
				p.resetJumps();
			} 
			else if (p.getVelocityY() < 0) {
				p.setY(block.getY() + block.getHeight());
				
			}
	    
	    }
	}
	
	public void resolveX(Player p, List<Block> blocks) {
		
		for (Block block : blocks) {
			if (!intersects(p, block)) continue;
			
			if (p.getVelocityX() > 0) {
				p.setX(block.getX() - p.getWidth());
			} 
			else if (p.getVelocityX() < 0) {
				p.setX(block.getX() + block.getWidth());
				
			}
		}
	}
}
