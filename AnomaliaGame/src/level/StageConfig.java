package level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar layout: " + path, e);
        }
    }
	
	public int getIndex() { return index; }
	public double getStartX() { return startX; }
	public double getStartY() { return startY; }
    public Direction getAnomalyDirection() { return anomalyDirection; }
    public double getAnomalySpeed() { return anomalySpeed; }
    public List<String> getLayout() { return layout; }

}
