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
	private static final String LEVELS_DIR = "resources/levels/stage";
    private static final String EXT = ".txt";
    
    public StageConfig(int index, double startX, double startY, Direction anomalyDirection, double anomalySpeed) {
		this.index = index;
		this.startX = startX;
		this.startY = startY;
		this.anomalyDirection = anomalyDirection;
		this.anomalySpeed = anomalySpeed;
		this.layout = loadLayout(LEVELS_DIR + index + EXT);
	}
	
	public StageConfig(int index, double startX, double startY, Direction anomalyDirection, double anomalySpeed, String layoutPath) {
		this.index = index;
		this.startX = startX;
		this.startY = startY;
		this.anomalyDirection = anomalyDirection;
		this.anomalySpeed = anomalySpeed;
		this.layout = loadLayout(layoutPath);
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
	
	public int getIndex() { return index; }
	public double getStartX() { return startX; }
	public double getStartY() { return startY; }
    public Direction getAnomalyDirection() { return anomalyDirection; }
    public double getAnomalySpeed() { return anomalySpeed; }
    public List<String> getLayout() { return layout; }

}
