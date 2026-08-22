package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Background {
	
	private List<BufferedImage> variants;
	private double scrollFactor;
	
	public Background(List<BufferedImage> variants, double scrollFactor) {
		this.variants = variants;
		this.scrollFactor = scrollFactor;
	}
	
	public void render(Graphics g, Camera camera) {
        int offsetX = (int) (camera.getX() * scrollFactor);
        int offsetY = (int) (camera.getY() * scrollFactor);

        int imgWidth = variants.get(0).getWidth(); 
        int imgHeight = variants.get(0).getHeight();
        
        // ponto de partida: primeira posição "à esquerda" que ainda cobre a tela
        int startX = -(offsetX % imgWidth) - imgWidth;
        int y = GameConfig.SCREEN_HEIGHT - imgHeight;

     // primeira posição de tile visível, em coordenadas absolutas do mundo
        int firstTileWorldX = Math.floorDiv(offsetX, imgWidth) * imgWidth - imgWidth;

        for (int worldX = firstTileWorldX; worldX < offsetX + GameConfig.SCREEN_WIDTH + imgWidth; worldX += imgWidth) {
            int tileIndex = Math.floorDiv(worldX, imgWidth); // índice absoluto, estável
            BufferedImage chosen = variants.get(Math.floorMod(tileIndex, variants.size()));

            int screenX = worldX - offsetX; // converte de coordenada do mundo pra coordenada da tela

            g.drawImage(chosen, screenX - 2, y, imgWidth + 2, imgHeight, null);
        }
    }
}
