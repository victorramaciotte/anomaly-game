package entity;

public enum BlockType {
    NORMAL(false, false, true),
    FILL(false, false, true), 
    BREAKABLE(true, false, true),
    INVISIBLE (false, false, false),
    SLIPPERY(false, true, true);

    public final boolean breakable;
    public final boolean slippery;
    public final boolean visible;

    BlockType(boolean breakable, boolean slippery, boolean visible) {
        this.breakable = breakable;
        this.slippery = slippery;
        this.visible = visible;
    }
}
