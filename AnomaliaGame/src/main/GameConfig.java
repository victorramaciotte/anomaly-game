package main;

public final class GameConfig {
    private GameConfig() {}

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 540;
    public static final int FPS = 120;
    public static final int UPS = 200;
    public static final int TILE_SIZE = 48;

    public static final double PLAYER_WIDTH = 34;
    public static final double PLAYER_HEIGHT = 48;
    public static final double MOVE_SPEED = 4.0;    // px por tick
    public static final double JUMP_FORCE = -12.0;   // px por tick
    public static final double GRAVITY = 0.5;         // px por tick²
    public static final int MAX_JUMPS = 2;

    public static final double MAX_HP = 100;
    public static final int STARTING_LIVES = 3;
    public static final double ANOMALY_DAMAGE_PER_SECOND = 15;

    public static final double FALL_DAMAGE_MIN_HEIGHT = 210;
    public static final double FALL_DAMAGE_AMOUNT = 25;

    public static final double VOID_Y = SCREEN_HEIGHT + 180;
}
