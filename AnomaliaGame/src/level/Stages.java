package level;

import java.util.List;
import entity.Direction;

public class Stages {
    public static Campaign createDefaultCampaign() {
        List<StageConfig> stages = List.of(
        	new StageConfig(1, 200, 200, Direction.LEFT_TO_RIGHT, 1.0),
            new StageConfig(2, 300, 2400, Direction.BOTTOM_TO_TOP, 1.2),
            new StageConfig(3, 144, 480, Direction.LEFT_TO_RIGHT, 1.4), 
            new StageConfig(4, 50, 90, Direction.TOP_TO_BOTTOM, 1.6),
            new StageConfig(5, 300, 4268, Direction.BOTTOM_TO_TOP, 1.8)
        );
        return new Campaign(stages);
    }
}