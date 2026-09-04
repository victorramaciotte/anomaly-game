package level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import entity.Direction;

public class StageConfig {
	int index;
	double anomalySpeed;
	double startX, startY;
	Direction anomalyDirection;
	private List<String> layout;
	private List<String> backgroundLayers;
	private List<String> corruptedBackgroundLayers;
	private static final String LEVELS_DIR = "resources/levels/stage";
    private static final String EXT = ".txt";
    private double cameraVerticalOffset;
    private List<String> overlayFrames;
    
    public StageConfig(int index, double startX, double startY, Direction anomalyDirection, double anomalySpeed) {
		this.index = index;
		this.startX = startX;
		this.startY = startY;
		this.anomalyDirection = anomalyDirection;
		this.anomalySpeed = anomalySpeed;
		this.layout = loadLayout(LEVELS_DIR + index + EXT);
		this.backgroundLayers = discoverBackgroundLayers(index);
		this.corruptedBackgroundLayers = discoverCorruptedLayers(this.backgroundLayers);
		this.overlayFrames = discoverOverlayFrames(index);
		
		if (anomalyDirection == Direction.LEFT_TO_RIGHT || anomalyDirection == Direction.RIGHT_TO_LEFT) {
			this.cameraVerticalOffset = 100;
		}
		else {
			this.cameraVerticalOffset = 0;
		}
	}
	
	public StageConfig(int index, double startX, double startY, Direction anomalyDirection, double anomalySpeed, String layoutPath) {
		this.index = index;
		this.startX = startX;
		this.startY = startY;
		this.anomalyDirection = anomalyDirection;
		this.anomalySpeed = anomalySpeed;
		this.layout = loadLayout(layoutPath);
		this.backgroundLayers = discoverBackgroundLayers(index);
		this.corruptedBackgroundLayers = discoverCorruptedLayers(this.backgroundLayers);
		
		if (anomalyDirection == Direction.LEFT_TO_RIGHT || anomalyDirection == Direction.RIGHT_TO_LEFT) {
			this.cameraVerticalOffset = 100;
		}
		else {
			this.cameraVerticalOffset = 0;
		}
	}
	
	private List<String> loadLayout(String path) {
		List<String> lines = new ArrayList<>();
		
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(path);
		         BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

		        String line;
		        while ((line = reader.readLine()) != null) {
		            lines.add(line);
		        }
		    } catch (IOException | NullPointerException e) {
		        throw new RuntimeException("Falha ao carregar layout: " + path, e);
		    }

		    return lines;
    }
	
	private List<String> discoverBackgroundLayers(int stageIndex) {
	    List<String> layers = new ArrayList<>();
	    String basePath = "resources/images/backgrounds/stage" + stageIndex + "/";

	    int layerIndex = 0;
	    while (true) {
	        String path = basePath + "layer" + layerIndex + ".png";
	        if (getClass().getClassLoader().getResource(path) == null) break;
	        layers.add(path);
	        layerIndex++;
	    }

	    return layers;
	}

    private List<String> discoverVariants(String basePath, String layerName) {
        List<String> variants = new ArrayList<>();
        char letter = 'a';

        while (true) {
            String path = basePath + layerName + letter + ".png";
            if (getClass().getClassLoader().getResource(path) == null) {
                break;
            }
            variants.add(path);
            letter++;
        }

        return variants;
    }
    
    private List<String> discoverCorruptedLayers(List<String> normalLayers) {
        List<String> corrupted = new ArrayList<>();
        for (String normalPath : normalLayers) {
            String corruptedPath = normalPath.replace(".png", "_corrupted.png");
            if (getClass().getClassLoader().getResource(corruptedPath) != null) {
                corrupted.add(corruptedPath);
            } else {
                corrupted.add(null);
            }
        }
        return corrupted;
    }
    private List<String> discoverCorruptedVariants(String basePath, String layerName) {
        List<String> variants = new ArrayList<>();
        char letter = 'a';

        while (true) {
            String path = basePath + layerName + letter + "_corrupted.png";
            if (getClass().getClassLoader().getResource(path) == null) {
                break;
            }
            variants.add(path);
            letter++;
        }

        return variants;
    }
    
    private List<String> discoverOverlayFrames(int stageIndex) {
        List<String> frames = new ArrayList<>();
        String basePath = "resources/images/backgrounds/stage" + stageIndex + "/"; // mesma pasta base dos outros
        char letter = 'a';

        while (true) {
            String path = basePath + "overlay" + letter + ".png";
            if (getClass().getClassLoader().getResource(path) == null) {
                break;
            }
            frames.add(path);
            letter++;
        }

        return frames;
    }
	
	public int getIndex() { return index; }
	public double getStartX() { return startX; }
	public double getStartY() { return startY; }
    public Direction getAnomalyDirection() { return anomalyDirection; }
    public double getAnomalySpeed() { return anomalySpeed; }
    public List<String> getLayout() { return layout; }
    public List<String> getBackgroundLayers() { return backgroundLayers; }
    public double getCameraVerticalOffset() { return cameraVerticalOffset; }
	public List<String> getCorruptedBackgroundLayers() { return corruptedBackgroundLayers; }
	public List<String> getOverlayFrames() { return overlayFrames; }

}
