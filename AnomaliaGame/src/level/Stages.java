package level;

import java.util.List;
import entity.Direction;

public class Stages {
    public static Campaign createDefaultCampaign() {
        List<StageConfig> stages = List.of(
            new StageConfig(1, 200, 200, Direction.LEFT_TO_RIGHT, 1.5),
            new StageConfig(2, 900, 200, Direction.LEFT_TO_RIGHT, 1.5)
        );
        return new Campaign(stages);
    }
}