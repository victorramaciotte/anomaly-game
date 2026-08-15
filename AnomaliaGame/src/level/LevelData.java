package level;

import java.util.List;
import entity.Block;

public class LevelData {
    private final List<Block> blocks;
    private final double coreX;
    private final double coreY;
    int levelHeight, levelWidth;

    public LevelData(List<Block> blocks, double coreX, double coreY, int levelHeight, int levelWidth) {
        this.blocks = blocks;
        this.coreX = coreX;
        this.coreY = coreY;
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
    }

    public List<Block> getBlocks() { return blocks; }
    public double getCoreX() { return coreX; }
    public double getCoreY() { return coreY; }
    public int getLevelHeight() { return levelHeight; }
    public int getLevelWidth() { return levelWidth; }
}