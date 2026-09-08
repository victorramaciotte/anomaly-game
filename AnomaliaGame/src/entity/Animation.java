package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame = 0;
    private int ticksPerFrame;
    private int tickCounter = 0;

    public Animation(BufferedImage spriteSheet, int frameCount, int ticksPerFrame) {
        this.ticksPerFrame = ticksPerFrame;
        this.frames = new BufferedImage[frameCount];

        int frameWidth = spriteSheet.getWidth() / frameCount;
        int frameHeight = spriteSheet.getHeight();

        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }
    }

    public void update() {
        tickCounter++;
        if (tickCounter >= ticksPerFrame) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % frames.length;
        }
    }

    public void reset() {
        currentFrame = 0;
        tickCounter = 0;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
}