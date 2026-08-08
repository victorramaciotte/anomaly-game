package collision;

import java.util.List;

import entity.Anomaly;
import entity.Block;
import entity.Entity;
import entity.Player;
import main.GameConfig;

public class CollisionSystem {
	
	public boolean intersects(Entity a, Entity b) {
		return a.getBounds().intersects(b.getBounds());
	}
	
	public void resolveY(Player p, List<Block> blocks) {
		boolean landedThisTick = false;
		
		for (Block block : blocks) {
			if (!intersects(p, block) || !block.isSolid()) continue;
			
			if (p.getVelocityY() > 0) {
				p.setY(block.getY() - p.getHeight());
				p.resetJumps();
				landedThisTick = true;

	            if (!p.isOnGround()) {
	                block.stepOn();
	            }
			} 
			else if (p.getVelocityY() < 0) {
				p.setY(block.getY() + block.getHeight());
				
			}
	    }
		
		p.setOnGround(landedThisTick);
	}
	
	public void resolveX(Player p, List<Block> blocks) {
		
		for (Block block : blocks) {
			if (!intersects(p, block) || !block.isSolid()) continue;
			
			if (p.getVelocityX() > 0) {
				p.setX(block.getX() - p.getWidth());
			} 
			else if (p.getVelocityX() < 0) {
				p.setX(block.getX() + block.getWidth());
				
			}
		}
	}
	
	public void checkAnomalyDamage(Player p, Anomaly anomaly) {
	    if (p.getBounds().intersects(anomaly.getAffectedArea())) {
	        boolean fatal = p.takeDamage(GameConfig.ANOMALY_LIGHT_DAMAGE);
	        if(fatal) { p.takeLife(); }
	    }
	}
	
	public void affectBlocks(List<Block> blocks, Anomaly anomaly) {
		for (Block block : blocks) {
			if (block.getBounds().intersects(anomaly.getAffectedArea())) {
		        block.corrupt();
		    }
		}
	}
}
