package main;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CorruptionOverlay {
    private List<BufferedImage> frames;
    private int currentFrame = 0;
    private int ticksPerFrame; // quantos ticks de update até trocar de frame
    private int tickCounter = 0;

    public CorruptionOverlay(List<BufferedImage> frames, int ticksPerFrame) {
        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
    }

    public void update() {
    	if (frames == null || frames.isEmpty()) {
            return;
        }

        tickCounter++;
        if (tickCounter >= ticksPerFrame) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % frames.size(); // volta pro início ao chegar no fim
        }
    }

    public void render(Graphics g, Camera camera, Rectangle2D affectedArea) {
    	if (frames == null || frames.isEmpty()) {
            return;
        }
    	
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage frame = frames.get(currentFrame);

        int screenX = (int) (affectedArea.getX() - camera.getX());
        int screenY = (int) affectedArea.getY();
        int screenW = (int) affectedArea.getWidth();
        int screenH = (int) affectedArea.getHeight();
        int fadeWidth = 60;

        Shape originalClip = g2d.getClip();
        Composite originalComposite = g2d.getComposite();
        
        g2d.setClip(screenX, screenY, Math.max(0, screenW - fadeWidth), screenH);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.drawImage(frame, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);
        
     // faixa de transição: mesma imagem, mas agora com clip só na faixa final,
        // e um gradiente controlando a opacidade dessa faixa especificamente
        g2d.setClip(screenX + screenW - fadeWidth, screenY, fadeWidth, screenH);

        // desenha em fatias finas com opacidade decrescente — simples e direto
        int slices = 10;
        int sliceWidth = fadeWidth / slices;
        for (int i = 0; i < slices; i++) {
            float alpha = 0.4f * (1f - (float) i / slices); // opacidade caindo gradualmente
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));
            int sliceX = screenX + screenW - fadeWidth + i * sliceWidth;
            g2d.setClip(sliceX, screenY, sliceWidth + 2, screenH); // +1 evita gaps de arredondamento
            g2d.drawImage(frame, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, null);
        }
        
        g2d.setComposite(originalComposite);
        g2d.setClip(originalClip);
    }
}
