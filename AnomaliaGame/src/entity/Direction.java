package entity;

public enum Direction {
    LEFT_TO_RIGHT(1, 0),
    RIGHT_TO_LEFT(-1, 0),
    BOTTOM_TO_TOP(0, -1),
    TOP_TO_BOTTOM(0, 1);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}