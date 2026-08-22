package main;

public final class GameConfig {
    private GameConfig() {}

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int HUD_MARGIN = 10;
    public static final int FPS = 120;
    public static final int UPS = 200;
    public static final int TILE_SIZE = 64;
    public static final double FIXED_DELTA = 1.0 / FPS;

    public static final double PLAYER_WIDTH = 34;
    public static final double PLAYER_HEIGHT = 64;
    public static final double MOVE_SPEED = 2.0;
    public static final double ANOMALY_BASE_SPEED = 0.1;// px por tick
    public static final double JUMP_FORCE = -6.0;   // px por tick
    public static final double GRAVITY = 0.15;         // px por tick²
    public static final int MAX_JUMPS = 2;

    public static final double MAX_HP = 100;
    public static final int STARTING_LIVES = 3;
    public static final double ANOMALY_DAMAGE_PER_SECOND = 15.0/UPS;
    public static final double ANOMALY_LIGHT_DAMAGE = 5.0/UPS;

    public static final double FALL_DAMAGE_MIN_HEIGHT = 210;
    public static final double FALL_DAMAGE_AMOUNT = 25;

    public static final double VOID_MARGIN = 100;
}
