package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Background {

    private List<BufferedImage> variants;
    private List<BufferedImage> corruptedVariants;
    private double scrollFactor;

    public Background(List<BufferedImage> variants, List<BufferedImage> corruptedVariants, double scrollFactor) {
        this.variants = variants;
        this.corruptedVariants = corruptedVariants;
        this.scrollFactor = scrollFactor;
    }

    public void render(Graphics g, Camera camera, Rectangle2D affectedArea) {
        Graphics2D g2d = (Graphics2D) g;

        int offsetX = (int) (camera.getX() * scrollFactor);
        int imgWidth = variants.get(0).getWidth();
        int imgHeight = variants.get(0).getHeight();
        int y = GameConfig.SCREEN_HEIGHT - imgHeight;

        int firstTileWorldX = Math.floorDiv(offsetX, imgWidth) * imgWidth - imgWidth;

        for (int worldX = firstTileWorldX; worldX < offsetX + GameConfig.SCREEN_WIDTH + imgWidth; worldX += imgWidth) {
            int tileIndex = Math.floorDiv(worldX, imgWidth);
            int screenX = worldX - offsetX;

            BufferedImage normal = variants.get(Math.floorMod(tileIndex, variants.size()));

            if (corruptedVariants.isEmpty()) {
                g2d.drawImage(normal, screenX - 2, y, imgWidth + 2, imgHeight, null);
                continue;
            }

            double realWorldX = screenX + camera.getX();
            Rectangle2D tileWorldBounds = new Rectangle2D.Double(realWorldX, 0, imgWidth, GameConfig.SCREEN_HEIGHT);
            Rectangle2D intersection = tileWorldBounds.createIntersection(affectedArea);

            if (intersection.isEmpty()) {
                g2d.drawImage(normal, screenX - 2, y, imgWidth + 2, imgHeight, null);
                continue;
            }

            BufferedImage corrupted = corruptedVariants.get(Math.floorMod(tileIndex, corruptedVariants.size()));

            int corruptX = (int) (intersection.getX() - realWorldX) + screenX;
            int corruptY = (int) intersection.getY();
            int corruptW = (int) intersection.getWidth();
            int corruptH = (int) intersection.getHeight();

            Rectangle2D tileScreenBounds = new Rectangle2D.Double(screenX, y, imgWidth, imgHeight);
            Rectangle2D corruptScreenBounds = new Rectangle2D.Double(corruptX, corruptY, corruptW, corruptH);
            Rectangle2D visibleCorruption = tileScreenBounds.createIntersection(corruptScreenBounds);

            if (visibleCorruption.isEmpty()) {
                g2d.drawImage(normal, screenX - 2, y, imgWidth + 2, imgHeight, null);
                continue;
            }

            drawNormalOutsideCorruption(g2d, normal, screenX, y, imgWidth, imgHeight, visibleCorruption);
            drawImageClipped(g2d, corrupted, screenX, y, imgWidth, imgHeight, visibleCorruption);
        }
    }

    private void drawNormalOutsideCorruption(
            Graphics2D g2d,
            BufferedImage image,
            int x,
            int y,
            int width,
            int height,
            Rectangle2D corruption
    ) {
        double cx = corruption.getX();
        double cy = corruption.getY();
        double cw = corruption.getWidth();
        double ch = corruption.getHeight();

        drawImageClipped(g2d, image, x, y, width, height, new Rectangle2D.Double(x, y, width, cy - y));
        drawImageClipped(g2d, image, x, y, width, height, new Rectangle2D.Double(x, cy + ch, width, y + height - (cy + ch)));
        drawImageClipped(g2d, image, x, y, width, height, new Rectangle2D.Double(x, cy, cx - x, ch));
        drawImageClipped(g2d, image, x, y, width, height, new Rectangle2D.Double(cx + cw, cy, x + width - (cx + cw), ch));
    }

    private void drawImageClipped(
            Graphics2D g2d,
            BufferedImage image,
            int x,
            int y,
            int width,
            int height,
            Rectangle2D clip
    ) {
        if (clip.getWidth() <= 0 || clip.getHeight() <= 0) {
            return;
        }

        Shape originalClip = g2d.getClip();

        g2d.setClip(
                (int) clip.getX(),
                (int) clip.getY(),
                (int) Math.ceil(clip.getWidth()),
                (int) Math.ceil(clip.getHeight())
        );

        g2d.drawImage(image, x - 2, y, width + 2, height, null);
        g2d.setClip(originalClip);
    }
}