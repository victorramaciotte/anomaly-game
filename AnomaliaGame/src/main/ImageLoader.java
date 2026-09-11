package main;

import javax.imageio.ImageIO;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {
	private static final Map<String, BufferedImage> cache = new ConcurrentHashMap<>();

    public static BufferedImage load(String path) {
        return cache.computeIfAbsent(path, ImageLoader::readImage);
    }

    private static BufferedImage readImage(String path) {
    	try (InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }
    
    public static Font loadFont(String path, float size) {
        if (!path.startsWith("/")) path = "/" + path;
        
        try (InputStream is = ImageLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Fonte não encontrada: " + path);
                return new Font("SansSerif", Font.BOLD, (int) size); // Fallback se falhar
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size);
        }
    }
}