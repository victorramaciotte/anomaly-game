package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Background {

    public enum Orientation { HORIZONTAL, VERTICAL }

    private BufferedImage image;
    private BufferedImage corruptedImage;
    private double scrollFactor;
    private Orientation orientation;

    public Background(BufferedImage image, BufferedImage corruptedImage, double scrollFactor, Orientation orientation) {
        this.image = image;
        this.corruptedImage = corruptedImage;
        this.scrollFactor = scrollFactor;
        this.orientation = orientation;
    }

    public void render(Graphics g, Camera camera, Rectangle2D affectedArea) {
        Graphics2D g2d = (Graphics2D) g;

        int x, y;
        if (orientation == Orientation.HORIZONTAL) {
            x = -(int) (camera.getX() * scrollFactor);
            y = -(int) (camera.getY() * scrollFactor);
        } else {
            x = -(int) (camera.getX() * scrollFactor);
            y = -(int) (camera.getY() * scrollFactor);
        }

        if (corruptedImage == null) {
            g2d.drawImage(image, x, y, null);
            return;
        }

        // posição real do mundo por trás dessa imagem (usa a câmera real, não a escala de parallax)
        double realWorldX = x + camera.getX();
        double realWorldY = y + camera.getY();
        Rectangle2D imageWorldBounds = new Rectangle2D.Double(realWorldX, realWorldY, image.getWidth(), image.getHeight());
        Rectangle2D intersection = imageWorldBounds.createIntersection(affectedArea);

        if (intersection.isEmpty()) {
            g2d.drawImage(image, x, y, null);
            return;
        }

        // converte a interseção de volta pra coordenada de tela
        double corruptScreenX = intersection.getX() - realWorldX + x;
        double corruptScreenY = intersection.getY() - realWorldY + y;
        Rectangle2D corruption = new Rectangle2D.Double(corruptScreenX, corruptScreenY, intersection.getWidth(), intersection.getHeight());

        drawNormalOutsideCorruption(g2d, image, x, y, corruption);
        drawImageClipped(g2d, corruptedImage, x, y, corruption);
    }

    private void drawNormalOutsideCorruption(Graphics2D g2d, BufferedImage img, int x, int y, Rectangle2D c) {
        double cx = c.getX(), cy = c.getY(), cw = c.getWidth(), ch = c.getHeight();
        int w = img.getWidth(), h = img.getHeight();

        drawImageClipped(g2d, img, x, y, new Rectangle2D.Double(x, y, w, cy - y));
        drawImageClipped(g2d, img, x, y, new Rectangle2D.Double(x, cy + ch, w, y + h - (cy + ch)));
        drawImageClipped(g2d, img, x, y, new Rectangle2D.Double(x, cy, cx - x, ch));
        drawImageClipped(g2d, img, x, y, new Rectangle2D.Double(cx + cw, cy, x + w - (cx + cw), ch));
    }

    private void drawImageClipped(Graphics2D g2d, BufferedImage img, int x, int y, Rectangle2D clip) {
        if (clip.getWidth() <= 0 || clip.getHeight() <= 0) return;

        Shape originalClip = g2d.getClip();
        g2d.setClip((int) clip.getX(), (int) clip.getY(), (int) Math.ceil(clip.getWidth()), (int) Math.ceil(clip.getHeight()));
        g2d.drawImage(img, x, y, null);
        g2d.setClip(originalClip);
    }
}