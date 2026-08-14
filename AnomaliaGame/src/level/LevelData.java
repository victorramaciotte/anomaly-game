package level;

import java.util.List;
import entity.Block;

public class LevelData {
    private final List<Block> blocks;
    private final double coreX;
    private final double coreY;

    public LevelData(List<Block> blocks, double coreX, double coreY) {
        this.blocks = blocks;
        this.coreX = coreX;
        this.coreY = coreY;
    }

    public List<Block> getBlocks() { return blocks; }
    public double getCoreX() { return coreX; }
    public double getCoreY() { return coreY; }
}