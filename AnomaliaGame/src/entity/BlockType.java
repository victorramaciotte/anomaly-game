package entity;

public enum BlockType {
    NORMAL(false, false),
    BREAKABLE(true, false),
    SLIPPERY(false, true);

    public final boolean breakable;
    public final boolean slippery;

    BlockType(boolean breakable, boolean slippery) {
        this.breakable = breakable;
        this.slippery = slippery;
    }
}
